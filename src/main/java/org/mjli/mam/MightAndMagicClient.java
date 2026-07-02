package org.mjli.mam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
import org.mjli.mam.block.SpreaderBlock;
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
        // Loop Marking's rune-glyph decal has real alpha transparency (design/magic/16_mana-spreader.md).
        event.enqueueWork(() -> {
            for (Block spreader : spreaderBlocks()) {
                ItemBlockRenderTypes.setRenderLayer(spreader, RenderType.cutout());
            }
        });
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
            VerdantMana.INFUSED_MANA_POOL.get(), VerdantMana.INFUSED_APOTHECARY.get(), VerdantMana.INFUSED_ALTAR.get()
        };
    }

    private static Block[] sacredTintedBlocks() {
        return new Block[] {
            VerdantRock.SACRED_LIVING_ROCK.get(), VerdantRock.SACRED_LIVING_ROCK_POLISHED.get(), VerdantRock.SACRED_LIVING_ROCK_BRICK.get(),
            VerdantWood.SACRED_LIVINGWOOD_LOG.get(), VerdantWood.SACRED_LIVINGWOOD.get(), VerdantWood.SACRED_LIVINGWOOD_PLANKS.get(),
            VerdantMana.SACRED_MANA_POOL.get(), VerdantMana.SACRED_APOTHECARY.get(), VerdantMana.SACRED_ALTAR.get()
        };
    }

    private static Block[] desecratedTintedBlocks() {
        return new Block[] {
            VerdantRock.DESECRATED_LIVING_ROCK.get(), VerdantRock.DESECRATED_LIVING_ROCK_POLISHED.get(), VerdantRock.DESECRATED_LIVING_ROCK_BRICK.get(),
            VerdantWood.DESECRATED_LIVINGWOOD_LOG.get(), VerdantWood.DESECRATED_LIVINGWOOD.get(), VerdantWood.DESECRATED_LIVINGWOOD_PLANKS.get(),
            VerdantMana.DESECRATED_MANA_POOL.get(), VerdantMana.DESECRATED_APOTHECARY.get(), VerdantMana.DESECRATED_ALTAR.get()
        };
    }

    private static Block[] neutralTintedBlocks() {
        return new Block[] { VerdantMana.APOTHECARY.get() };
    }

    // Spreader carries two independent tints on the same block (design/magic/16_mana-spreader.md
    // § Loop Marking) — tintindex 0 is the fixed tier color (same as every other tiered block),
    // tintindex 1 is the mark color read off the MARK blockstate property. NeoForge only lets one
    // handler own a given block, so unlike the groups above this can't reuse a flat constant lambda.
    private static Block[] spreaderBlocks() {
        return new Block[] {
            VerdantMana.SPREADER.get(), VerdantMana.INFUSED_SPREADER.get(),
            VerdantMana.SACRED_SPREADER.get(), VerdantMana.DESECRATED_SPREADER.get()
        };
    }

    private static int spreaderTierTint(Block block) {
        if (block == VerdantMana.INFUSED_SPREADER.get()) return TINT_INFUSED;
        if (block == VerdantMana.SACRED_SPREADER.get()) return TINT_SACRED;
        if (block == VerdantMana.DESECRATED_SPREADER.get()) return TINT_DESECRATED;
        return TINT_NEUTRAL;
    }

    private static int spreaderBlockTint(BlockState state, int tintIndex) {
        if (tintIndex == 1) return state.getValue(SpreaderBlock.MARK).getTint();
        return spreaderTierTint(state.getBlock());
    }

    @SubscribeEvent
    static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tint) -> TINT_INFUSED, infusedTintedBlocks());
        event.register((state, level, pos, tint) -> TINT_SACRED, sacredTintedBlocks());
        event.register((state, level, pos, tint) -> TINT_DESECRATED, desecratedTintedBlocks());
        event.register((state, level, pos, tint) -> TINT_NEUTRAL, neutralTintedBlocks());
        event.register((state, level, pos, tint) -> spreaderBlockTint(state, tint), spreaderBlocks());
    }

    @SubscribeEvent
    static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> TINT_INFUSED, infusedTintedBlocks());
        event.register((stack, tint) -> TINT_SACRED, sacredTintedBlocks());
        event.register((stack, tint) -> TINT_DESECRATED, desecratedTintedBlocks());
        event.register((stack, tint) -> TINT_NEUTRAL, neutralTintedBlocks());
        // Item form is always the unmarked model (see VerdantMana's spreader .item() registration),
        // so tintindex 1 is never actually sampled here — only the tier constant matters.
        event.register((stack, tint) -> spreaderTierTint(Block.byItem(stack.getItem())), spreaderBlocks());
    }
}
