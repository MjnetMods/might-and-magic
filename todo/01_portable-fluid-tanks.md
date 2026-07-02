---
type: todo
last-updated: 2026-07-02
links: ["[[10_apothecary]]"]
---

# Portable Fluid-Filled Tanks

Need a general mechanic for tank-like blocks that can be picked up (broken, or silk-touched)
and carried while still holding their fluid — placing the item back down restores both the
block and its contents.

**Why:** the Apothecary now has a real fluid capability (recent `IFluidHandler` render work,
[[10_apothecary]]), but breaking one while it holds water/lava has no defined behavior yet.
Does the fluid vanish, drop as a bucket, or travel with the block item?

**Q:** Shared mechanic (any fluid-handler block preserves contents through break/place, reusable
beyond Apothecary) or an Apothecary-specific special case? Check Create's fluid tank block
(`/Users/mannil/mcmod/Create`) for a reference pickup/place implementation before deciding.

**Q:** For whatever *isn't* preserved through the item form — does breaking with the wrong tool
void the fluid, spill it as a source block in the world, or something else?
