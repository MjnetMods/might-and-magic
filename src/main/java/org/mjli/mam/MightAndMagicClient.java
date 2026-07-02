package org.mjli.mam;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.mjli.mam.client.render.ApothecaryBlockEntityRenderer;
import org.mjli.mam.verdant.VerdantMana;
import org.mjli.mam.verdant.VerdantRock;
import org.mjli.mam.verdant.VerdantWood;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = MightAndMagic.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = MightAndMagic.MODID, value = Dist.CLIENT)
public class MightAndMagicClient {
    public MightAndMagicClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        MightAndMagic.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        if (ModList.get().isLoaded("create")) {
            initPonder();
        }
    }

    private static void initPonder() {
        net.createmod.ponder.foundation.PonderIndex.addPlugin(new org.mjli.mam.ponder.MamPonderPlugin());
    }

    @SubscribeEvent
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MamBlockEntities.APOTHECARY.get(), ApothecaryBlockEntityRenderer::new);
    }

    // Tier tinting: Infused/Sacred/Desecrated share one desaturated texture per material,
    // tinted blue/green/purple at render time. See todo/09_sacred-desecrated-tier-tinting.md.
    private static final int TINT_INFUSED = 0x4A90E2;
    private static final int TINT_SACRED = 0x4CAF50;
    private static final int TINT_DESECRATED = 0x8E44AD;
    private static final int TINT_NEUTRAL = 0xFFFFFF;

    // Evaluated lazily (at event-fire time, well after Registrate's deferred blocks are
    // registered) rather than as eager static fields, which would resolve .get() too early.
    private static Block[] infusedTintedBlocks() {
        return new Block[] {
            VerdantRock.INFUSED_LIVING_ROCK.get(), VerdantRock.INFUSED_LIVING_ROCK_POLISHED.get(), VerdantRock.INFUSED_LIVING_ROCK_BRICK.get(),
            VerdantWood.INFUSED_LIVINGWOOD_LOG.get(), VerdantWood.INFUSED_LIVINGWOOD.get(), VerdantWood.INFUSED_LIVINGWOOD_PLANKS.get(),
            VerdantMana.INFUSED_MANA_POOL.get(), VerdantMana.INFUSED_APOTHECARY.get(), VerdantMana.INFUSED_ALTAR.get(),
            VerdantMana.INFUSED_SPREADER.get()
        };
    }

    private static Block[] sacredTintedBlocks() {
        return new Block[] {
            VerdantRock.SACRED_LIVING_ROCK.get(), VerdantRock.SACRED_LIVING_ROCK_POLISHED.get(), VerdantRock.SACRED_LIVING_ROCK_BRICK.get(),
            VerdantWood.SACRED_LIVINGWOOD_LOG.get(), VerdantWood.SACRED_LIVINGWOOD.get(), VerdantWood.SACRED_LIVINGWOOD_PLANKS.get(),
            VerdantMana.SACRED_MANA_POOL.get(), VerdantMana.SACRED_APOTHECARY.get(), VerdantMana.SACRED_ALTAR.get(),
            VerdantMana.SACRED_SPREADER.get()
        };
    }

    private static Block[] desecratedTintedBlocks() {
        return new Block[] {
            VerdantRock.DESECRATED_LIVING_ROCK.get(), VerdantRock.DESECRATED_LIVING_ROCK_POLISHED.get(), VerdantRock.DESECRATED_LIVING_ROCK_BRICK.get(),
            VerdantWood.DESECRATED_LIVINGWOOD_LOG.get(), VerdantWood.DESECRATED_LIVINGWOOD.get(), VerdantWood.DESECRATED_LIVINGWOOD_PLANKS.get(),
            VerdantMana.DESECRATED_MANA_POOL.get(), VerdantMana.DESECRATED_APOTHECARY.get(), VerdantMana.DESECRATED_ALTAR.get(),
            VerdantMana.DESECRATED_SPREADER.get()
        };
    }

    private static Block[] neutralTintedBlocks() {
        return new Block[] { VerdantMana.APOTHECARY.get() };
    }

    @SubscribeEvent
    static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tint) -> TINT_INFUSED, infusedTintedBlocks());
        event.register((state, level, pos, tint) -> TINT_SACRED, sacredTintedBlocks());
        event.register((state, level, pos, tint) -> TINT_DESECRATED, desecratedTintedBlocks());
        event.register((state, level, pos, tint) -> TINT_NEUTRAL, neutralTintedBlocks());
    }

    @SubscribeEvent
    static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> TINT_INFUSED, infusedTintedBlocks());
        event.register((stack, tint) -> TINT_SACRED, sacredTintedBlocks());
        event.register((stack, tint) -> TINT_DESECRATED, desecratedTintedBlocks());
        event.register((stack, tint) -> TINT_NEUTRAL, neutralTintedBlocks());
    }
}
