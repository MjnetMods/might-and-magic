package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
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

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(LIVING_ROCK.asStack(), tab);
        modifier.accept(LIVING_ROCK_POLISHED.asStack(), tab);
        modifier.accept(LIVING_ROCK_BRICK.asStack(), tab);
    }

    public static void init() {}
}
