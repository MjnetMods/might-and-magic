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

public class SpreaderBlock extends Block {
    public static final MapCodec<SpreaderBlock> CODEC = simpleCodec(SpreaderBlock::new);

    // Mirrors models/block/shapes/mana_spreader.json (ported from Botania's spreader.json) —
    // without this, the block falls back to a full 16x16x16 cube for collision/outline/
    // face-occlusion (design/magic/16_mana-spreader.md).
    private static final VoxelShape SHAPE = Shapes.or(
        Block.box(2, 2, 2, 3, 14, 14),
        Block.box(13, 2, 2, 14, 14, 14),
        Block.box(3, 13, 2, 13, 14, 14),
        Block.box(3, 2, 2, 13, 3, 14),
        Block.box(3, 3, 13, 13, 13, 14),
        Block.box(6, 10, 2, 13, 13, 3),
        Block.box(3, 3, 2, 10, 6, 3),
        Block.box(3, 6, 2, 6, 13, 3),
        Block.box(10, 3, 2, 13, 10, 3)
    );

    public SpreaderBlock(BlockBehaviour.Properties properties) {
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
