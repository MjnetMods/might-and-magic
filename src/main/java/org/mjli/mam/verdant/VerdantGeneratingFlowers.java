package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block.flower.DaybloomBlock;
import org.mjli.mam.block.flower.EndoflameBlock;
import org.mjli.mam.block.flower.HydroangeasBlock;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;

public class VerdantGeneratingFlowers {
    private static final MamRegistrate R = MightAndMagic.registrate();

    public static final BlockEntry<DaybloomBlock> DAYBLOOM =
        R.block("daybloom", DaybloomBlock::new)
         .properties(p -> MamBlockProperties.flower())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(), p.modLoc("block/" + ctx.getName())).renderType("cutout")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<EndoflameBlock> ENDOFLAME =
        R.block("endoflame", EndoflameBlock::new)
         .properties(p -> MamBlockProperties.flower())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(), p.modLoc("block/" + ctx.getName())).renderType("cutout")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<HydroangeasBlock> HYDROANGEAS =
        R.block("hydroangeas", HydroangeasBlock::new)
         .properties(p -> MamBlockProperties.flower())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(), p.modLoc("block/" + ctx.getName())).renderType("cutout")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(DAYBLOOM.asStack(), tab);
        modifier.accept(ENDOFLAME.asStack(), tab);
        modifier.accept(HYDROANGEAS.asStack(), tab);
    }

    public static void init() {}
}
