---
type: design
status: wip
last-updated: 2026-06-30
links: "[[magic/00_energy]], [[01_rituals]], [[30_summoning-path]], [[40_sanguine-path]]"
---

# MAM — Cross-Cutting Items

Items used across multiple schools or as shared crafting ingredients. Not school-exclusive.

---

## Butcher's Knife

A tool used to extract **Lard** from pigs. Right-click a pig to harvest lard (does not kill — extracts a quantity, pig survives). School-neutral; any player can craft and use it.

| Property | Value |
|----------|-------|
| Item key | `mam:butchers_knife` |
| Durability | TBD |
| Recipe | TBD (iron + handle materials) |

---

## Lard

Raw animal fat extracted from pigs using the Butcher's Knife.

| Property | Value |
|----------|-------|
| Item key | `mam:lard` |
| Source | Butcher's Knife on pig |
| Uses | Candles, Basic Chalk |

---

## Candles

Crafted from Lard. Decorative light source — thematically ritual-adjacent but no functional mechanic for now.

| Property | Value |
|----------|-------|
| Item key | `mam:candle` (or variant) |
| Recipe | Lard → Candle (shape TBD) |
| Uses | Decorative light source |

---

## Chalk (raw material — three sizes)

One raw chalk material in three sizes, following the standard Minecraft nugget/ingot/block pattern. Size determines which pool tier it interacts with.

| Item | Item key | Crafted from | Drop into | Pool cost | Output |
|------|----------|-------------|-----------|-----------|--------|
| Chalk Lump | `mam:chalk_lump` | Lard + Bone Meal | Any pool (≥75% T1 capacity) | Chalk (16 colors) |
| Chalk Ingot | `mam:chalk_ingot` | 9× Chalk Lump | Any pool (≥75% T2 capacity) | Infused Chalk (16 colors) |
| Chalk Block | `mam:chalk_block` | 9× Chalk Ingot | Any pool (≥75% T3 capacity) | Desecrated Chalk (16 colors) |

Chalk size determines output tier — pool type doesn't matter, only that it holds sufficient energy. Standard compression applies (9 lumps → 1 ingot, 9 ingots → 1 block), reversible. See `01_rituals.md` for the full chain.

---

## Blank Rune

The Altar activation trigger. Thrown last into a loaded Altar to fire recipe matching. Not tiered — works for all Altar tiers.

| Property | Value |
|----------|-------|
| Item key | `mam:blank_rune` |
| Recipe | 1× Living Rock + 1× Living Rock (1×2 shape) → 2× Blank Rune |
| Uses | Altar trigger |

---

## School Tools (stubs — see school docs)

| Item | School | Doc |
|------|--------|-----|
| Scythe | Summoning — soul reaping, primary tool | `[[30_summoning-path]]` |
| Anathema | Sanguine — self-harm / sacrifice instrument | `[[40_sanguine-path]]` |

---

## Open Questions

- [ ] Butcher's Knife durability and recipe
- [ ] Candle uses in rituals — decoration only, or functional ingredient?
- [ ] Basic Chalk recipe shape (shapeless? shaped?)

---

## Status

| Item | Status |
|------|--------|
| Butcher's Knife (lard extraction) | ✅ decided |
| Lard (pig-extracted ingredient) | ✅ decided |
| Candles (from lard) | ✅ decided |
| Basic Chalk (lard + bone meal) | ✅ decided |
| Scythe (Summoning) | ⬜ stub — see summoning doc |
| Anathema (Sanguine) | ⬜ stub — see sanguine doc |
| Item registration | ⬜ not started |
