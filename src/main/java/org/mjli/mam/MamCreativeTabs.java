package org.mjli.mam;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mjli.mam.verdant.VerdantFlowers;

public class MamCreativeTabs {

    // Defined first so domain classes can reference it during their static init
    public static final ResourceKey<CreativeModeTab> VERDANT_PATH_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, MightAndMagic.modLoc("verdant_path"));

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MightAndMagic.MODID);

    // Registrate populates this tab via .tab(VERDANT_PATH_KEY) on each builder — no displayItems needed
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VERDANT_PATH_TAB =
            TABS.register("verdant_path", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mam.verdant_path"))
                    .icon(() -> VerdantFlowers.FLOWERS.get(DyeColor.PINK).asStack())
                    .build());

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
