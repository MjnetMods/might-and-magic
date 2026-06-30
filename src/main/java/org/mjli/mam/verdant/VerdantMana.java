package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block.ApothecaryBlock;
import org.mjli.mam.block.mana.ManaPoolBlock;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;

public class VerdantMana {

    private static final MamRegistrate R = MightAndMagic.registrate();

    public static final BlockEntry<ManaPoolBlock> MANA_POOL =
        R.block("mana_pool", ManaPoolBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<ManaPoolBlock> INFUSED_MANA_POOL =
        R.block("infused_mana_pool", ManaPoolBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/mana_pool"))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/mana_pool"))).build()
         .register();

    public static final BlockEntry<ManaPoolBlock> SACRED_MANA_POOL =
        R.block("sacred_mana_pool", ManaPoolBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/mana_pool"))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/mana_pool"))).build()
         .register();

    public static final BlockEntry<ManaPoolBlock> DESECRATED_MANA_POOL =
        R.block("desecrated_mana_pool", ManaPoolBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/mana_pool"))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/mana_pool"))).build()
         .register();

    public static final BlockEntry<ApothecaryBlock> APOTHECARY =
        R.block("apothecary", ApothecaryBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(MANA_POOL.asStack(), tab);
        modifier.accept(INFUSED_MANA_POOL.asStack(), tab);
        modifier.accept(SACRED_MANA_POOL.asStack(), tab);
        modifier.accept(DESECRATED_MANA_POOL.asStack(), tab);
        modifier.accept(APOTHECARY.asStack(), tab);
    }

    public static void init() {}
}
