---
type: design
status: done
last-updated: 2026-07-02
links: ["[[magic/25_runes]]", "[[24_verdant-flowers-botania-compat]]"]
---

# MAM — Rune Botania Compat

MAM's runes are mechanically independent of Botania — see [[magic/25_runes]] § Production ("No dependency on Botania runes"). This doc covers the separate, additive question: when both mods are loaded, should Botania recognise MAM's runes as runes too? Same policy shape as flower compat (see [[24_verdant-flowers-botania-compat]]), applied to a different Botania tag structure.

---

## Tag structure — one flat tag, not per-concept

Unlike the flower tags (three separate tags by kind), Botania groups **all** of its runes into a single tag: `data/botania/tags/items/runes.json` (`botania:runes`). There is no per-element tag (no `botania:rune_fire` as a tag, only as an item ID) — so "tag MAM runes as the same runes as Botania" can only mean the broad "this item is a rune" membership, not a semantic pairing like MAM's Rune of Force ↔ Botania's Rune of Fire. No dependency is introduced by this — the tag file is purely additive, the same non-dependency shape as the flower tags.

**Q:** Should MAM runes be added to `botania:runes`?
**A:** Yes, all 25. Mirrors the flower tag policy (CLAUDE.md § Flower identity & Botania compat) — cross-mod recognition without a code dependency. File: `data/botania/tags/items/runes.json`, `replace: false`.

---

## Placeholder art provenance

Registering 25 rune items ahead of final art (per the Item Registry Summary in [[magic/25_runes]]) needed *some* texture so items aren't rendered as missing-texture in-world. Botania ships 16 rune textures (4 elemental + 4 seasonal + mana + 7 sin runes) — reused directly as placeholders rather than commissioning 25 new pieces for art that will be replaced anyway. No thematic pairing is implied beyond loose color/concept fit chosen for visual variety; several MAM runes intentionally share a source texture since 25 > 16.

| MAM item | Display name | Placeholder texture source (`botania:`) |
|----------|--------------|------------------------------------------|
| `rune_infusion` | Rune of Infusion | `rune_earth` |
| `rune_sacred` | Rune of the Sacred | `rune_mana` |
| `rune_desecrated` | Rune of the Desecrated | `rune_envy` |
| `rune_life` | Rune of Life | `rune_spring` |
| `rune_death` | Rune of Death | `rune_autumn` |
| `rune_order` | Rune of Order | `rune_air` |
| `rune_chaos` | Rune of Chaos | `rune_wrath` |
| `rune_flow` | Rune of Flow | `rune_water` |
| `rune_force` | Rune of Force | `rune_fire` |
| `rune_growth` | Rune of Growth | `rune_summer` |
| `rune_decay` | Rune of Decay | `rune_autumn` |
| `rune_will` | Rune of Will | `rune_greed` |
| `rune_binding` | Rune of Binding | `rune_winter` |
| `rune_mana` | Rune of Mana | `rune_mana` |
| `rune_cycle` | Rune of the Cycle | `rune_sloth` |
| `rune_dominion` | Rune of Dominion | `rune_pride` |
| `rune_manifestation` | Rune of Manifestation | `rune_air` |
| `rune_entropy` | Rune of Entropy | `rune_gluttony` |
| `rune_resonance` | Rune of Resonance | `rune_mana` |
| `rune_transcendence` | Rune of Transcendence | `rune_lust` |
| `rune_grove` | Rune of the Grove | `rune_spring` |
| `rune_sanguine` | Rune of the Sanguine | `rune_wrath` |
| `rune_rational` | Rune of the Rational | `rune_earth` |
| `rune_pact` | Rune of the Pact | `rune_winter` |
| `rune_void` | Rune of the Void | `rune_envy` |

`rune_sloth` is the only one of Botania's 16 rune textures not otherwise reused elsewhere in this table.

---

## Validation

- `done` — `botania:runes` tag populated with all 25 `mam:rune_*` items — [`data/botania/tags/items/runes.json`](../../src/main/resources/data/botania/tags/items/runes.json)
- `done` — Placeholder textures copied from Botania per the table above — `src/main/resources/assets/mam/textures/item/rune_*.png`
- `todo` — Replace placeholder art with MAM-original rune textures (hand-drawn, distinct per item)
