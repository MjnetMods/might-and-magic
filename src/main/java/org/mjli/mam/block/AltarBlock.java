package org.mjli.mam.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AltarBlock extends Block {
    public static final MapCodec<AltarBlock> CODEC = simpleCodec(AltarBlock::new);

    // Mirrors models/block/shapes/altar.json — without this, the block falls back to a full
    // 16x16x16 cube for collision/outline/face-occlusion (design/magic/20_altar.md).
    private static final VoxelShape SHAPE = Shapes.or(
        Block.box(0, 6, 0, 16, 12, 16),
        Block.box(4, 4, 4, 12, 6, 12),
        Block.box(2, 0, 2, 14, 4, 14)
    );

    public AltarBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends Block> codec() { return CODEC; }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
