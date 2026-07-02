---
type: todo
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]", "[[05_summoning-souls-mechanic]]"]
---

# Lava-Filled Apothecary Burns Items (Souls Excepted)

An Apothecary filled with lava should burn/destroy dropped `ItemEntity`s that fall into it, like vanilla lava does. Souls are the exception: they don't burn, they float on the lava's surface instead — this is how "summoning" recipes get their soul ingredient into the Apothecary.

**Why:** currently `collideEntityItem()` in `design/magic/10_apothecary.md`'s described behavior treats every colliding `ItemEntity` as a potential recipe ingredient, with no burn/destroy path. Adding vanilla-like lava burning needs souls carved out as a special case so summoning recipes still work once lava is the medium.

**Depends on:** [[05_summoning-souls-mechanic]] — souls need to exist as an entity/item type before "souls float instead of burning" is implementable.

**Q:** Does this apply to all Apothecary fluids that happen to be lava, or is it specifically the Sanguine/Summoning recipe context? Also needs to reconcile with the existing ingredient-collection logic — burn-if-not-ingredient, or burn-if-not-soul-and-not-matched-recipe?
