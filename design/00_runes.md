---
type: design
status: draft
last-updated: 2026-06-29
links: "[[20_verdant-path-altar]], [[00_infra]]"
---

# MAM — Rune Taxonomy

Runes are **universal magical primitives** — cross-school crafting components used across all magic paths in MAM. They are not school-specific items; they are the shared substrate from which school-specific recipes draw.

All runes are MAM-native (`mam:rune_*`). No dependency on Botania runes.

---

## Production

The **Altar** (Verdant Path) is the canonical primary source for all runes. Other schools will add alternative production routes for runes aligned to their themes — different mechanics, same output item. Rune of the Sanguine can be produced by the Altar AND by a Sanguine-specific ritual; both produce `mam:rune_sanguine`.

This means:
- Verdant players produce runes first
- Other-school players eventually get their own production path
- Cross-school rune trade is naturally incentivised in the interim

---

## Tier Structure

| Tier | Altar | Slots | Recipe template |
|------|-------|-------|-----------------|
| Infrastructure | Altar / Infused Altar | 4–5 | Tier-matched Living Rock + mana gem + elemental rune |
| T1 — Elemental | Altar | ≤4 | Natural ingredients + base Verdant materials |
| T2 — Concept | Infused Altar | 5–6 | 2× T1 runes + thematic ingredients |
| T3a — Grand Force | Sacred Altar | 8 | 2× Mana Rune + 4× Mana Dust + 2× T2 runes |
| T3b — School Essence | Sacred Altar (primary) | 8 | 2× Mana Rune + 4× Mana Dust + 2× T2 runes |

T3a and T3b use the same recipe template. School-specific alternative production routes for T3b are added per-school as they are designed.

---

## T1 — Elemental Runes

Six primitive forces. These are the atoms of the rune system — everything higher is built from combinations of these.

| Rune | `mam:` key | Concept | School alignment |
|------|------------|---------|-----------------|
| Rune of Life | `rune_life` | Growth, vitality, restoration | Verdant primary |
| Rune of Death | `rune_death` | Decay, ending, sacrifice | Sanguine primary |
| Rune of Order | `rune_order` | Structure, precision, law | Rational primary |
| Rune of Chaos | `rune_chaos` | Disruption, wild magic, entropy | unassigned (TBD) |
| Rune of Flow | `rune_flow` | Motion, transfer, mana current | Verdant secondary |
| Rune of Force | `rune_force` | Power, impact, physical energy | Rational secondary |

**Recipe template:** ≤4 ingredients, ~5,000 mana, natural + basic Verdant materials. Specific ingredients TBD during balancing pass.

---

## T2 — Concept Runes

Five synthesised concepts, each emerging from a pair of T1 runes plus thematic natural ingredients.

| Rune | `mam:` key | T1 Pair | Concept |
|------|------------|---------|---------|
| Rune of Growth | `rune_growth` | Life + Flow | Life made directional; cultivation and expansion |
| Rune of Decay | `rune_decay` | Death + Chaos | Uncontrolled breakdown; entropy in motion |
| Rune of Will | `rune_will` | Order + Force | Intent made powerful; directed purpose |
| Rune of Binding | `rune_binding` | Order + Chaos | Structure imposed on wild things; agreements, bonds |
| Rune of Mana | `rune_mana` | Flow + Life | Living magical current; the medium of Verdant magic |

**Recipe template:** 5–6 ingredients, ~8,000 mana. 2× T1 runes + 3–4 thematic natural ingredients. Specific ingredients TBD during balancing pass.

---

## T3a — Grand Force Runes

Six universal principles that emerge from combining two T2 concepts. These represent deeper magical truths — not tied to any school, available to all paths as powerful crafting components.

| Rune | `mam:` key | T2 Pair | Principle |
|------|------------|---------|-----------|
| Rune of the Cycle | `rune_cycle` | Growth + Decay | All things turn; life feeds death feeds life |
| Rune of Dominion | `rune_dominion` | Will + Binding | Absolute mastery; control made permanent |
| Rune of Manifestation | `rune_manifestation` | Will + Growth | Intent made real; creation from purpose |
| Rune of Entropy | `rune_entropy` | Decay + Binding | Structure dissolving; the unravelling of order |
| Rune of Resonance | `rune_resonance` | Mana + Binding | Deep attunement; magic anchored to form |
| Rune of Transcendence | `rune_transcendence` | Growth + Mana | Ascension through life and living mana |

**Recipe template:** 8 ingredients — `2× Rune of Mana + 4× Mana Dust + 2× T2 runes (the matching pair)`, ~12,000 mana.

---

## T3b — School Essence Runes

One rune per magic school. Each represents the school's fundamental magical identity — used in that school's capstone recipes and in cross-school hybrid crafting.

Primary production: Sacred Altar using same 8-slot template. Each school adds its own alternative production route as it is designed.

| Rune | `mam:` key | T2 Pair | School |
|------|------------|---------|--------|
| Rune of the Grove | `rune_grove` | Growth + Mana | Verdant |
| Rune of the Sanguine | `rune_sanguine` | Decay + Will | Sanguine |
| Rune of the Rational | `rune_rational` | Will + Binding | Rational |
| Rune of the Pact | `rune_pact` | Binding + Mana | Summoning |
| Rune of the Void | `rune_void` | Decay + Binding | TBD — school or generic dark |

**Recipe template:** 8 ingredients — `2× Mana Rune + 4× Mana Dust + 2× T2 runes (school-aligned pair)`, ~12,000 mana.

---

## Infrastructure Runes

Two runes that sit outside the school taxonomy — they gate pool tier progression and are intentionally cross-school. Any school can craft and use them.

| Rune | `mam:` key | Altar | Slots | Purpose |
|------|------------|-------|-------|---------|
| Rune of Infusion | `rune_infusion` | Altar (T1) | 4 | Upgrades Mana Pool → Infused Mana Pool |
| Rune of the Sacred | `rune_sacred` | Infused Altar (T2) | 5 | Upgrades Infused Mana Pool → Sacred Mana Pool |

**Rune of Infusion recipe** — ~6,000 mana:
```
2× Living Rock  +  1× #mam:mana_gems  +  1× Rune of Flow
```
Uses only T1 materials — no circular dependency with the Infused Pool.

**Rune of the Sacred recipe** — ~10,000 mana:
```
2× Infused Living Rock  +  1× Rune of Mana  +  1× Rune of Binding  +  1× #mam:mana_gems
```
Uses T2 runes — requires the Infused Pool to produce them first, which you already have at this point.

Both runes are consumed on use (dropped into the crafting table upgrade recipe along with the pool item).

---

## Item Registry Summary

### Infrastructure (2 items)
`rune_infusion`, `rune_sacred`

### T1 (6 items)
`rune_life`, `rune_death`, `rune_order`, `rune_chaos`, `rune_flow`, `rune_force`

### T2 (5 items)
`rune_growth`, `rune_decay`, `rune_will`, `rune_binding`, `rune_mana`

### T3a (6 items)
`rune_cycle`, `rune_dominion`, `rune_manifestation`, `rune_entropy`, `rune_resonance`, `rune_transcendence`

### T3b (5 items)
`rune_grove`, `rune_sanguine`, `rune_rational`, `rune_pact`, `rune_void`

**Total: 24 rune items**

---

## Implementation Order

Implement runes alongside the content that first requires them — not speculatively.

| Rune set | When to implement |
|----------|-------------------|
| T1 elemental | When Altar is implemented |
| T2 concept | When Infused Altar is implemented |
| Infrastructure (Rune of Infusion, Rune of the Sacred) | With pool upgrade mechanic (see `20_verdant-path-mana-pool.md`) |
| T3a Grand Force | When first recipe consuming them is designed |
| T3b School Essence | When each school's capstone content is designed |

---

## Open Questions

- [x] **Mana Dust** — pool infusion: Gunpowder **or** Redstone **or** Glowstone Dust **or** Sugar (any one), 500 mana, any T1 pool. OR logic — single item in, single item out. Adapted from Botania `mana_powder_dust.json`. Tag: add `mam:mana_dust` to `botania:mana_dusts` via `data/botania/tags/items/mana_dusts.json` (`replace: false`) — MAM and Botania dust are then interchangeable in all recipes using that tag.
- [ ] **T1 recipe ingredients** — specific natural items per rune TBD, balancing pass
- [ ] **T2 recipe ingredients** — thematic natural items per rune TBD, balancing pass
- [ ] **Mana costs** — all values approximate; tune during implementation
- [ ] **`rune_chaos` / `rune_void` alignment** — Nox is cross-school; these two runes are unassigned pending further school design. May become school-specific or stay as generic dark primitives.
- [ ] **Alternative production routes** — each non-Verdant school adds its own when designed

---

## Status

| Item | Status |
|------|--------|
| Taxonomy (T1/T2/T3a/T3b) | ✅ decided |
| Item keys (`mam:rune_*`) | ✅ decided |
| T3 recipe template | ✅ decided (2× Mana Rune + 4× Mana Dust + 2× T2 runes) |
| T1/T2 recipe ingredients | ⬜ TBD — balancing pass |
| Mana Dust design | ⬜ TBD |
| Infrastructure runes (recipe + keys) | ✅ decided |
| Item registration | ⬜ not started |
| Recipe JSONs | ⬜ not started |
