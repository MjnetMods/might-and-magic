package org.mjli.mam.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MysticalFlowerBlock extends FlowerBlock implements BonemealableBlock {
    public final DyeColor color;
    private final Supplier<Block> tallFlower;

    public MysticalFlowerBlock(DyeColor color, Holder<MobEffect> effect, int effectDuration, Supplier<Block> tallFlower, BlockBehaviour.Properties properties) {
        super(effect, effectDuration, properties);
        this.color = color;
        this.tallFlower = tallFlower;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return super.canSurvive(state, level, pos);
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
        Block tall = tallFlower.get();
        if (tall instanceof DoublePlantBlock) {
            DoublePlantBlock.placeAt(world, tall.defaultBlockState(), pos, 3);
        }
    }
}
