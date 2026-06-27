package org.mjli.mam.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FloralPowderItem extends Item {
    private static final int RANGE = 3;
    private static final TagKey<Block> MYSTICAL_FLOWERS =
            TagKey.create(net.minecraft.core.registries.Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath("mam", "mystical_flowers"));

    public FloralPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        var flowersTag = BuiltInRegistries.BLOCK.getTag(MYSTICAL_FLOWERS);
        if (flowersTag.isEmpty() || flowersTag.get().size() == 0) {
            return InteractionResult.FAIL;
        }

        List<BlockPos> valid = new ArrayList<>();
        for (BlockPos candidate : BlockPos.betweenClosed(
                pos.getX() - RANGE, pos.getY() - 1, pos.getZ() - RANGE,
                pos.getX() + RANGE, pos.getY() + 2, pos.getZ() + RANGE)) {
            if (!level.isInWorldBounds(candidate) || !level.isEmptyBlock(candidate)) continue;
            BlockState below = level.getBlockState(candidate.below());
            if (below.is(BlockTags.DIRT) && !level.dimensionType().ultraWarm()) {
                valid.add(candidate.immutable());
            }
        }

        int count = level.random.nextIntBetweenInclusive(5, 7);
        while (count > 0 && !valid.isEmpty()) {
            count--;
            BlockPos target = valid.remove(level.random.nextInt(valid.size()));
            Optional<net.minecraft.core.Holder<Block>> toPlace = flowersTag.get().getRandomElement(level.random);
            if (toPlace.isPresent()) {
                BlockState flowerState = toPlace.get().value().defaultBlockState();
                if (flowerState.canSurvive(level, target)) {
                    level.setBlockAndUpdate(target, flowerState);
                }
            }
        }

        ctx.getItemInHand().shrink(1);
        return InteractionResult.CONSUME;
    }
}
