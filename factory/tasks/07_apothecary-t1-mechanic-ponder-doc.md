---
type: task
gate: ponder-doc
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# Apothecary — In-World Mechanic Ponder Scene

Owned end-to-end by [[ponder]] (script + scene, per that charter). Goes straight to `ponder-doc`
with no site-doc/book-doc/impl/test siblings — T1's Apothecary is already fully shipped (site
docs, book entry, and the in-world mechanic all exist and are tested); this task only adds the
missing tutorial scene for it.

**Why this task exists:** `05_apothecary-t2-ponder-doc.md` scoped its scene around the T2 goblet
*crafting-table recipe* — a static shaped recipe already visible via the vanilla recipe book, not
something a Ponder tutorial adds value to. That was a task-briefing miss, not an execution one
(flagged by a human after the fact — see that task's own handoff log, which already recommended
this task as the real follow-up). The scene that's actually worth building is T1's **in-world,
dynamic** mechanic: fill with a fluid, throw ingredients in, throw a catalyst last, get an output.
That's non-obvious and exactly what Ponder is for.

## What to show

The concrete, already-implemented, already-tested example: brewing a Pure Daisy.
`ApothecaryBlockEntity` (`src/main/java/org/mjli/mam/block_entity/ApothecaryBlockEntity.java`) and
its GameTest coverage (`TestApothecary.apothecaryCraftsPureDaisyFromPetalsAndSeed`, PA-3, in
`src/main/java/org/mjli/mam/infrastructure/gametest/tests/TestApothecary.java`) are the ground
truth for exact behavior — read PA-3 before writing the scene, it's the literal sequence to stage:

1. A player fills the Apothecary with a water bucket (right-click with `Items.WATER_BUCKET`) —
   `be.interact(player)` fills the fluid tank.
2. Four white Mystical Petals are thrown in (as dropped `ItemEntity`s landing above the block) —
   they're ingested one at a time, floating/orbiting above the fluid (already has its own
   renderer, `ApothecaryBlockEntityRenderer.renderIngredients()` — the real in-game visual to
   match, not something to invent).
3. A seed (`Items.WHEAT_SEEDS`, matching `#c:seeds`) is thrown in last as the catalyst — this
   triggers matching against `mam:apothecary/pure_daisy` and crafts.
4. On success: fluid drains, ingredients clear, and a Pure Daisy item pops out above the block.

This is the recipe `mam:apothecary/pure_daisy` (`MamRecipeProvider.apothecaryPureDaisy()`) — 4×
white petal + a seed reagent → Pure Daisy, already registered and tested. Nothing here needs new
implementation; this is purely staging an existing, working mechanic as a tutorial.

## Guidance for the scene build

- Register against `VerdantMana.APOTHECARY` (T1's item) — same registration target `05`'s scene
  already used, so both scenes show up on the same item's Ponder index.
- The real ingredient-orbit visual already exists in-game (see `ApothecaryBlockEntityRenderer`) —
  a Ponder scene can't replicate a live block-entity renderer directly (Ponder scenes drive static
  `SceneBuilder`/SNBT block-state changes, not the mod's own renderer), so approximate the "petals
  floating above the fluid" beat with simple block/entity reveals timed to read as "ingredients
  going in," rather than trying to exactly reproduce the orbit animation. Don't block on making
  this pixel-perfect — the goal is teaching the *sequence* (fill → throw → throw → catalyst →
  output), not replicating the exact render.
- If something about staging this in Ponder's actual API turns out to be unclear or infeasible
  once you're reading `[[ponder-guide]]` and Ponder's real capabilities, flag it back rather than
  guessing at scope the way `05`'s task brief did — that's the mistake this task exists to not
  repeat.

## Handoffs

- 2026-07-02 — task created (human), gate: ponder-doc
