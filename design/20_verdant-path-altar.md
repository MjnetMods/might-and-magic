---
type: design
status: draft
last-updated: 2026-06-29
links: "[[20_verdant-path]], [[20_verdant-path-mana-pool]], [[20_verdant-path-items]]"
---

# Verdant Path — Altar

The Altar is the Verdant ritual station. It is to the Verdant Path what the Runic Altar is to Botania — a mana-consuming, ingredient-accepting station that produces runes, upgrade components, and sacred crafted items through a drop-and-charge ritual.

---

## Thematic Framing

The Altar feels like a **living stone basin** that hums with sacred potential. Ingredients placed on it orbit slowly; mana fills it from a nearby pool; when the Living Rock trigger lands and mana is sufficient, the ritual completes and the output emerges. No wand. No manual step. The grove decides when it is ready.

---

## Tier Overview

Four tiers, each with a larger ingredient capacity. Slot count is the primary tier gate — a recipe that needs 10 ingredient slots physically cannot run on a 6-slot altar.

| Tier                 | Input Slots | Mana Source        | Notes                         |
|----------------------|-------------|--------------------|-------------------------------|
| **Altar**      | 4           | Any pool ≥ recipe cost | T1 runes, early rites     |
| **Infused Altar** | 6        | Any pool ≥ recipe cost | T2 runes, mid rites       |
| **Sacred Altar**       | 16          | Sacred Pool ≥ recipe cost     | T3 rites, endgame recipes   |
| **Desecrated Altar**   | 16          | Desecrated Pool ≥ recipe cost | Dark school T3 parallel     |

Mana gating follows the pool capacity model — T1/T2 altars accept any pool; T3 altars are energy-aligned (Sacred uses Mana, Desecrated uses Nox). The altar tier gates recipe access via slot count, not by pool type.

Slot counts are the progression gate: a recipe requiring 8 ingredients physically cannot run on a 6-slot altar, regardless of mana available. The jump from 6 → 16 at T3 is intentional — T3 recipes are meaningfully more complex than T2.

---

## Crafting Recipes

All three use the same flat-top U shape. The center slot holds either a gem (T1) or the previous-tier altar (T2/T3) — consuming it to produce the next tier.

```
S S S
S C S

S = rock (tier-matched)   C = center ingredient
```

| Output                  | S                   | C                              |
|-------------------------|---------------------|--------------------------------|
| Altar             | Living Rock           | Mana Pearl **or** Mana Diamond (`#mam:mana_gems`) |
| Infused Altar     | Infused Living Rock   | Altar (item)                                      |
| Sacred Altar      | Sacred Living Rock    | Infused Altar (item)                              |
| Desecrated Altar  | Desecrated Living Rock| Infused Altar (item)                              |

5× rock + 1× center per recipe. The previous-tier altar is consumed — not returned.

*Reference shape: `Botania/Xplat/src/generated/resources/data/botania/recipes/runic_altar.json`*

---

## Mechanic

### Ingredient detection

- Scans a 1×1×1 AABB above the altar each tick for `ItemEntity`s
- Any non-trigger item entity is automatically ingested into the altar's internal slot container (up to the tier's slot limit)
- Living Rock item entities are **not** ingested — they sit on the surface as the trigger

### Trigger and auto-craft

When all of the following are true simultaneously:
1. Living Rock is present on the altar surface (as an item entity)
2. The ingested items match a valid recipe for this altar tier
3. A nearby Mana Pool within range has ≥ the recipe's mana cost

The altar auto-crafts: consumes the mana from the pool, consumes the Living Rock, clears the ingredient slots, and ejects the output item at Y+1.5 above the altar.

No wand, no manual activation step.

### Mana sourcing

- Scans for a Mana Pool (any tier) within a fixed range (TBD — suggest 8 blocks)
- Calls `pool.drainMana(cost)` when the recipe fires
- Pool must have `mana >= recipe.manaCost` — partial drain does not trigger the recipe

### Output

- Output ejected as `ItemEntity` above the altar with a short pickup delay
- Output item is flagged to prevent re-ingest by the auto-pickup loop (same guard as Botania)

### Guard conditions

- Slots full (at tier limit): additional item entities are not ingested — they bounce off
- While mana is being waited on (recipe matched, Living Rock present, pool not yet full): ingredients are locked, new items rejected
- 60-tick cooldown after craft before the altar accepts new ingredients

---

## Recipe Format

Data-driven. Recipe type: `mam:altar`.

```json
{
  "type": "mam:altar",
  "ingredients": [
    { "item": "mam:living_rock" },
    { "tag": "mam:mana_gems" },
    { "item": "minecraft:red_mushroom" }
  ],
  "mana": 10000,
  "output": { "item": "mam:rune_of_infusion" }
}
```

- `ingredients` — unordered list; count must not exceed the altar tier's slot limit
- `mana` — cost drawn from the nearest pool; pool must hold at least this much
- `output` — single item result; count supported

The altar tier that can run a recipe is implicitly determined by `ingredients.length` vs slot count. No explicit tier field needed.

---

## Known Recipe Categories

Slot counts define which recipes land at which altar:

| Slot range | Altar | Recipe tier |
|------------|-------|-------------|
| ≤4 slots   | Altar            | T1 — elemental runes, early rites             |
| 5–6 slots  | Infused Altar    | T2 — concept runes, mid rites                 |
| 7–16 slots | Sacred Altar     | T3 — complex runes, endgame components (Mana) |
| 7–16 slots | Desecrated Altar | T3 — Nox recipes, dark school components      |

| Category | Altar Tier | Slots | Examples |
|----------|------------|-------|---------|
| Pool upgrade runes | T1, T2 | ≤4, 5–6 | Rune of Infusion, Rune of the Sacred |
| Elemental runes | T1 | ≤4 | TBD |
| Concept runes | T2 | 5–6 | TBD |
| Complex runes (2× Mana Rune + 4× Mana Dust + 2× Concept Rune) | T3 | 8 | TBD |
| Endgame sacred components | T3 | 8–16 | TBD |

### Rune recipes

Rune ingredients and mana costs are TBD — set during balancing pass. Constraints:
- Rune of Infusion: ≤4 ingredients, T1 materials only (runs on Altar)
- Rune of the Sacred: ≤6 ingredients, T2 materials only (runs on Infused Altar)
- T3 complex runes: 2× Mana Rune + 4× Mana Dust + 2× Concept Rune = 8 slots (requires Sacred Altar)

---

## Parallel with the Apothecary

The Apothecary and the Altar share the same core mechanic — drop items, medium charges, output ejects — differing only in their medium:

| Station | Medium | Trigger | Tiers |
|---------|--------|---------|-------|
| Apothecary | Water (filled like a cauldron) | — (auto on water present + recipe match) | 4 |
| Altar      | Mana (drawn from nearby pool)  | Living Rock drop                          | 4 |

Both station families share a base tick loop skeleton. Shared abstract base class recommended.

---

## Implementation Notes

- Internal storage: `SimpleContainer` sized to the altar tier's slot limit (4 / 6 / 16)
- Nearby pool scan: same pattern as pool infusion entity scan — once per second is sufficient
- `drainMana()` on the pool block entity — already needed for pool infusion; shared API
- Output item flagging: custom tag on `ItemEntity` NBT to prevent re-ingest (mirror Botania's `runicAltarSpawned`)
- Abstract base: `AltarBlockEntity` with slot count as a constructor param; T2/T3 subclass only

---

## Open Questions

- [ ] **Mana pool scan range** — 8 blocks suggested; tune during implementation
- [ ] **Rune ingredients and mana costs** — TBD, balancing pass
- [ ] **Other T1/T2/T3 recipe content** — what else does the altar produce beyond runes?
- [ ] **Visual** — ingredients orbit above altar? Particle effects during mana fill? Decide before client implementation

---

## Status

| Item | Status |
|------|--------|
| Tier design (slots, recipe shape) | ✅ decided |
| Crafting recipes | ✅ decided |
| Mechanic (trigger, mana, output) | ✅ decided |
| Recipe format | ✅ decided |
| Rune recipes (ingredients, costs) | ⬜ TBD |
| Other altar recipe content | ⬜ TBD |
| Implementation | ⬜ not started |
