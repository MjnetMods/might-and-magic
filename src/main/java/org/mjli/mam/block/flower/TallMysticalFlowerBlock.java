package org.mjli.mam.block.flower;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TallMysticalFlowerBlock extends TallFlowerBlock implements IShearable {
    public final DyeColor color;
    private final Supplier<ItemStack> petal;

    public TallMysticalFlowerBlock(DyeColor color, Supplier<ItemStack> petal, Properties properties) {
        super(properties);
        this.color = color;
        this.petal = petal;
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
        return false;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level,
            @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        if (stack.canPerformAction(ItemAbilities.SHEARS_HARVEST) && isShearable(player, stack, level, pos)) {
            List<ItemStack> drops = onSheared(player, stack, level, pos);
            if (!level.isClientSide) {
                drops.forEach(drop -> spawnShearedDrop(level, pos, drop));
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    public boolean isShearable(@NotNull Player player, @NotNull ItemStack item, @NotNull Level level, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@NotNull Player player, @NotNull ItemStack item, @NotNull Level level, @NotNull BlockPos pos) {
        level.destroyBlock(pos, false);
        return List.of(petal.get().copyWithCount(2));
    }
}
