package org.mjli.mam.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MysticalMushroomBlock extends BushBlock {
    public static final MapCodec<MysticalMushroomBlock> CODEC = simpleCodec(props -> new MysticalMushroomBlock(DyeColor.WHITE, props));

    @Override
    public MapCodec<? extends BushBlock> codec() { return CODEC; }
    private static final VoxelShape SHAPE = box(4.8, 0, 4.8, 12.8, 16, 12.8);
    public final DyeColor color;

    public MysticalMushroomBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.isSolidRender(level, pos.below()) || below.is(net.minecraft.tags.BlockTags.MUSHROOM_GROW_BLOCK);
    }
}
