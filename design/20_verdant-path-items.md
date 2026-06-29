---
type: design
status: draft
last-updated: 2026-06-29
links: "[[20_verdant-path]], [[20_verdant-path-mana-pool]], [[20_verdant-path-quipment]]"
---

# Verdant Path — Items

Mana-infused consumables, portable mana storage, and wearable trinkets. All produced via pool infusion or crafted from infused materials.

---

## Gems

Two gem tracks — Diamond and Pearl — each in three tiers. Produced by dropping the base material into the matching pool tier. Used as crafting components in higher-tier recipes (tablets, rings, gear).

### Mana Diamonds

| Output           | Input   | Pool Required     | Mana Cost  |
|------------------|---------|-------------------|------------|
| Mana Diamond     | Diamond | Mana Pool         | 750,000    |
| Infused Diamond  | Diamond | Infused Mana Pool | 3,000,000  |
| Sacred Diamond   | Diamond | Sacred Mana Pool  | 12,000,000 |

### Mana Pearls

| Output          | Input        | Pool Required     | Mana Cost  |
|-----------------|--------------|-------------------|------------|
| Mana Pearl      | Ender Pearl  | Mana Pool         | 750,000    |
| Infused Pearl   | Ender Pearl  | Infused Mana Pool | 3,000,000  |
| Sacred Pearl    | Ender Pearl  | Sacred Mana Pool  | 12,000,000 |

All gem infusion costs follow the 75% rule — consistent with ingots and Living Rock.

---

## Tablets

A Mana Tablet is an item held in the inventory (or curio slot) that stores mana and passively repairs Verdant gear. Three tiers exist, each with a larger mana capacity.

| Tablet          | Mana Capacity | Notes                               |
|-----------------|---------------|-------------------------------------|
| Mana Tablet     | 500,000       | Half a T1 pool; early portable mana |
| Infused Tablet  | 2,000,000     | Half a T2 pool                      |
| Sacred Tablet   | 8,000,000     | Half a T3 pool                      |

Capacity = 50% of the matching pool tier. This means a full tablet can repair gear from a single pool fill without the pool needing to be completely drained.

### Tablet repair behaviour

- Passively drains mana from the tablet to repair equipped Verdant gear each tick
- Works from any inventory slot (not just curio) — convenience item, not a commitment
- Drain rate TBD during balancing; should feel like a slow burn, not instant refill

### Tablet crafting

Hollow square of tier-matched Living Rock with the gem in the center. Either gem track works — implemented as a single recipe per tier using an item tag (`#mam:mana_gems_<tier>`) rather than two alt recipes.

```
L L L
L G L   L = Living Rock (tier-matched)   G = gem (Diamond or Pearl, tier-matched)
L L L
```

| Output          | L                  | G (either)                          |
|-----------------|--------------------|-------------------------------------|
| Mana Tablet     | Living Rock        | Mana Diamond **or** Mana Pearl      |
| Infused Tablet  | Infused Living Rock| Infused Diamond **or** Infused Pearl|
| Sacred Tablet   | Sacred Living Rock | Sacred Diamond **or** Sacred Pearl  |

Tags needed: `#mam:mana_gems` (T1), `#mam:infused_gems` (T2), `#mam:sacred_gems` (T3) — each containing its tier's Diamond and Pearl.

*Reference: `Botania/Xplat/src/generated/resources/data/botania/recipes/mana_tablet.json` + `mana_tablet_alt.json`*

---

## Rings

A Ring is the curio-slot equivalent of a Tablet — worn in the ring slot, same mana storage as its tablet tier, but frees up inventory space and may gain passive bonuses.

| Ring          | Source        | Curio Slot | Mana Capacity | Passive Bonus     |
|---------------|---------------|------------|---------------|-------------------|
| Mana Ring     | Mana Tablet   | Ring       | 500,000       | TBD               |
| Infused Ring  | Infused Tablet| Ring       | 2,000,000     | TBD               |
| Sacred Ring   | Sacred Tablet | Ring       | 8,000,000     | TBD               |

### Tablet → Ring conversion

Ring shape made from the tier's ingots, with the tablet in the top-left slot. Adapted directly from Botania's `mana_ring` recipe.

```
T I .
I . I   T = tablet (tier-matched)   I = ingot (tier-matched)
. I .
```

| Output        | T               | I             |
|---------------|-----------------|---------------|
| Mana Ring     | Mana Tablet     | Mana Ingot    |
| Infused Ring  | Infused Tablet  | Infused Ingot |
| Sacred Ring   | Sacred Tablet   | Sacred Ingot  |

5 ingots + 1 tablet per ring. The gem track is already baked into the tablet; no gem choice at ring stage.

*Reference: `Botania/Xplat/src/generated/resources/data/botania/recipes/mana_ring.json`*

### Ring — dual purpose

The ring is a deliberate choice point: wear it standalone, or commit it into cloth.

- **Wear as curio** — worn in the ring slot; same mana storage and passive repair as the tablet, but hands-free
- **Weave into cloth** — consumed at the Weavery; mana storage lives permanently in that cloth piece

This makes the tablet → ring step meaningful: the tablet is the portable workhorse, the ring is the commitment artifact. Players who invest in a full cloth set will weave their rings in; players who prefer flexibility keep them as curios.

### Ring passive bonuses

Held for future design pass. Candidates:
- Mana Ring: slightly faster gear repair rate when worn as curio
- Infused Ring: pulls mana from nearby pools passively (small radius)
- Sacred Ring: aura generation, contributes to Verdant Aura field

Keep simple for initial implementation — mana storage and repair only.

---

## Trinkets

Trinkets are crafted items with a passive effect. Like the Ring, each trinket is a **choice point**: wear it standalone in a curio slot for flexibility, or weave it permanently into a cloth armor piece at the Weavery.

The Mana Ring (converted from tablet) follows the same pattern — it can be woven into any cloth slot. All other trinkets have a fixed slot affinity based on what makes physical and thematic sense.

### Slot affinity

| Slot    | Trinket effects |
|---------|-----------------|
| **Hood**     | Reach, Water Breathing, Aura |
| **Robe**     | Holy Cloak, Unholy Cloak, Balance Cloak, Invisibility Cloak, Pixie Ring, Diva Charm |
| **Sash**     | Travel Belt, Speed Belt, Super Travel Belt, Knockback Belt |
| **Slippers** | Cloud Pendant (featherfall), Ice Pendant, Lava Pendant, Magnet, Dodge |

### Trinket list

Adapted from Botania — effects and names to be Verdant-themed on implementation.

| Trinket | Slot | Effect | Source |
|---------|------|--------|--------|
| Reach Weave | Hood | Extended block/entity reach | Botania: Reach Ring |
| Breathing Weave | Hood | Water breathing + swim speed | Botania: Water Ring |
| Aura Weave | Hood | Generates Verdant Aura around player | Botania: Aura Ring |
| Sacred Cloak | Robe | Holy burst damages attacker on hit | Botania: Holy Cloak |
| Thorn Cloak | Robe | Reflects damage to attacker | Botania: Unholy Cloak |
| Balance Cloak | Robe | Distributes damage across nearby players | Botania: Balance Cloak |
| Veil Cloak | Robe | Grants invisibility | Botania: Invisibility Cloak |
| Pixie Cloak | Robe | Summons pixies to fight for you | Botania: Pixie Ring |
| Presence Weave | Robe | Mobs target you instead of others | Botania: Diva Charm |
| Wanderer's Sash | Sash | Movement speed + step assist | Botania: Travel Belt |
| Gale Sash | Sash | Sprint speed boost | Botania: Speed Up Belt |
| Repulse Sash | Sash | Knocks back nearby enemies | Botania: Knockback Belt |
| Feather Slippers | Slippers | Featherfall | Botania: Cloud Pendant |
| Frost Slippers | Slippers | Frost/snow immunity | Botania: Ice Pendant |
| Ember Slippers | Slippers | Fire immunity | Botania: Lava Pendant |
| Draw Slippers | Slippers | Attracts nearby items | Botania: Magnet Ring |
| Shadow Step | Slippers | Chance to dodge attacks | Botania: Dodge Ring |

Recipes TBD — all crafted from Verdant materials, then woven into cloth via the Weavery.

---

## Open Questions

- [ ] **Tablet recipe** — what materials? After ingots and gems are implemented.
- [ ] **Ring passive bonuses** — hold for post-launch pass; implement storage + repair only first.
- [ ] **Tablet slot** — inventory-only or also fits curio offhand slot? Affects repair reliability when no hands-free slot.
- [ ] **Repair rate** — how fast does the tablet drain to repair? Affects tablet capacity feel.

---

## Status

| Item                    | Status |
|-------------------------|--------|
| Mana Diamond (T1)       | ⬜ planned |
| Infused Diamond (T2)    | ⬜ planned |
| Sacred Diamond (T3)     | ⬜ planned |
| Mana Pearl (T1)         | ⬜ planned |
| Infused Pearl (T2)      | ⬜ planned |
| Sacred Pearl (T3)       | ⬜ planned |
| Mana Tablet (T1)        | ⬜ planned |
| Infused Tablet (T2)     | ⬜ planned |
| Sacred Tablet (T3)      | ⬜ planned |
| Mana Ring (T1)          | ⬜ planned |
| Infused Ring (T2)       | ⬜ planned |
| Sacred Ring (T3)        | ⬜ planned |
