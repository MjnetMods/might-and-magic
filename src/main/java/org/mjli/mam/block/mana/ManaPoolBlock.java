package org.mjli.mam.block.mana;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamBlockEntities;
import org.mjli.mam.block_entity.mana.ManaPoolBlockEntity;

import javax.annotation.Nullable;

public class ManaPoolBlock extends BaseEntityBlock {
    public static final MapCodec<ManaPoolBlock> CODEC = simpleCodec(ManaPoolBlock::new);

    public ManaPoolBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ManaPoolBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MamBlockEntities.MANA_POOL.get(), ManaPoolBlockEntity::tick);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ManaPoolBlockEntity pool) {
            return ManaPoolBlockEntity.calculateComparatorLevel(pool.getCurrentMana(), pool.getMaxMana());
        }
        return 0;
    }
}
