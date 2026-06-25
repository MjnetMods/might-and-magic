package org.mjli.mam;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantMana;
import org.mjli.mam.verdant.VerdantRock;
import org.mjli.mam.verdant.VerdantWood;

public class MamCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MightAndMagic.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VERDANT_PATH_TAB =
            TABS.register("verdant_path", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mam.verdant_path"))
                    .icon(() -> VerdantFlowers.FLOWERS.get(DyeColor.PINK).asStack())
                    .displayItems((params, output) -> {
                        VerdantFlowers.appendToTab(output);
                        VerdantRock.appendToTab(output);
                        VerdantWood.appendToTab(output);
                        VerdantMana.appendToTab(output);
                    })
                    .build());

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
