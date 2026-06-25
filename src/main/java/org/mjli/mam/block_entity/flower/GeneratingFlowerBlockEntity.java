package org.mjli.mam.block_entity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.api.internal.ManaBlockType;
import org.mjli.mam.api.internal.ManaNetworkAction;
import org.mjli.mam.api.internal.ManaNetworkHandler;
import org.mjli.mam.api.mana.ManaCollector;
import org.mjli.mam.api.mana.ManaPool;

import javax.annotation.Nullable;

public abstract class GeneratingFlowerBlockEntity extends BlockEntity implements ManaCollector {
    protected static final int BIND_RADIUS = 6;

    private int mana;
    @Nullable private BlockPos boundPoolPos;
    private boolean addedToNetwork;

    protected GeneratingFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract void tickFlower();

    protected void addMana(int amount) {
        mana = Math.min(mana + amount, getMaxMana());
    }

    protected void emptyManaIntoCollector() {
        if (mana <= 0) return;
        ManaPool pool = findBoundPool();
        if (pool != null && !pool.isFull()) {
            pool.receiveMana(mana);
            mana = 0;
        }
    }

    @Nullable
    protected ManaPool findBoundPool() {
        if (level == null) return null;
        if (boundPoolPos != null) {
            if (level.getBlockEntity(boundPoolPos) instanceof ManaPool pool) return pool;
            boundPoolPos = null;
        }
        ManaPool closest = ManaNetworkHandler.INSTANCE.getClosestPool(worldPosition, level, BIND_RADIUS);
        if (closest instanceof BlockEntity be) boundPoolPos = be.getBlockPos();
        return closest;
    }

    public void serverTick() {
        if (!addedToNetwork) {
            ManaNetworkHandler.INSTANCE.fireManaNetworkEvent(this, ManaBlockType.COLLECTOR, ManaNetworkAction.ADD);
            addedToNetwork = true;
        }
        tickFlower();
        emptyManaIntoCollector();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        ManaNetworkHandler.INSTANCE.fireManaNetworkEvent(this, ManaBlockType.COLLECTOR, ManaNetworkAction.REMOVE);
    }

    // ManaCollector / ManaReceiver
    @Override public int getCurrentMana() { return mana; }
    @Override public boolean isFull() { return mana >= getMaxMana(); }
    @Override public void receiveMana(int amount) { mana = Math.max(0, Math.min(mana + amount, getMaxMana())); }
    @Override public boolean canReceiveManaFromBursts() { return false; }
    @Override public float getManaYieldMultiplier() { return 1.0f; }
    @Override public void onClientDisplayTick() {}

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("mana", mana);
        if (boundPoolPos != null) {
            tag.putLong("boundPool", boundPoolPos.asLong());
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        mana = tag.getInt("mana");
        if (tag.contains("boundPool")) {
            boundPoolPos = BlockPos.of(tag.getLong("boundPool"));
        }
    }
}
