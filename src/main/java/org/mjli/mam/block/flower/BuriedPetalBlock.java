package org.mjli.mam.block.flower;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BuriedPetalBlock extends BushBlock implements BonemealableBlock {
    // Codec never invoked at runtime (blocks serialized by registry ID in blockstates)
    public static final MapCodec<BuriedPetalBlock> CODEC = simpleCodec(
        props -> new BuriedPetalBlock(DyeColor.WHITE, () -> null, props));

    @Override
    protected MapCodec<? extends BushBlock> codec() { return CODEC; }

    private static final VoxelShape SHAPE = box(0, 0, 0, 16, 1.6, 16);

    public final DyeColor color;
    private final Supplier<Block> flower;

    public BuriedPetalBlock(DyeColor color, Supplier<Block> flower, Properties properties) {
        super(properties);
        this.color = color;
        this.flower = flower;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
        return world.isEmptyBlock(pos.above());
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level world, @NotNull RandomSource rand, @NotNull BlockPos pos, @NotNull BlockState state) {
        return isValidBonemealTarget(world, pos, state);
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel world, @NotNull RandomSource rand, @NotNull BlockPos pos, @NotNull BlockState state) {
        world.setBlockAndUpdate(pos, flower.get().defaultBlockState());
    }
}
