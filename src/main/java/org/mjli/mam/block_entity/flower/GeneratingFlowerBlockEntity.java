package org.mjli.mam.block_entity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.api.internal.EnergyBlockType;
import org.mjli.mam.api.internal.EnergyNetworkAction;
import org.mjli.mam.api.internal.EnergyNetworkHandler;
import org.mjli.mam.api.energy.EnergyCollector;
import org.mjli.mam.api.energy.EnergyPool;

import javax.annotation.Nullable;

public abstract class GeneratingFlowerBlockEntity extends BlockEntity implements EnergyCollector {
    protected static final int BIND_RADIUS = 6;

    private int energy;
    @Nullable private BlockPos boundPoolPos;
    private boolean addedToNetwork;

    protected GeneratingFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract void tickFlower();

    protected void addMana(int amount) {
        energy = Math.min(energy + amount, getMaxEnergy());
    }

    protected void emptyManaIntoCollector() {
        if (energy <= 0) return;
        EnergyPool pool = findBoundPool();
        if (pool != null && !pool.isFull()) {
            pool.receiveEnergy(energy);
            energy = 0;
        }
    }

    @Nullable
    protected EnergyPool findBoundPool() {
        if (level == null) return null;
        if (boundPoolPos != null) {
            if (level.getBlockEntity(boundPoolPos) instanceof EnergyPool pool) return pool;
            boundPoolPos = null;
        }
        EnergyPool closest = EnergyNetworkHandler.INSTANCE.getClosestPool(worldPosition, level, BIND_RADIUS);
        if (closest instanceof BlockEntity be) boundPoolPos = be.getBlockPos();
        return closest;
    }

    public void serverTick() {
        if (!addedToNetwork) {
            EnergyNetworkHandler.INSTANCE.fireEvent(this, EnergyBlockType.COLLECTOR, EnergyNetworkAction.ADD);
            addedToNetwork = true;
        }
        tickFlower();
        emptyManaIntoCollector();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        EnergyNetworkHandler.INSTANCE.fireEvent(this, EnergyBlockType.COLLECTOR, EnergyNetworkAction.REMOVE);
    }

    // EnergyCollector / EnergyReceiver
    @Override public int getCurrentEnergy() { return energy; }
    @Override public boolean isFull() { return energy >= getMaxEnergy(); }
    @Override public void receiveEnergy(int amount) { energy = Math.max(0, Math.min(energy + amount, getMaxEnergy())); }
    @Override public boolean canReceiveEnergyFromBursts() { return false; }
    @Override public float getEnergyYieldMultiplier() { return 1.0f; }
    @Override public void onClientDisplayTick() {}

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("energy", energy);
        if (boundPoolPos != null) {
            tag.putLong("boundPool", boundPoolPos.asLong());
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        energy = tag.getInt("energy");
        if (tag.contains("boundPool")) {
            boundPoolPos = BlockPos.of(tag.getLong("boundPool"));
        }
    }
}
