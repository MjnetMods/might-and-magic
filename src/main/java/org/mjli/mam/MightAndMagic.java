package org.mjli.mam;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.mjli.mam.foundation.registration.MamRegistrate;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantMana;
import org.mjli.mam.verdant.VerdantRock;
import org.mjli.mam.verdant.VerdantWood;
import org.slf4j.Logger;

@Mod(MightAndMagic.MODID)
public class MightAndMagic {
    public static final String MODID = "mam";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final MamRegistrate REGISTRATE = MamRegistrate.create(MODID);

    public static MamRegistrate registrate() {
        return REGISTRATE;
    }

    public MightAndMagic(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        VerdantFlowers.init();
        VerdantRock.init();
        VerdantWood.init();
        VerdantMana.init();

        // One consumer, fires once per tab build — avoids Registrate's per-item double-add
        REGISTRATE.modifyCreativeModeTab(MamCreativeTabs.VERDANT_PATH_KEY, modifier -> {
            VerdantFlowers.appendToTab(modifier);
            VerdantRock.appendToTab(modifier);
            VerdantWood.appendToTab(modifier);
            VerdantMana.appendToTab(modifier);
        });

        MamBlockEntities.register(modEventBus);
        MamRecipes.register(modEventBus);
        MamCreativeTabs.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
