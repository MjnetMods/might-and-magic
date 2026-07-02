package org.mjli.mam.block_entity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamBlockEntities;

public class DaybloomBlockEntity extends GeneratingFlowerBlockEntity {

    // Bootstrap trickle, not a production source — mirrors the Nox bootstrap's tiny yield
    // (design/magic/10_apothecary.md § Nox bootstrap). See design/23_verdant-generating-flowers.md.
    private static final int LIFETIME_TICKS = 10;

    private int ticksGenerated;

    public DaybloomBlockEntity(BlockPos pos, BlockState state) {
        super(MamBlockEntities.DAYBLOOM.get(), pos, state);
    }

    @Override
    public void tickFlower() {
        Level level = getLevel();
        if (level == null || level.isClientSide) return;
        if (level.getDayTime() % 24000L < 12000L && level.canSeeSky(worldPosition)) {
            addMana(1);
            ticksGenerated++;
            if (ticksGenerated >= LIFETIME_TICKS) {
                level.destroyBlock(worldPosition, false);
            }
        }
    }

    @Override
    public int getMaxEnergy() {
        return 900;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("ticksGenerated", ticksGenerated);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ticksGenerated = tag.getInt("ticksGenerated");
    }
}
