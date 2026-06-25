package org.mjli.mam;

import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mjli.mam.block.MysticalMushroomBlock;
import org.mjli.mam.block.PetalApothecaryBlock;
import org.mjli.mam.block.flower.MysticalFlowerBlock;
import org.mjli.mam.block.flower.PureDaisyBlock;
import org.mjli.mam.block.mana.ManaPoolBlock;

public class MamBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MightAndMagic.MODID);

    // --- Mystical Flowers ---
    private static BlockBehaviour.Properties flowerProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .instabreak()
                .offsetType(BlockBehaviour.OffsetType.XZ)
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.GRASS);
    }

    public static final DeferredBlock<MysticalFlowerBlock> WHITE_FLOWER =
            BLOCKS.register("white_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.WHITE, MobEffects.MOVEMENT_SPEED, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> ORANGE_FLOWER =
            BLOCKS.register("orange_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.ORANGE, MobEffects.FIRE_RESISTANCE, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> MAGENTA_FLOWER =
            BLOCKS.register("magenta_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.MAGENTA, MobEffects.DIG_SLOWDOWN, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> LIGHT_BLUE_FLOWER =
            BLOCKS.register("light_blue_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.LIGHT_BLUE, MobEffects.JUMP, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> YELLOW_FLOWER =
            BLOCKS.register("yellow_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.YELLOW, MobEffects.ABSORPTION, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> LIME_FLOWER =
            BLOCKS.register("lime_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.LIME, MobEffects.POISON, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> PINK_FLOWER =
            BLOCKS.register("pink_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.PINK, MobEffects.REGENERATION, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> GRAY_FLOWER =
            BLOCKS.register("gray_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.GRAY, MobEffects.DAMAGE_RESISTANCE, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> LIGHT_GRAY_FLOWER =
            BLOCKS.register("light_gray_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.LIGHT_GRAY, MobEffects.WEAKNESS, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> CYAN_FLOWER =
            BLOCKS.register("cyan_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.CYAN, MobEffects.WATER_BREATHING, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> PURPLE_FLOWER =
            BLOCKS.register("purple_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.PURPLE, MobEffects.CONFUSION, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> BLUE_FLOWER =
            BLOCKS.register("blue_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.BLUE, MobEffects.NIGHT_VISION, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> BROWN_FLOWER =
            BLOCKS.register("brown_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.BROWN, MobEffects.WITHER, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> GREEN_FLOWER =
            BLOCKS.register("green_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.GREEN, MobEffects.HUNGER, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> RED_FLOWER =
            BLOCKS.register("red_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.RED, MobEffects.DAMAGE_BOOST, 240, flowerProps()));
    public static final DeferredBlock<MysticalFlowerBlock> BLACK_FLOWER =
            BLOCKS.register("black_mystical_flower", () -> new MysticalFlowerBlock(DyeColor.BLACK, MobEffects.BLINDNESS, 240, flowerProps()));

    // --- Living Rock ---
    private static BlockBehaviour.Properties livingRockProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(2.0f, 10.0f)
                .sound(SoundType.STONE);
    }

    public static final DeferredBlock<Block> LIVING_ROCK =
            BLOCKS.register("living_rock", () -> new Block(livingRockProps()));
    public static final DeferredBlock<Block> LIVING_ROCK_POLISHED =
            BLOCKS.register("living_rock_polished", () -> new Block(livingRockProps()));
    public static final DeferredBlock<Block> LIVING_ROCK_BRICK =
            BLOCKS.register("living_rock_brick", () -> new Block(livingRockProps()));

    // --- Mystical Mushrooms ---
    private static BlockBehaviour.Properties mushroomProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .instabreak()
                .lightLevel(s -> 3)
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.GRASS);
    }

    public static final DeferredBlock<MysticalMushroomBlock> WHITE_MUSHROOM =
            BLOCKS.register("white_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.WHITE, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> ORANGE_MUSHROOM =
            BLOCKS.register("orange_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.ORANGE, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> MAGENTA_MUSHROOM =
            BLOCKS.register("magenta_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.MAGENTA, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> LIGHT_BLUE_MUSHROOM =
            BLOCKS.register("light_blue_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.LIGHT_BLUE, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> YELLOW_MUSHROOM =
            BLOCKS.register("yellow_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.YELLOW, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> LIME_MUSHROOM =
            BLOCKS.register("lime_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.LIME, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> PINK_MUSHROOM =
            BLOCKS.register("pink_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.PINK, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> GRAY_MUSHROOM =
            BLOCKS.register("gray_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.GRAY, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> LIGHT_GRAY_MUSHROOM =
            BLOCKS.register("light_gray_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.LIGHT_GRAY, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> CYAN_MUSHROOM =
            BLOCKS.register("cyan_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.CYAN, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> PURPLE_MUSHROOM =
            BLOCKS.register("purple_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.PURPLE, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> BLUE_MUSHROOM =
            BLOCKS.register("blue_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.BLUE, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> BROWN_MUSHROOM =
            BLOCKS.register("brown_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.BROWN, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> GREEN_MUSHROOM =
            BLOCKS.register("green_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.GREEN, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> RED_MUSHROOM =
            BLOCKS.register("red_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.RED, mushroomProps()));
    public static final DeferredBlock<MysticalMushroomBlock> BLACK_MUSHROOM =
            BLOCKS.register("black_mystical_mushroom", () -> new MysticalMushroomBlock(DyeColor.BLACK, mushroomProps()));

    // --- Special Flowers ---
    public static final DeferredBlock<PureDaisyBlock> PURE_DAISY =
            BLOCKS.register("pure_daisy", () -> new PureDaisyBlock(flowerProps()));

    // --- Mana ---
    public static final DeferredBlock<ManaPoolBlock> MANA_POOL =
            BLOCKS.register("mana_pool", () -> new ManaPoolBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(3.0f, 10.0f)
                            .sound(SoundType.STONE)));

    // --- Living Wood ---
    private static BlockBehaviour.Properties livingWoodProps() {
        return BlockBehaviour.Properties.of()
                .mapColor(state -> state.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y
                        ? MapColor.TERRACOTTA_RED : MapColor.TERRACOTTA_BROWN)
                .strength(2.0f)
                .sound(SoundType.WOOD);
    }

    public static final DeferredBlock<RotatedPillarBlock> LIVINGWOOD_LOG =
            BLOCKS.register("livingwood_log", () -> new RotatedPillarBlock(livingWoodProps()));
    public static final DeferredBlock<RotatedPillarBlock> LIVINGWOOD_LOG_STRIPPED =
            BLOCKS.register("livingwood_log_stripped", () -> new RotatedPillarBlock(livingWoodProps()));
    public static final DeferredBlock<RotatedPillarBlock> LIVINGWOOD_LOG_GLIMMERING =
            BLOCKS.register("livingwood_log_glimmering", () -> new RotatedPillarBlock(livingWoodProps().lightLevel(s -> 12)));
    public static final DeferredBlock<RotatedPillarBlock> LIVINGWOOD_LOG_STRIPPED_GLIMMERING =
            BLOCKS.register("livingwood_log_stripped_glimmering", () -> new RotatedPillarBlock(livingWoodProps().lightLevel(s -> 8)));
    public static final DeferredBlock<RotatedPillarBlock> LIVINGWOOD =
            BLOCKS.register("livingwood", () -> new RotatedPillarBlock(livingWoodProps()));
    public static final DeferredBlock<RotatedPillarBlock> LIVINGWOOD_STRIPPED =
            BLOCKS.register("livingwood_stripped", () -> new RotatedPillarBlock(livingWoodProps()));
    public static final DeferredBlock<Block> LIVINGWOOD_PLANKS =
            BLOCKS.register("livingwood_planks", () -> new Block(livingWoodProps().mapColor(MapColor.TERRACOTTA_RED)));
    public static final DeferredBlock<Block> LIVINGWOOD_PLANKS_MOSSY =
            BLOCKS.register("livingwood_planks_mossy", () -> new Block(livingWoodProps().mapColor(MapColor.TERRACOTTA_RED)));

    // --- Petal Apothecary ---
    public static final DeferredBlock<PetalApothecaryBlock> PETAL_APOTHECARY =
            BLOCKS.register("petal_apothecary", () -> new PetalApothecaryBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(3.0f, 10.0f)
                            .sound(SoundType.STONE)));

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
