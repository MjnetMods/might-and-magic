package org.mjli.mam.block_entity.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamBlockEntities;
import org.mjli.mam.api.internal.ManaBlockType;
import org.mjli.mam.api.internal.ManaNetworkAction;
import org.mjli.mam.api.internal.ManaNetworkHandler;
import org.mjli.mam.api.mana.ManaPool;

import java.util.Optional;

public class ManaPoolBlockEntity extends BlockEntity implements ManaPool {
    public static final int MAX_MANA = 1_000_000;

    private int mana;
    private boolean outputting;
    private Optional<DyeColor> color = Optional.empty();
    private boolean addedToNetwork;

    public ManaPoolBlockEntity(BlockPos pos, BlockState state) {
        super(MamBlockEntities.MANA_POOL.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ManaPoolBlockEntity self) {
        if (!self.addedToNetwork) {
            ManaNetworkHandler.INSTANCE.fireManaNetworkEvent(self, ManaBlockType.POOL, ManaNetworkAction.ADD);
            self.addedToNetwork = true;
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        ManaNetworkHandler.INSTANCE.fireManaNetworkEvent(this, ManaBlockType.POOL, ManaNetworkAction.REMOVE);
    }

    public static int calculateComparatorLevel(int mana, int max) {
        int val = (int) ((double) mana / (double) max * 15.0);
        if (mana > 0) val = Math.max(val, 1);
        return val;
    }

    // ManaPool
    @Override public boolean isOutputtingPower() { return outputting; }
    @Override public Optional<DyeColor> getColor() { return color; }
    @Override public void setColor(Optional<DyeColor> color) { this.color = color; setChanged(); }

    // ManaReceiver
    @Override public int getCurrentMana() { return mana; }
    @Override public int getMaxMana() { return MAX_MANA; }
    @Override public boolean isFull() { return mana >= MAX_MANA; }
    @Override public void receiveMana(int amount) { mana = Math.max(0, Math.min(mana + amount, MAX_MANA)); setChanged(); }
    @Override public boolean canReceiveManaFromBursts() { return true; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("mana", mana);
        tag.putBoolean("outputting", outputting);
        color.ifPresent(c -> tag.putByte("color", (byte) c.getId()));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        mana = tag.getInt("mana");
        outputting = tag.getBoolean("outputting");
        color = tag.contains("color") ? Optional.of(DyeColor.byId(tag.getByte("color"))) : Optional.empty();
    }
}
