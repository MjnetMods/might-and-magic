package org.mjli.mam;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MamCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MightAndMagic.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VERDANT_PATH_TAB =
            TABS.register("verdant_path", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mam.verdant_path"))
                    .icon(() -> MamItems.PINK_FLOWER.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        // Guidebook
                        output.accept(MamItems.VERDANT_PATH_GUIDE.get());
                        // Petals
                        output.accept(MamItems.WHITE_PETAL.get());
                        output.accept(MamItems.ORANGE_PETAL.get());
                        output.accept(MamItems.MAGENTA_PETAL.get());
                        output.accept(MamItems.LIGHT_BLUE_PETAL.get());
                        output.accept(MamItems.YELLOW_PETAL.get());
                        output.accept(MamItems.LIME_PETAL.get());
                        output.accept(MamItems.PINK_PETAL.get());
                        output.accept(MamItems.GRAY_PETAL.get());
                        output.accept(MamItems.LIGHT_GRAY_PETAL.get());
                        output.accept(MamItems.CYAN_PETAL.get());
                        output.accept(MamItems.PURPLE_PETAL.get());
                        output.accept(MamItems.BLUE_PETAL.get());
                        output.accept(MamItems.BROWN_PETAL.get());
                        output.accept(MamItems.GREEN_PETAL.get());
                        output.accept(MamItems.RED_PETAL.get());
                        output.accept(MamItems.BLACK_PETAL.get());
                        // Flowers
                        output.accept(MamItems.WHITE_FLOWER.get());
                        output.accept(MamItems.ORANGE_FLOWER.get());
                        output.accept(MamItems.MAGENTA_FLOWER.get());
                        output.accept(MamItems.LIGHT_BLUE_FLOWER.get());
                        output.accept(MamItems.YELLOW_FLOWER.get());
                        output.accept(MamItems.LIME_FLOWER.get());
                        output.accept(MamItems.PINK_FLOWER.get());
                        output.accept(MamItems.GRAY_FLOWER.get());
                        output.accept(MamItems.LIGHT_GRAY_FLOWER.get());
                        output.accept(MamItems.CYAN_FLOWER.get());
                        output.accept(MamItems.PURPLE_FLOWER.get());
                        output.accept(MamItems.BLUE_FLOWER.get());
                        output.accept(MamItems.BROWN_FLOWER.get());
                        output.accept(MamItems.GREEN_FLOWER.get());
                        output.accept(MamItems.RED_FLOWER.get());
                        output.accept(MamItems.BLACK_FLOWER.get());
                        // Special flowers
                        output.accept(MamItems.PURE_DAISY.get());
                        // Mushrooms
                        output.accept(MamItems.WHITE_MUSHROOM.get());
                        output.accept(MamItems.ORANGE_MUSHROOM.get());
                        output.accept(MamItems.MAGENTA_MUSHROOM.get());
                        output.accept(MamItems.LIGHT_BLUE_MUSHROOM.get());
                        output.accept(MamItems.YELLOW_MUSHROOM.get());
                        output.accept(MamItems.LIME_MUSHROOM.get());
                        output.accept(MamItems.PINK_MUSHROOM.get());
                        output.accept(MamItems.GRAY_MUSHROOM.get());
                        output.accept(MamItems.LIGHT_GRAY_MUSHROOM.get());
                        output.accept(MamItems.CYAN_MUSHROOM.get());
                        output.accept(MamItems.PURPLE_MUSHROOM.get());
                        output.accept(MamItems.BLUE_MUSHROOM.get());
                        output.accept(MamItems.BROWN_MUSHROOM.get());
                        output.accept(MamItems.GREEN_MUSHROOM.get());
                        output.accept(MamItems.RED_MUSHROOM.get());
                        output.accept(MamItems.BLACK_MUSHROOM.get());
                        // Living rock
                        output.accept(MamItems.LIVING_ROCK.get());
                        output.accept(MamItems.LIVING_ROCK_POLISHED.get());
                        output.accept(MamItems.LIVING_ROCK_BRICK.get());
                        // Living wood
                        output.accept(MamItems.LIVINGWOOD_LOG.get());
                        output.accept(MamItems.LIVINGWOOD_LOG_STRIPPED.get());
                        output.accept(MamItems.LIVINGWOOD_LOG_GLIMMERING.get());
                        output.accept(MamItems.LIVINGWOOD_LOG_STRIPPED_GLIMMERING.get());
                        output.accept(MamItems.LIVINGWOOD.get());
                        output.accept(MamItems.LIVINGWOOD_STRIPPED.get());
                        output.accept(MamItems.LIVINGWOOD_PLANKS.get());
                        output.accept(MamItems.LIVINGWOOD_PLANKS_MOSSY.get());
                        // Mana
                        output.accept(MamItems.MANA_POOL.get());
                        output.accept(MamItems.PETAL_APOTHECARY.get());
                    })
                    .build());

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
