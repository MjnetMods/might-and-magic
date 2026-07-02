package org.mjli.mam.verdant;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.mjli.mam.MamDataComponents;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.api.energy.EnergyContainer;
import org.mjli.mam.api.energy.EnergyType;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.mjli.mam.block.AltarBlock;
import org.mjli.mam.block.ApothecaryBlock;
import org.mjli.mam.block.SpreaderBlock;
import org.mjli.mam.block.SpreaderMarkColor;
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
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/" + ctx.getName()))).build()
         .register();

    public static final BlockEntry<ManaPoolBlock> SACRED_MANA_POOL =
        R.block("sacred_mana_pool", ManaPoolBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/" + ctx.getName()))).build()
         .register();

    public static final BlockEntry<ManaPoolBlock> DESECRATED_MANA_POOL =
        R.block("desecrated_mana_pool", ManaPoolBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/" + ctx.getName()))).build()
         .register();

    public static final BlockEntry<ApothecaryBlock> APOTHECARY =
        R.block("apothecary", ApothecaryBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    // Infused/Sacred/Desecrated share T1's shape+textures (already tinted/desaturated,
    // see design/magic/27_tier-tinting.md) and ApothecaryBlockEntity behavior — registration
    // only, no tier-specific mechanic yet.
    public static final BlockEntry<ApothecaryBlock> INFUSED_APOTHECARY =
        R.block("infused_apothecary", ApothecaryBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/apothecary"))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/apothecary"))).build()
         .register();

    public static final BlockEntry<ApothecaryBlock> SACRED_APOTHECARY =
        R.block("sacred_apothecary", ApothecaryBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/apothecary"))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/apothecary"))).build()
         .register();

    public static final BlockEntry<ApothecaryBlock> DESECRATED_APOTHECARY =
        R.block("desecrated_apothecary", ApothecaryBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/apothecary"))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/apothecary"))).build()
         .register();

    // Verdant ritual station (design/magic/20_altar.md) — registration only, no in-world
    // mechanic yet (ingredient detection/trigger/recipe firing are separate follow-up work).
    // T1 keeps its original colored texture; higher tiers reuse the tinted-shape + desaturated-
    // texture pattern from design/magic/27_tier-tinting.md, each tier with its own model since
    // (unlike Apothecary) the desaturated textures are distinct files, not a shared reused model.
    public static final BlockEntry<AltarBlock> ALTAR =
        R.block("altar", AltarBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<AltarBlock> INFUSED_ALTAR =
        R.block("infused_altar", AltarBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/" + ctx.getName()))).build()
         .register();

    public static final BlockEntry<AltarBlock> SACRED_ALTAR =
        R.block("sacred_altar", AltarBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/" + ctx.getName()))).build()
         .register();

    public static final BlockEntry<AltarBlock> DESECRATED_ALTAR =
        R.block("desecrated_altar", AltarBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().getExistingFile(p.modLoc("block/" + ctx.getName()))))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/" + ctx.getName()))).build()
         .register();

    // Cross-school transport block (design/magic/16_mana-spreader.md) — registration only, no
    // burst/aim mechanic yet. T1 keeps original Livingwood coloring; higher tiers reuse the
    // tinted-shape + desaturated-texture pattern from design/magic/27_tier-tinting.md.
    public static final BlockEntry<SpreaderBlock> SPREADER =
        R.block("mana_spreader", SpreaderBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> spreaderVariants(ctx, p, ctx.getName()))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<SpreaderBlock> INFUSED_SPREADER =
        R.block("infused_mana_spreader", SpreaderBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> spreaderVariants(ctx, p, ctx.getName()))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/" + ctx.getName()))).build()
         .register();

    public static final BlockEntry<SpreaderBlock> SACRED_SPREADER =
        R.block("sacred_mana_spreader", SpreaderBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> spreaderVariants(ctx, p, ctx.getName()))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/" + ctx.getName()))).build()
         .register();

    public static final BlockEntry<SpreaderBlock> DESECRATED_SPREADER =
        R.block("desecrated_mana_spreader", SpreaderBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .blockstate((ctx, p) -> spreaderVariants(ctx, p, ctx.getName()))
         .loot((t, b) -> t.dropSelf(b))
         .item().model((ctx, p) -> p.withExistingParent(ctx.getName(), p.modLoc("block/" + ctx.getName()))).build()
         .register();

    // Loop Marking (design/magic/16_mana-spreader.md) — MARK == NONE uses the tier's plain
    // model, any other value swaps in the "_marked" model (same shape + a rune-glyph decal on
    // tintindex 1). Both models already exist per tier; this only wires the blockstate switch.
    private static void spreaderVariants(DataGenContext<Block, SpreaderBlock> ctx, RegistrateBlockstateProvider p, String name) {
        var unmarked = p.models().getExistingFile(p.modLoc("block/" + name));
        var marked = p.models().getExistingFile(p.modLoc("block/" + name + "_marked"));
        p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> new ConfiguredModel[] { new ConfiguredModel(
            state.getValue(SpreaderBlock.MARK) == SpreaderMarkColor.NONE ? unmarked : marked) });
    }

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
        modifier.accept(INFUSED_APOTHECARY.asStack(), tab);
        modifier.accept(SACRED_APOTHECARY.asStack(), tab);
        modifier.accept(DESECRATED_APOTHECARY.asStack(), tab);
        modifier.accept(ALTAR.asStack(), tab);
        modifier.accept(INFUSED_ALTAR.asStack(), tab);
        modifier.accept(SACRED_ALTAR.asStack(), tab);
        modifier.accept(DESECRATED_ALTAR.asStack(), tab);
        modifier.accept(SPREADER.asStack(), tab);
        modifier.accept(INFUSED_SPREADER.asStack(), tab);
        modifier.accept(SACRED_SPREADER.asStack(), tab);
        modifier.accept(DESECRATED_SPREADER.asStack(), tab);
        modifier.accept(MANA_TABLET.asStack(), tab);
    }

    public static void init() {}
}
