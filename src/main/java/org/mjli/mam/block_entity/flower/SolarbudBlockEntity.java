package org.mjli.mam.block_entity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamBlockEntities;

public class SolarbudBlockEntity extends GeneratingFlowerBlockEntity {

    public SolarbudBlockEntity(BlockPos pos, BlockState state) {
        super(MamBlockEntities.SOLARBUD.get(), pos, state);
    }

    @Override
    public void tickFlower() {
        Level level = getLevel();
        if (level == null || level.isClientSide || isFull()) return;
        if (level.getDayTime() % 24000L < 12000L && level.canSeeSky(worldPosition)) {
            addMana(1);
        }
    }

    @Override
    public int getMaxMana() {
        return 900;
    }
}
