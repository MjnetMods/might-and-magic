package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.mjli.mam.MamDataComponents;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.api.energy.EnergyContainer;
import org.mjli.mam.api.energy.EnergyType;
import org.mjli.mam.block.ApothecaryBlock;
import org.mjli.mam.block.mana.ManaPoolBlock;
import org.mjli.mam.block_entity.mana.ManaPoolBlockEntity;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;

public class VerdantMana {

    private static final MamRegistrate R = MightAndMagic.registrate();

    // Trinkets (design/magic/17_trinkets.md) — capacity is 50% of the matching pool tier
    public static final int MANA_TABLET_CAPACITY = ManaPoolBlockEntity.MAX_CAPACITY_TIER_1 / 2;

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

    // T1 only — Infused/Sacred/Desecrated Tablets are blocked on gem infusion (design/magic/17_trinkets.md)
    public static final ItemEntry<Item> MANA_TABLET =
        R.item("mana_tablet", p -> new Item(p.stacksTo(1)
                .component(MamDataComponents.ENERGY_CONTAINER.get(),
                        new EnergyContainer(0, MANA_TABLET_CAPACITY, EnergyType.MANA, false))))
         .register();

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(MANA_POOL.asStack(), tab);
        modifier.accept(INFUSED_MANA_POOL.asStack(), tab);
        modifier.accept(SACRED_MANA_POOL.asStack(), tab);
        modifier.accept(DESECRATED_MANA_POOL.asStack(), tab);
        modifier.accept(APOTHECARY.asStack(), tab);
        modifier.accept(MANA_TABLET.asStack(), tab);
    }

    public static void init() {}
}
