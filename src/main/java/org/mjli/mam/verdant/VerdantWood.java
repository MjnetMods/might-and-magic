package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;

public class VerdantWood {

    private static final MamRegistrate R = MightAndMagic.registrate();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_LOG =
        R.block("livingwood_log", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog())
         .blockstate((ctx, p) -> p.axisBlock((RotatedPillarBlock) ctx.get(),
             p.modLoc("block/livingwood_log"),
             p.modLoc("block/livingwood_log_top")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_LOG_STRIPPED =
        R.block("livingwood_log_stripped", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog())
         .blockstate((ctx, p) -> p.axisBlock((RotatedPillarBlock) ctx.get(),
             p.modLoc("block/livingwood_log_stripped"),
             p.modLoc("block/livingwood_log_stripped_top")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_LOG_GLIMMERING =
        R.block("livingwood_log_glimmering", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog().lightLevel(s -> 12))
         .blockstate((ctx, p) -> p.axisBlock((RotatedPillarBlock) ctx.get(),
             p.modLoc("block/livingwood_log_glimmering"),
             p.modLoc("block/livingwood_log_top")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_LOG_STRIPPED_GLIMMERING =
        R.block("livingwood_log_stripped_glimmering", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog().lightLevel(s -> 8))
         .blockstate((ctx, p) -> p.axisBlock((RotatedPillarBlock) ctx.get(),
             p.modLoc("block/livingwood_log_stripped_glimmering"),
             p.modLoc("block/livingwood_log_stripped_top")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD =
        R.block("livingwood", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog())
         .blockstate((ctx, p) -> p.axisBlock((RotatedPillarBlock) ctx.get(),
             p.modLoc("block/livingwood_log"),
             p.modLoc("block/livingwood_log")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_STRIPPED =
        R.block("livingwood_stripped", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog())
         .blockstate((ctx, p) -> p.axisBlock((RotatedPillarBlock) ctx.get(),
             p.modLoc("block/livingwood_log_stripped"),
             p.modLoc("block/livingwood_log_stripped")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> LIVINGWOOD_PLANKS =
        R.block("livingwood_planks", Block::new)
         .properties(p -> MamBlockProperties.livingWood())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.cubeAll(ctx.get())))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> LIVINGWOOD_PLANKS_MOSSY =
        R.block("livingwood_planks_mossy", Block::new)
         .properties(p -> MamBlockProperties.livingWood())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.cubeAll(ctx.get())))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<StairBlock> LIVINGWOOD_PLANKS_STAIRS =
        R.block("livingwood_planks_stairs", p -> new StairBlock(LIVINGWOOD_PLANKS.get().defaultBlockState(), p))
         .properties(p -> MamBlockProperties.livingWood())
         .blockstate((ctx, p) -> p.stairsBlock(ctx.get(), p.modLoc("block/livingwood_planks")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<SlabBlock> LIVINGWOOD_PLANKS_SLAB =
        R.block("livingwood_planks_slab", SlabBlock::new)
         .properties(p -> MamBlockProperties.livingWood())
         .blockstate((ctx, p) -> p.slabBlock(ctx.get(), p.modLoc("block/livingwood_planks"), p.modLoc("block/livingwood_planks")))
         .loot((t, b) -> t.add(b, t.createSlabItemTable(b)))
         .simpleItem()
         .register();

    public static final BlockEntry<FenceBlock> LIVINGWOOD_PLANKS_FENCE =
        R.block("livingwood_planks_fence", FenceBlock::new)
         .properties(p -> MamBlockProperties.livingWood())
         .blockstate((ctx, p) -> {
             var tex = p.modLoc("block/livingwood_planks");
             p.fenceBlock(ctx.get(), tex);
             p.models().getBuilder(ctx.getName() + "_inventory")
                 .parent(new ModelFile.UncheckedModelFile("minecraft:block/fence_inventory"))
                 .texture("texture", tex.toString());
         })
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.blockWithInventoryModel(ctx::getEntry)).build()
         .register();

    public static final BlockEntry<FenceGateBlock> LIVINGWOOD_PLANKS_FENCE_GATE =
        R.block("livingwood_planks_fence_gate", p -> new FenceGateBlock(WoodType.OAK, p))
         .properties(p -> MamBlockProperties.livingWood())
         .blockstate((ctx, p) -> p.fenceGateBlock(ctx.get(), p.modLoc("block/livingwood_planks")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(LIVINGWOOD_LOG.asStack(), tab);
        modifier.accept(LIVINGWOOD_LOG_STRIPPED.asStack(), tab);
        modifier.accept(LIVINGWOOD_LOG_GLIMMERING.asStack(), tab);
        modifier.accept(LIVINGWOOD_LOG_STRIPPED_GLIMMERING.asStack(), tab);
        modifier.accept(LIVINGWOOD.asStack(), tab);
        modifier.accept(LIVINGWOOD_STRIPPED.asStack(), tab);
        modifier.accept(LIVINGWOOD_PLANKS.asStack(), tab);
        modifier.accept(LIVINGWOOD_PLANKS_MOSSY.asStack(), tab);
        modifier.accept(LIVINGWOOD_PLANKS_STAIRS.asStack(), tab);
        modifier.accept(LIVINGWOOD_PLANKS_SLAB.asStack(), tab);
        modifier.accept(LIVINGWOOD_PLANKS_FENCE.asStack(), tab);
        modifier.accept(LIVINGWOOD_PLANKS_FENCE_GATE.asStack(), tab);
    }

    public static void init() {}
}
