package org.mjli.mam.block_entity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamBlockEntities;

public class DewpetalBlockEntity extends GeneratingFlowerBlockEntity {

    public DewpetalBlockEntity(BlockPos pos, BlockState state) {
        super(MamBlockEntities.DEWPETAL.get(), pos, state);
    }

    @Override
    public void tickFlower() {
        Level level = getLevel();
        if (level == null || level.isClientSide || isFull()) return;
        if ((level.isRainingAt(worldPosition.above()) || hasAdjacentWater(level))
                && level.getGameTime() % 2 == 0) {
            addMana(1);
        }
    }

    private boolean hasAdjacentWater(Level level) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (level.getFluidState(worldPosition.relative(dir)).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMaxMana() {
        return 900;
    }
}
