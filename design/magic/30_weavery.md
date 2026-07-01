---
type: design
status: wip
last-updated: 2026-06-30
links: "[[magic/00_energy]], [[20_verdant-path]], [[20_verdant-path-quipment]], [[magic/17_trinkets]], [[20_verdant-path-items]]"
---

# MAM — Weavery

Cross-school trinket/cloth merging station. Smithing-table style, tier-matched. Accepts trinkets and rings from any school — the merge mechanic doesn't care which school produced the input, only the tier. See [[magic/00_energy]] for the broader cross-school station model, and [[magic/17_trinkets]] / [[20_verdant-path-items]] for the full Ring / Trinket catalog (not duplicated here — this doc covers the station only).

---

## Crafting the Weavery

```
S S
W W   S = string   W = Livingwood (tier-matched)
W W
```

| Output          | W                  |
|-----------------|--------------------|
| Weavery         | Livingwood         |
| Infused Weavery | Infused Livingwood |
| Sacred Weavery  | Sacred Livingwood  |

2× string + 4× tier-matched wood per recipe.

**Implementation:** subclass `SmithingTableBlock`, override container title per tier, provide tier-matched model/texture. No GUI code needed — reuses `SmithingMenu` directly.

---

## Using the Weavery

Cloth armor pieces are augmented by merging in a crafted item — a Ring or a Trinket — via `smithing_transform`:

```
[ template ] [ cloth piece ] [ ring or trinket ]  →  augmented cloth piece
```

- Template slot: empty.
- Must use the correct tier Weavery (Mana cloth → Weavery, Infused → Infused Weavery, Sacred → Sacred Weavery).
- The merged item is **consumed**; its effect lives permanently in the cloth piece.
- A cloth piece can hold **two independent merges**: one Ring (mana storage, any slot) and one Trinket (passive effect, slot-specific). One of each, not multiples.
- Ring can be woven into any cloth slot (see [[magic/17_trinkets]]); Trinkets have fixed slot affinity (see [[20_verdant-path-items]]).

A full augmented cloth set gives 4× ring capacity and 4 passive effects simultaneously — a powerful but deeply invested endgame state.

---

## Status

| Item | Status |
|------|--------|
| Weavery crafting recipe shape (tier-matched) | ✅ decided |
| Merge mechanic (`smithing_transform`, template + cloth + ring/trinket) | ✅ decided |
| Two independent merge slots per cloth piece (ring + trinket) | ✅ decided |
| Ring/Trinket catalog | ⬜ design only — see [[magic/17_trinkets]] (Ring) and [[20_verdant-path-items]] (Trinket), recipes TBD |
| Implementation | ⬜ not started |
