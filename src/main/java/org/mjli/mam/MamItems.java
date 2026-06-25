package org.mjli.mam;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MamItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MightAndMagic.MODID);

    // Mystical flower items
    public static final DeferredItem<BlockItem> WHITE_FLOWER = registerBlockItem("white_mystical_flower", MamBlocks.WHITE_FLOWER);
    public static final DeferredItem<BlockItem> ORANGE_FLOWER = registerBlockItem("orange_mystical_flower", MamBlocks.ORANGE_FLOWER);
    public static final DeferredItem<BlockItem> MAGENTA_FLOWER = registerBlockItem("magenta_mystical_flower", MamBlocks.MAGENTA_FLOWER);
    public static final DeferredItem<BlockItem> LIGHT_BLUE_FLOWER = registerBlockItem("light_blue_mystical_flower", MamBlocks.LIGHT_BLUE_FLOWER);
    public static final DeferredItem<BlockItem> YELLOW_FLOWER = registerBlockItem("yellow_mystical_flower", MamBlocks.YELLOW_FLOWER);
    public static final DeferredItem<BlockItem> LIME_FLOWER = registerBlockItem("lime_mystical_flower", MamBlocks.LIME_FLOWER);
    public static final DeferredItem<BlockItem> PINK_FLOWER = registerBlockItem("pink_mystical_flower", MamBlocks.PINK_FLOWER);
    public static final DeferredItem<BlockItem> GRAY_FLOWER = registerBlockItem("gray_mystical_flower", MamBlocks.GRAY_FLOWER);
    public static final DeferredItem<BlockItem> LIGHT_GRAY_FLOWER = registerBlockItem("light_gray_mystical_flower", MamBlocks.LIGHT_GRAY_FLOWER);
    public static final DeferredItem<BlockItem> CYAN_FLOWER = registerBlockItem("cyan_mystical_flower", MamBlocks.CYAN_FLOWER);
    public static final DeferredItem<BlockItem> PURPLE_FLOWER = registerBlockItem("purple_mystical_flower", MamBlocks.PURPLE_FLOWER);
    public static final DeferredItem<BlockItem> BLUE_FLOWER = registerBlockItem("blue_mystical_flower", MamBlocks.BLUE_FLOWER);
    public static final DeferredItem<BlockItem> BROWN_FLOWER = registerBlockItem("brown_mystical_flower", MamBlocks.BROWN_FLOWER);
    public static final DeferredItem<BlockItem> GREEN_FLOWER = registerBlockItem("green_mystical_flower", MamBlocks.GREEN_FLOWER);
    public static final DeferredItem<BlockItem> RED_FLOWER = registerBlockItem("red_mystical_flower", MamBlocks.RED_FLOWER);
    public static final DeferredItem<BlockItem> BLACK_FLOWER = registerBlockItem("black_mystical_flower", MamBlocks.BLACK_FLOWER);

    // Mystical mushroom items
    public static final DeferredItem<BlockItem> WHITE_MUSHROOM = registerBlockItem("white_mystical_mushroom", MamBlocks.WHITE_MUSHROOM);
    public static final DeferredItem<BlockItem> ORANGE_MUSHROOM = registerBlockItem("orange_mystical_mushroom", MamBlocks.ORANGE_MUSHROOM);
    public static final DeferredItem<BlockItem> MAGENTA_MUSHROOM = registerBlockItem("magenta_mystical_mushroom", MamBlocks.MAGENTA_MUSHROOM);
    public static final DeferredItem<BlockItem> LIGHT_BLUE_MUSHROOM = registerBlockItem("light_blue_mystical_mushroom", MamBlocks.LIGHT_BLUE_MUSHROOM);
    public static final DeferredItem<BlockItem> YELLOW_MUSHROOM = registerBlockItem("yellow_mystical_mushroom", MamBlocks.YELLOW_MUSHROOM);
    public static final DeferredItem<BlockItem> LIME_MUSHROOM = registerBlockItem("lime_mystical_mushroom", MamBlocks.LIME_MUSHROOM);
    public static final DeferredItem<BlockItem> PINK_MUSHROOM = registerBlockItem("pink_mystical_mushroom", MamBlocks.PINK_MUSHROOM);
    public static final DeferredItem<BlockItem> GRAY_MUSHROOM = registerBlockItem("gray_mystical_mushroom", MamBlocks.GRAY_MUSHROOM);
    public static final DeferredItem<BlockItem> LIGHT_GRAY_MUSHROOM = registerBlockItem("light_gray_mystical_mushroom", MamBlocks.LIGHT_GRAY_MUSHROOM);
    public static final DeferredItem<BlockItem> CYAN_MUSHROOM = registerBlockItem("cyan_mystical_mushroom", MamBlocks.CYAN_MUSHROOM);
    public static final DeferredItem<BlockItem> PURPLE_MUSHROOM = registerBlockItem("purple_mystical_mushroom", MamBlocks.PURPLE_MUSHROOM);
    public static final DeferredItem<BlockItem> BLUE_MUSHROOM = registerBlockItem("blue_mystical_mushroom", MamBlocks.BLUE_MUSHROOM);
    public static final DeferredItem<BlockItem> BROWN_MUSHROOM = registerBlockItem("brown_mystical_mushroom", MamBlocks.BROWN_MUSHROOM);
    public static final DeferredItem<BlockItem> GREEN_MUSHROOM = registerBlockItem("green_mystical_mushroom", MamBlocks.GREEN_MUSHROOM);
    public static final DeferredItem<BlockItem> RED_MUSHROOM = registerBlockItem("red_mystical_mushroom", MamBlocks.RED_MUSHROOM);
    public static final DeferredItem<BlockItem> BLACK_MUSHROOM = registerBlockItem("black_mystical_mushroom", MamBlocks.BLACK_MUSHROOM);

    // Living rock items
    public static final DeferredItem<BlockItem> LIVING_ROCK = registerBlockItem("living_rock", MamBlocks.LIVING_ROCK);
    public static final DeferredItem<BlockItem> LIVING_ROCK_POLISHED = registerBlockItem("living_rock_polished", MamBlocks.LIVING_ROCK_POLISHED);
    public static final DeferredItem<BlockItem> LIVING_ROCK_BRICK = registerBlockItem("living_rock_brick", MamBlocks.LIVING_ROCK_BRICK);

    // Special flower items
    public static final DeferredItem<BlockItem> PURE_DAISY = registerBlockItem("pure_daisy", MamBlocks.PURE_DAISY);

    // Guidebook
    public static final DeferredItem<Item> VERDANT_PATH_GUIDE =
            ITEMS.register("verdant_path_guide", () -> new org.mjli.mam.item.VerdantPathGuideItem(new Item.Properties().stacksTo(1)));

    // Petal items (16 colors)
    public static final DeferredItem<Item> WHITE_PETAL        = ITEMS.register("white_petal",        () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ORANGE_PETAL       = ITEMS.register("orange_petal",       () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MAGENTA_PETAL      = ITEMS.register("magenta_petal",      () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGHT_BLUE_PETAL   = ITEMS.register("light_blue_petal",   () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> YELLOW_PETAL       = ITEMS.register("yellow_petal",       () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIME_PETAL         = ITEMS.register("lime_petal",         () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PINK_PETAL         = ITEMS.register("pink_petal",         () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GRAY_PETAL         = ITEMS.register("gray_petal",         () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGHT_GRAY_PETAL   = ITEMS.register("light_gray_petal",   () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CYAN_PETAL         = ITEMS.register("cyan_petal",         () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PURPLE_PETAL       = ITEMS.register("purple_petal",       () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLUE_PETAL         = ITEMS.register("blue_petal",         () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BROWN_PETAL        = ITEMS.register("brown_petal",        () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GREEN_PETAL        = ITEMS.register("green_petal",        () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> RED_PETAL          = ITEMS.register("red_petal",          () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLACK_PETAL        = ITEMS.register("black_petal",        () -> new Item(new Item.Properties()));

    // Living wood items
    public static final DeferredItem<BlockItem> LIVINGWOOD_LOG = registerBlockItem("livingwood_log", MamBlocks.LIVINGWOOD_LOG);
    public static final DeferredItem<BlockItem> LIVINGWOOD_LOG_STRIPPED = registerBlockItem("livingwood_log_stripped", MamBlocks.LIVINGWOOD_LOG_STRIPPED);
    public static final DeferredItem<BlockItem> LIVINGWOOD_LOG_GLIMMERING = registerBlockItem("livingwood_log_glimmering", MamBlocks.LIVINGWOOD_LOG_GLIMMERING);
    public static final DeferredItem<BlockItem> LIVINGWOOD_LOG_STRIPPED_GLIMMERING = registerBlockItem("livingwood_log_stripped_glimmering", MamBlocks.LIVINGWOOD_LOG_STRIPPED_GLIMMERING);
    public static final DeferredItem<BlockItem> LIVINGWOOD = registerBlockItem("livingwood", MamBlocks.LIVINGWOOD);
    public static final DeferredItem<BlockItem> LIVINGWOOD_STRIPPED = registerBlockItem("livingwood_stripped", MamBlocks.LIVINGWOOD_STRIPPED);
    public static final DeferredItem<BlockItem> LIVINGWOOD_PLANKS = registerBlockItem("livingwood_planks", MamBlocks.LIVINGWOOD_PLANKS);
    public static final DeferredItem<BlockItem> LIVINGWOOD_PLANKS_MOSSY = registerBlockItem("livingwood_planks_mossy", MamBlocks.LIVINGWOOD_PLANKS_MOSSY);

    // Mana items
    public static final DeferredItem<BlockItem> MANA_POOL = registerBlockItem("mana_pool", MamBlocks.MANA_POOL);
    public static final DeferredItem<BlockItem> PETAL_APOTHECARY = registerBlockItem("petal_apothecary", MamBlocks.PETAL_APOTHECARY);

    private static <T extends net.minecraft.world.level.block.Block> DeferredItem<BlockItem> registerBlockItem(
            String name, net.neoforged.neoforge.registries.DeferredBlock<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
