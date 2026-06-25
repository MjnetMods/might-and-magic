package org.mjli.mam.foundation.registration;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.UnaryOperator;

public class MamBlockProperties {

    public static BlockBehaviour.Properties flower() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .instabreak()
                .offsetType(BlockBehaviour.OffsetType.XZ)
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.GRASS);
    }

    public static BlockBehaviour.Properties mushroom() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .instabreak()
                .lightLevel(s -> 3)
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.GRASS);
    }

    public static BlockBehaviour.Properties livingRock() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(2.0f, 10.0f)
                .sound(SoundType.STONE);
    }

    public static BlockBehaviour.Properties livingWoodLog() {
        return BlockBehaviour.Properties.of()
                .mapColor(state -> state.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y
                        ? MapColor.TERRACOTTA_RED : MapColor.TERRACOTTA_BROWN)
                .strength(2.0f)
                .sound(SoundType.WOOD);
    }

    public static BlockBehaviour.Properties livingWood() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.TERRACOTTA_RED)
                .strength(2.0f)
                .sound(SoundType.WOOD);
    }

    public static BlockBehaviour.Properties manaPool() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.0f, 10.0f)
                .sound(SoundType.STONE);
    }
}
