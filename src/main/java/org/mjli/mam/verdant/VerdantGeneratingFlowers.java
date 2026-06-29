package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block.flower.DewpetalBlock;
import org.mjli.mam.block.flower.EmberwortBlock;
import org.mjli.mam.block.flower.SolarbudBlock;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;

public class VerdantGeneratingFlowers {
    private static final MamRegistrate R = MightAndMagic.registrate();

    public static final BlockEntry<SolarbudBlock> SOLARBUD =
        R.block("solarbud", SolarbudBlock::new)
         .properties(p -> MamBlockProperties.flower())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(), p.modLoc("block/" + ctx.getName())).renderType("cutout")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<EmberwortBlock> EMBERWORT =
        R.block("emberwort", EmberwortBlock::new)
         .properties(p -> MamBlockProperties.flower())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(), p.modLoc("block/" + ctx.getName())).renderType("cutout")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<DewpetalBlock> DEWPETAL =
        R.block("dewpetal", DewpetalBlock::new)
         .properties(p -> MamBlockProperties.flower())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(), p.modLoc("block/" + ctx.getName())).renderType("cutout")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(SOLARBUD.asStack(), tab);
        modifier.accept(EMBERWORT.asStack(), tab);
        modifier.accept(DEWPETAL.asStack(), tab);
    }

    public static void init() {}
}
