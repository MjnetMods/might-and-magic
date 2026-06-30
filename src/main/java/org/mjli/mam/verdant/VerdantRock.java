package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;

public class VerdantRock {

    private static final MamRegistrate R = MightAndMagic.registrate();

    public static final BlockEntry<Block> LIVING_ROCK =
        R.block("living_rock", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> {
             var ml  = p.models().getExistingFile(p.modLoc("block/" + ctx.getName()));
             var mlm = p.models().getExistingFile(p.modLoc("block/" + ctx.getName() + "_mirrored"));
             p.getVariantBuilder(ctx.get())
                 .partialState()
                 .addModels(
                     new ConfiguredModel(ml),
                     new ConfiguredModel(mlm),
                     new ConfiguredModel(ml,  0, 180, false),
                     new ConfiguredModel(mlm, 0, 180, false)
                 );
         })
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> LIVING_ROCK_POLISHED =
        R.block("living_rock_polished", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> {
             var ml  = p.models().getExistingFile(p.modLoc("block/" + ctx.getName()));
             var mlm = p.models().getExistingFile(p.modLoc("block/" + ctx.getName() + "_mirrored"));
             p.getVariantBuilder(ctx.get())
                 .partialState()
                 .addModels(
                     new ConfiguredModel(ml),
                     new ConfiguredModel(mlm),
                     new ConfiguredModel(ml,  0, 180, false),
                     new ConfiguredModel(mlm, 0, 180, false)
                 );
         })
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> LIVING_ROCK_BRICK =
        R.block("living_rock_brick", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> {
             var ml  = p.models().getExistingFile(p.modLoc("block/" + ctx.getName()));
             var mlm = p.models().getExistingFile(p.modLoc("block/" + ctx.getName() + "_mirrored"));
             p.getVariantBuilder(ctx.get())
                 .partialState()
                 .addModels(
                     new ConfiguredModel(ml),
                     new ConfiguredModel(mlm),
                     new ConfiguredModel(ml,  0, 180, false),
                     new ConfiguredModel(mlm, 0, 180, false)
                 );
         })
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<StairBlock> LIVING_ROCK_STAIRS =
        R.block("living_rock_stairs", p -> new StairBlock(LIVING_ROCK.get().defaultBlockState(), p))
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.stairsBlock(ctx.get(), p.modLoc("block/living_rock")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<SlabBlock> LIVING_ROCK_SLAB =
        R.block("living_rock_slab", SlabBlock::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.slabBlock(ctx.get(), p.modLoc("block/living_rock"), p.modLoc("block/living_rock")))
         .loot((t, b) -> t.add(b, t.createSlabItemTable(b)))
         .simpleItem()
         .register();

    public static final BlockEntry<WallBlock> LIVING_ROCK_WALL =
        R.block("living_rock_wall", WallBlock::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> {
             var tex = p.modLoc("block/living_rock");
             p.wallBlock(ctx.get(), tex);
             p.models().getBuilder(ctx.getName() + "_inventory")
                 .parent(new ModelFile.UncheckedModelFile("minecraft:block/wall_inventory"))
                 .texture("wall", tex.toString());
         })
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.blockWithInventoryModel(ctx::getEntry)).build()
         .register();

    public static final BlockEntry<StairBlock> LIVING_ROCK_POLISHED_STAIRS =
        R.block("living_rock_polished_stairs", p -> new StairBlock(LIVING_ROCK_POLISHED.get().defaultBlockState(), p))
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.stairsBlock(ctx.get(), p.modLoc("block/living_rock_polished")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<SlabBlock> LIVING_ROCK_POLISHED_SLAB =
        R.block("living_rock_polished_slab", SlabBlock::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.slabBlock(ctx.get(), p.modLoc("block/living_rock_polished"), p.modLoc("block/living_rock_polished")))
         .loot((t, b) -> t.add(b, t.createSlabItemTable(b)))
         .simpleItem()
         .register();

    public static final BlockEntry<WallBlock> LIVING_ROCK_POLISHED_WALL =
        R.block("living_rock_polished_wall", WallBlock::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> {
             var tex = p.modLoc("block/living_rock_polished");
             p.wallBlock(ctx.get(), tex);
             p.models().getBuilder(ctx.getName() + "_inventory")
                 .parent(new ModelFile.UncheckedModelFile("minecraft:block/wall_inventory"))
                 .texture("wall", tex.toString());
         })
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.blockWithInventoryModel(ctx::getEntry)).build()
         .register();

    public static final BlockEntry<StairBlock> LIVING_ROCK_BRICK_STAIRS =
        R.block("living_rock_brick_stairs", p -> new StairBlock(LIVING_ROCK_BRICK.get().defaultBlockState(), p))
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.stairsBlock(ctx.get(), p.modLoc("block/living_rock_brick")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<SlabBlock> LIVING_ROCK_BRICK_SLAB =
        R.block("living_rock_brick_slab", SlabBlock::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.slabBlock(ctx.get(), p.modLoc("block/living_rock_brick"), p.modLoc("block/living_rock_brick")))
         .loot((t, b) -> t.add(b, t.createSlabItemTable(b)))
         .simpleItem()
         .register();

    public static final BlockEntry<WallBlock> LIVING_ROCK_BRICK_WALL =
        R.block("living_rock_brick_wall", WallBlock::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> {
             var tex = p.modLoc("block/living_rock_brick");
             p.wallBlock(ctx.get(), tex);
             p.models().getBuilder(ctx.getName() + "_inventory")
                 .parent(new ModelFile.UncheckedModelFile("minecraft:block/wall_inventory"))
                 .texture("wall", tex.toString());
         })
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.blockWithInventoryModel(ctx::getEntry)).build()
         .register();

    // ── Infused / Sacred tiers (placeholder: reuse tier-1 textures, no art yet) ──

    public static final BlockEntry<Block> INFUSED_LIVING_ROCK =
        R.block("infused_living_rock", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.models().cubeAll(ctx.getName(), p.modLoc("block/living_rock"))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> INFUSED_LIVING_ROCK_POLISHED =
        R.block("infused_living_rock_polished", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.models().cubeAll(ctx.getName(), p.modLoc("block/living_rock_polished"))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> INFUSED_LIVING_ROCK_BRICK =
        R.block("infused_living_rock_brick", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.models().cubeAll(ctx.getName(), p.modLoc("block/living_rock_brick"))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> SACRED_LIVING_ROCK =
        R.block("sacred_living_rock", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.models().cubeAll(ctx.getName(), p.modLoc("block/living_rock"))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> SACRED_LIVING_ROCK_POLISHED =
        R.block("sacred_living_rock_polished", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.models().cubeAll(ctx.getName(), p.modLoc("block/living_rock_polished"))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> SACRED_LIVING_ROCK_BRICK =
        R.block("sacred_living_rock_brick", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.models().cubeAll(ctx.getName(), p.modLoc("block/living_rock_brick"))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    // ── Desecrated tier (Nox/Dark branch, placeholder: reuse tier-1 textures, no art yet) ──

    public static final BlockEntry<Block> DESECRATED_LIVING_ROCK =
        R.block("desecrated_living_rock", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.models().cubeAll(ctx.getName(), p.modLoc("block/living_rock"))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> DESECRATED_LIVING_ROCK_POLISHED =
        R.block("desecrated_living_rock_polished", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.models().cubeAll(ctx.getName(), p.modLoc("block/living_rock_polished"))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> DESECRATED_LIVING_ROCK_BRICK =
        R.block("desecrated_living_rock_brick", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.models().cubeAll(ctx.getName(), p.modLoc("block/living_rock_brick"))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(LIVING_ROCK.asStack(), tab);
        modifier.accept(LIVING_ROCK_STAIRS.asStack(), tab);
        modifier.accept(LIVING_ROCK_SLAB.asStack(), tab);
        modifier.accept(LIVING_ROCK_WALL.asStack(), tab);
        modifier.accept(LIVING_ROCK_POLISHED.asStack(), tab);
        modifier.accept(LIVING_ROCK_POLISHED_STAIRS.asStack(), tab);
        modifier.accept(LIVING_ROCK_POLISHED_SLAB.asStack(), tab);
        modifier.accept(LIVING_ROCK_POLISHED_WALL.asStack(), tab);
        modifier.accept(LIVING_ROCK_BRICK.asStack(), tab);
        modifier.accept(LIVING_ROCK_BRICK_STAIRS.asStack(), tab);
        modifier.accept(LIVING_ROCK_BRICK_SLAB.asStack(), tab);
        modifier.accept(LIVING_ROCK_BRICK_WALL.asStack(), tab);
        modifier.accept(INFUSED_LIVING_ROCK.asStack(), tab);
        modifier.accept(INFUSED_LIVING_ROCK_POLISHED.asStack(), tab);
        modifier.accept(INFUSED_LIVING_ROCK_BRICK.asStack(), tab);
        modifier.accept(SACRED_LIVING_ROCK.asStack(), tab);
        modifier.accept(SACRED_LIVING_ROCK_POLISHED.asStack(), tab);
        modifier.accept(SACRED_LIVING_ROCK_BRICK.asStack(), tab);
        modifier.accept(DESECRATED_LIVING_ROCK.asStack(), tab);
        modifier.accept(DESECRATED_LIVING_ROCK_POLISHED.asStack(), tab);
        modifier.accept(DESECRATED_LIVING_ROCK_BRICK.asStack(), tab);
    }

    public static void init() {}
}
