package org.mjli.mam;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(MightAndMagic.MODID)
public class MightAndMagic {
    public static final String MODID = "mam";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MightAndMagic(IEventBus modEventBus, ModContainer modContainer) {
        MamBlocks.register(modEventBus);
        MamItems.register(modEventBus);
        MamBlockEntities.register(modEventBus);
        MamRecipes.register(modEventBus);
        MamCreativeTabs.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
