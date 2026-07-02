package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.foundation.registration.MamRegistrate;

/**
 * Rune taxonomy — design/magic/25_runes.md. Universal cross-school crafting primitives,
 * registered here (alongside the other Verdant-first shared infra in this package) since
 * the Altar is the Verdant Path's station and the canonical primary producer.
 */
public class VerdantRunes {

    private static final MamRegistrate R = MightAndMagic.registrate();

    // Infrastructure — pool tier upgrades
    public static final ItemEntry<Item> RUNE_INFUSION = simple("rune_infusion");
    public static final ItemEntry<Item> RUNE_SACRED = simple("rune_sacred");
    public static final ItemEntry<Item> RUNE_DESECRATED = simple("rune_desecrated");

    // T1 — Elemental
    public static final ItemEntry<Item> RUNE_LIFE = simple("rune_life");
    public static final ItemEntry<Item> RUNE_DEATH = simple("rune_death");
    public static final ItemEntry<Item> RUNE_ORDER = simple("rune_order");
    public static final ItemEntry<Item> RUNE_CHAOS = simple("rune_chaos");
    public static final ItemEntry<Item> RUNE_FLOW = simple("rune_flow");
    public static final ItemEntry<Item> RUNE_FORCE = simple("rune_force");

    // T2 — Concept
    public static final ItemEntry<Item> RUNE_GROWTH = simple("rune_growth");
    public static final ItemEntry<Item> RUNE_DECAY = simple("rune_decay");
    public static final ItemEntry<Item> RUNE_WILL = simple("rune_will");
    public static final ItemEntry<Item> RUNE_BINDING = simple("rune_binding");
    public static final ItemEntry<Item> RUNE_MANA = simple("rune_mana");

    // T3a — Grand Force
    public static final ItemEntry<Item> RUNE_CYCLE = simple("rune_cycle");
    public static final ItemEntry<Item> RUNE_DOMINION = simple("rune_dominion");
    public static final ItemEntry<Item> RUNE_MANIFESTATION = simple("rune_manifestation");
    public static final ItemEntry<Item> RUNE_ENTROPY = simple("rune_entropy");
    public static final ItemEntry<Item> RUNE_RESONANCE = simple("rune_resonance");
    public static final ItemEntry<Item> RUNE_TRANSCENDENCE = simple("rune_transcendence");

    // T3b — School Essence
    public static final ItemEntry<Item> RUNE_GROVE = simple("rune_grove");
    public static final ItemEntry<Item> RUNE_SANGUINE = simple("rune_sanguine");
    public static final ItemEntry<Item> RUNE_RATIONAL = simple("rune_rational");
    public static final ItemEntry<Item> RUNE_PACT = simple("rune_pact");
    public static final ItemEntry<Item> RUNE_VOID = simple("rune_void");

    private static ItemEntry<Item> simple(String name) {
        return R.item(name, Item::new).register();
    }

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(RUNE_INFUSION.asStack(), tab);
        modifier.accept(RUNE_SACRED.asStack(), tab);
        modifier.accept(RUNE_DESECRATED.asStack(), tab);
        modifier.accept(RUNE_LIFE.asStack(), tab);
        modifier.accept(RUNE_DEATH.asStack(), tab);
        modifier.accept(RUNE_ORDER.asStack(), tab);
        modifier.accept(RUNE_CHAOS.asStack(), tab);
        modifier.accept(RUNE_FLOW.asStack(), tab);
        modifier.accept(RUNE_FORCE.asStack(), tab);
        modifier.accept(RUNE_GROWTH.asStack(), tab);
        modifier.accept(RUNE_DECAY.asStack(), tab);
        modifier.accept(RUNE_WILL.asStack(), tab);
        modifier.accept(RUNE_BINDING.asStack(), tab);
        modifier.accept(RUNE_MANA.asStack(), tab);
        modifier.accept(RUNE_CYCLE.asStack(), tab);
        modifier.accept(RUNE_DOMINION.asStack(), tab);
        modifier.accept(RUNE_MANIFESTATION.asStack(), tab);
        modifier.accept(RUNE_ENTROPY.asStack(), tab);
        modifier.accept(RUNE_RESONANCE.asStack(), tab);
        modifier.accept(RUNE_TRANSCENDENCE.asStack(), tab);
        modifier.accept(RUNE_GROVE.asStack(), tab);
        modifier.accept(RUNE_SANGUINE.asStack(), tab);
        modifier.accept(RUNE_RATIONAL.asStack(), tab);
        modifier.accept(RUNE_PACT.asStack(), tab);
        modifier.accept(RUNE_VOID.asStack(), tab);
    }

    public static void init() {}
}
