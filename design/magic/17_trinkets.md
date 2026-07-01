---
type: design
status: wip
last-updated: 2026-07-01
links: ["[[magic/00_energy]]", "[[magic/15_mana-pool]]", "[[magic/30_weavery]]", "[[20_verdant-path-items]]", "[[20_verdant-path-quipment]]"]
---

# MAM — Gems, Tablets & Rings

Generic, cross-school Mana/Nox portable storage — the wearable/carryable counterpart to the Mana Pool ([[magic/15_mana-pool]]). All produced via pool infusion or crafted from infused materials, four tiers each (Mana/Infused/Sacred/Desecrated), Sacred and Desecrated as parallel T3 branches.

> Naming note: this file is named `trinkets.md` because Rings are the energy-storage half of what gets woven into cloth at the Weavery — but the actual named **Trinket catalog** (Reach Weave, Breathing Weave, etc.) lives in [[20_verdant-path-items]], same as Weavery's own trinket content staying in its source doc rather than moving here.

---

## Gems

Two gem tracks — Diamond and Pearl — each in four tiers. Produced by dropping the base material into the matching pool tier. Used as crafting components in higher-tier recipes (tablets, rings, gear).

### Mana Diamonds

| Output           | Input   | Pool Required     | Mana Cost  |
|------------------|---------|-------------------|------------|
| Mana Diamond     | Diamond | Mana Pool         | 750,000    |
| Infused Diamond  | Diamond | Infused Mana Pool | 3,000,000  |
| Sacred Diamond   | Diamond | Sacred Mana Pool  | 12,000,000 |
| Desecrated Diamond | Diamond | Desecrated Mana Pool | 12,000,000 |

### Mana Pearls

| Output          | Input        | Pool Required     | Mana Cost  |
|-----------------|--------------|-------------------|------------|
| Mana Pearl      | Ender Pearl  | Mana Pool         | 750,000    |
| Infused Pearl   | Ender Pearl  | Infused Mana Pool | 3,000,000  |
| Sacred Pearl    | Ender Pearl  | Sacred Mana Pool  | 12,000,000 |
| Desecrated Pearl | Ender Pearl | Desecrated Mana Pool | 12,000,000 |

All gem infusion costs follow the 75% rule — consistent with ingots and Living Rock.

---

## Tablets

A Mana Tablet is an item held in the inventory (or curio slot) that stores mana and passively repairs equipped gear. Four tiers exist — Sacred and Desecrated are parallel T3 branches, each with a larger mana capacity.

| Tablet          | Mana Capacity | Notes                               |
|-----------------|---------------|-------------------------------------|
| Mana Tablet     | 500,000       | Half a T1 pool; early portable mana |
| Infused Tablet  | 2,000,000     | Half a T2 pool                      |
| Sacred Tablet   | 8,000,000     | Half a T3 pool (Mana)               |
| Desecrated Tablet | 8,000,000   | Half a T3 pool (Nox)                |

Capacity = 50% of the matching pool tier. This means a full tablet can repair gear from a single pool fill without the pool needing to be completely drained.

### Loading / unloading mana (implemented, T1 only)

A Tablet charges by touching down on a Mana Pool — same "drop it on the pool" interaction as infusion ([[magic/15_mana-pool]] § Infusion Mechanic), but the pool captures the item into an internal slot instead of consuming it through a recipe. This avoids the item ever existing as a world `ItemEntity` (so it can't despawn, burn, or be picked up by mobs) while it charges.

- **Insert** — right-click the pool while holding a mana-storage item and the slot is empty
- **Retrieve** — right-click the pool empty-handed while the slot is occupied; item returns to the player's inventory (or drops in front of them if inventory is full)
- **Transfer** — each pool tick, mana moves in whichever direction has room: pool → item (load) if the item isn't full and the pool has mana; item → pool (unload) if the item has spare mana and the pool isn't full
- **Cross-polarity behaviour matches pool tiering exactly** — a T1/T2 Tablet is unaligned (same as T1/T2 pools): charging it at the opposite-polarity pool *converts* it to that polarity. A T3 Sacred/Desecrated Tablet is aligned (same as T3 pools): opposite-polarity contact drains it instead of converting.
- Transfer rate is a placeholder (`CHARGE_RATE = 10,000/tick` in `ManaPoolBlockEntity`) — tune during balancing, same as repair drain rate below.

Only the base **Mana Tablet (T1)** exists so far — registered with no crafting recipe yet (`/give` only), matching the "Tablet recipe: after ingots and gems are implemented" open question below. Repair behaviour (draining the tablet to fix equipped gear) is not implemented.

### Tablet repair behaviour

- Passively drains mana from the tablet to repair equipped gear each tick
- Works from any inventory slot (not just curio) — convenience item, not a commitment
- Drain rate TBD during balancing; should feel like a slow burn, not instant refill

### Tablet crafting

Hollow square of tier-matched Living Rock with the gem in the center. Either gem track works — implemented as a single recipe per tier using an item tag rather than two alt recipes.

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
| Desecrated Tablet | Desecrated Living Rock | Desecrated Diamond **or** Desecrated Pearl |

Tags needed: `#mam:mana_gems` (T1), `#mam:infused_gems` (T2), `#mam:sacred_gems` (T3, Mana), `#mam:desecrated_gems` (T3, Nox) — each containing its tier's Diamond and Pearl. Sacred and Desecrated are separate tags, not shared — unlike the Infrastructure Rune recipes ([[magic/25_runes]]), a Tablet's own output tier *is* the polarity, so it needs the polarity-matched gem directly, not the neutral T2 gateway tag.

*Reference: `Botania/Xplat/src/generated/resources/data/botania/recipes/mana_tablet.json` + `mana_tablet_alt.json`*

---

## Rings

A Ring is the curio-slot equivalent of a Tablet — worn in the ring slot, same mana storage as its tablet tier, but frees up inventory space and may gain passive bonuses.

| Ring          | Source        | Curio Slot | Mana Capacity | Passive Bonus     |
|---------------|---------------|------------|---------------|-------------------|
| Mana Ring     | Mana Tablet   | Ring       | 500,000       | TBD               |
| Infused Ring  | Infused Tablet| Ring       | 2,000,000     | TBD               |
| Sacred Ring   | Sacred Tablet | Ring       | 8,000,000     | TBD               |
| Desecrated Ring | Desecrated Tablet | Ring | 8,000,000   | TBD               |

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
| Desecrated Ring | Desecrated Tablet | Desecrated Ingot |

5 ingots + 1 tablet per ring. The gem track is already baked into the tablet; no gem choice at ring stage.

*Reference: `Botania/Xplat/src/generated/resources/data/botania/recipes/mana_ring.json`*

### Ring — dual purpose

The ring is a deliberate choice point: wear it standalone, or commit it into cloth.

- **Wear as curio** — worn in the ring slot; same mana storage and passive repair as the tablet, but hands-free
- **Weave into cloth** — consumed at the Weavery ([[magic/30_weavery]]); mana storage lives permanently in that cloth piece

This makes the tablet → ring step meaningful: the tablet is the portable workhorse, the ring is the commitment artifact. Players who invest in a full cloth set will weave their rings in; players who prefer flexibility keep them as curios.

### Ring passive bonuses

Held for future design pass. Candidates:
- Mana Ring: slightly faster gear repair rate when worn as curio
- Infused Ring: pulls mana from nearby pools passively (small radius)
- Sacred Ring: aura generation, contributes to Verdant Aura field

Keep simple for initial implementation — mana storage and repair only.

---

## Open Questions

**Q:** Tablet recipe — what materials? After ingots and gems are implemented.

**Q:** Ring passive bonuses — hold for post-launch pass; implement storage + repair only first.

**Q:** Tablet slot — inventory-only or also fits curio offhand slot? Affects repair reliability when no hands-free slot.

**Q:** Repair rate — how fast does the tablet drain to repair? Affects tablet capacity feel.

---

## Status

| Item                    | Status |
|-------------------------|--------|
| Mana Diamond (T1)       | ⬜ planned |
| Infused Diamond (T2)    | ⬜ planned |
| Sacred Diamond (T3)     | ⬜ planned |
| Desecrated Diamond (T3) | ⬜ planned |
| Mana Pearl (T1)         | ⬜ planned |
| Infused Pearl (T2)      | ⬜ planned |
| Sacred Pearl (T3)       | ⬜ planned |
| Desecrated Pearl (T3)   | ⬜ planned |
| Mana Tablet (T1)        | ⬜ item + pool load/unload implemented — no crafting recipe or repair yet |
| Infused Tablet (T2)     | ⬜ planned |
| Sacred Tablet (T3)      | ⬜ planned |
| Desecrated Tablet (T3)  | ⬜ planned |
| Mana Ring (T1)          | ⬜ planned |
| Infused Ring (T2)       | ⬜ planned |
| Sacred Ring (T3)        | ⬜ planned |
| Desecrated Ring (T3)    | ⬜ planned |
