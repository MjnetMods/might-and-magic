package org.mjli.mam.block.flower;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mjli.mam.block_entity.flower.PureDaisyBlockEntity;
import org.mjli.mam.MamBlockEntities;

import javax.annotation.Nullable;

public class PureDaisyBlock extends BaseEntityBlock {
    public static final MapCodec<PureDaisyBlock> CODEC = simpleCodec(PureDaisyBlock::new);

    private static final VoxelShape SHAPE = box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

    public PureDaisyBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PureDaisyBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(net.minecraft.world.level.Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, MamBlockEntities.PURE_DAISY.get(), PureDaisyBlockEntity::serverTick);
    }
}
