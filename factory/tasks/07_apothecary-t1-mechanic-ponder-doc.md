---
type: task
gate: done
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

## Scene-warranted check (per [[ponder]] charter Directive)

This mechanic is a multi-step, tick-driven in-world sequence (fill → throw ×4 → throw catalyst →
drain/clear/eject), not a static recipe grid — a player looking at the recipe book sees the goblet
*shape* that crafts the Apothecary block itself, but nothing in vanilla UI explains that the
finished block then wants a fluid, four thrown petals, and a thrown seed, in that order, with the
seed specifically last. That sequencing (and the "catalyst goes last" rule) is exactly the kind of
thing Ponder exists for. Contrast with `05`'s reverted T2 scene: that one staged a plain shaped
crafting-table recipe with zero in-world component. This one clears the bar. Building a scene.

## Scene spec — `apothecary/brews_pure_daisy`

Registered against `VerdantMana.APOTHECARY` (same item `05`'s scene already targeted, so both
land on the same Ponder index entry). Single storyboard, mirrors PA-3
(`TestApothecary.apothecaryCraftsPureDaisyFromPetalsAndSeed`) beat-for-beat:

1. **Setup** — base plate (5×5 dirt floor), Apothecary block alone at local `(2,1,2)`, revealed
   before anything else (per `[[ponder-guide]]` §3 — thin/cross-model blocks must appear alone
   first; the Apothecary is a full block but keeping the convention).
2. **Overview text** — "Fill the Apothecary with a fluid, then throw in ingredients and a catalyst
   to craft." (`text_1`)
3. **Fill** — `showControls(...).rightClick().withItem(waterBucket)` at the block's top face, then
   `modifyBlockEntity(pos, ApothecaryBlockEntity.class, be -> be.getFluidTank().fill(...))` with
   1000mb water — this drives the *real* `ApothecaryBlockEntityRenderer` fluid-quad visual, not an
   approximation (see Finding below). Text: "Right-click with a water bucket to fill the basin."
   (`text_2`)
4. **Throw 4 petals** — `showControls(...).withItem(whitePetal)` (no rightClick — drop/throw
   gesture), then ×4: `createItemEntity` a white petal falling toward the block, idle, discard the
   entity, `modifyBlockEntity` to append to `be.getIngredients()` directly. This drives the real
   ingredient-orbit renderer live, item by item. Text: "Throw four matching petals in — they'll
   float above the fluid until the recipe is ready." (`text_3`)
5. **Throw the catalyst** — same throw/discard pattern with `Items.WHEAT_SEEDS`, then
   `modifyBlockEntity` drains the tank and clears `ingredients` (mirrors
   `ApothecaryBlockEntity.collideEntityItem`'s craft branch — Ponder scenes don't have a live
   `RecipeManager`, so the match outcome is staged, not actually computed), followed by
   `createItemEntity` popping a Pure Daisy stack up above the block and
   `scene.effects().indicateSuccess(pos)`. Text: "Throw the catalyst last — a seed — to trigger the
   craft. The fluid drains, the ingredients clear, and the Pure Daisy pops out." (`text_4`)

**Finding, not a guess:** the task brief assumed the live `ApothecaryBlockEntityRenderer` orbit/fluid
visual can't be reused in a Ponder scene and asked for an approximation instead. That assumption
doesn't hold — Create's own `FluidTankScenes.storage()` and `DrainScenes.emptying()`
(`Create/src/main/java/.../infrastructure/ponder/scenes/fluid/`) call
`scene.world().modifyBlockEntity(pos, SomeBlockEntity.class, be -> be.getTankInventory().fill(...))`
specifically *because* Ponder scenes render real registered `BlockEntityRenderer`s for block
entities present in the scene — that's how Create's own fluid-tank level and mixer-basin scenes
show correct fluid/particle visuals without inventing a parallel render path. Since
`ApothecaryBlockEntity` exposes `getFluidTank()` (a real `IFluidHandler`) and `getIngredients()`
(a live, mutable `List<ItemStack>`) as public getters already, the scene drives the actual
production renderer by mutating real block-entity state through its existing public API — no
mechanic code touched, no new renderer written, no approximation needed. Recommend updating the
task-brief assumption for future Ponder tasks involving block entities with custom renderers.

## Handoffs

- 2026-07-02 — task created (human), gate: ponder-doc
- 2026-07-02 — [[ponder]] built the scene end-to-end, gate: done. Scene-warranted check above
  concluded a scene is warranted (dynamic in-world sequence, not a static recipe grid) — full
  reasoning above, not skipped.

  Files changed:
  - `src/main/java/org/mjli/mam/ponder/MamPonderScenes.java` — added `apothecaryBrewsPureDaisy()`
    storyboard + registration against `VerdantMana.APOTHECARY` (same item `05`'s reverted scene
    targeted; this is now the first surviving storyboard on it)
  - `src/ponder/structure/assets/mam/ponder/apothecary/brews_pure_daisy.snbt` — new structure:
    5×5 dirt base plate, single Apothecary block at local `(2,1,2)`
  - `src/main/resources/assets/mam/lang/en_us.json` — added
    `mam.ponder.apothecary.brews_pure_daisy.{header,text_1,text_2,text_3,text_4}`
  - `test/10_apothecary-ponder-scene.md` — manual verification note (status: pending — not
    visually confirmed this session, no display/`runClient` capability available)
  - This task file — scene-warranted check, scene spec, and this handoff entry

  Verified: `./gradlew runData` — BUILD SUCCESSFUL, SNBT compiled cleanly to
  `src/generated/resources/assets/mam/ponder/apothecary/brews_pure_daisy.nbt`, no errors (also
  exercises `compileJava`, since datagen runs against compiled classes — clean).

  **Not verified — flagging explicitly per the charter's own Scope section:** did not run
  `./gradlew runClient` and cannot confirm the scene visually plays/reads correctly in-game (no
  display/long-running GUI capability in this environment, same constraint `05` hit). `test/
  10_apothecary-ponder-scene.md` is written so a human with a client can confirm it; `status:
  pending` until someone does.

  **Composed commit (not executed — no `git add`/`git commit` run):**
  ```
  git add src/main/java/org/mjli/mam/ponder/MamPonderScenes.java \
    src/ponder/structure/assets/mam/ponder/apothecary/brews_pure_daisy.snbt \
    src/main/resources/assets/mam/lang/en_us.json \
    test/10_apothecary-ponder-scene.md \
    factory/tasks/07_apothecary-t1-mechanic-ponder-doc.md

  git commit -m "feat: apothecary in-world brewing ponder scene

  Storyboard on mam:apothecary staging the T1 fill/throw/catalyst mechanic
  (water bucket -> 4x white petal -> seed catalyst -> Pure Daisy), mirroring
  TestApothecary PA-3 beat-for-beat. Drives the real ApothecaryBlockEntity
  fluid tank and ingredient list through their existing public getters so the
  scene renders with the actual ApothecaryBlockEntityRenderer output (fluid
  fill, ingredient orbit) instead of an invented approximation - see
  factory/tasks/07_apothecary-t1-mechanic-ponder-doc.md for why that's safe.
  Supersedes 05's reverted static-recipe scene as the real T1 tutorial."
  ```

- 2026-07-02 — human extended the storyboard registration to all four Apothecary tiers
  (`VerdantMana.APOTHECARY`, `INFUSED_APOTHECARY`, `SACRED_APOTHECARY`, `DESECRATED_APOTHECARY`) —
  `PonderSceneRegistrationHelper.forComponents(...)` takes varargs (confirmed against Create's own
  usage, `AllCreatePonderScenes.java`, e.g. `forComponents(AllBlocks.SHAFT,
  ANDESITE_ENCASED_SHAFT, BRASS_ENCASED_SHAFT)` — one scene per component family, not one scene
  per item). Correct here since the fill/throw/catalyst mechanic is identical across every tier per
  `magic/10_apothecary.md` ("Using it is no different from the base Apothecary... Tier gates the
  number of ingredient slots... not the school" — and this holds across all four, only slot count
  differs). The structure still shows a `mam:apothecary` (T1) block regardless of which tier item
  the player hovers — same precedent as Create's shaft family, one representative example teaches
  the shared mechanic, not a per-tier variant. Verified: `./gradlew compileJava runData` clean.

  ```bash
  git add src/main/java/org/mjli/mam/ponder/MamPonderScenes.java
  git commit -m "feat: apothecary ponder scene applies to all tiers"
  ```

  (Composed but never separately executed — confirmed by later review the four-tier
  registration is already present in the working tree and was folded into commit `a9e7682`
  directly; no second commit exists under this message. No functional impact, just a
  handoff-log/commit-count mismatch.)

- 2026-07-02 — reviewed (reviewer). No `CONFIRMED` findings — empty list, a positive outcome
  per the charter. Confirmed `getFluidTank()`/`getIngredients()` usage matches
  `ApothecaryBlockEntity`'s real public API; confirmed the sibling per-tier capacity fix (commit
  `3f061fc`) doesn't affect this scene (only touched the constructor and one internal
  comparison, getters unchanged); confirmed the 4 staged text beats match the shipped
  `text_1`–`text_4` lang keys in order; confirmed the fill→throw×4→catalyst→craft sequence
  matches `TestApothecary.apothecaryCraftsPureDaisyFromPetalsAndSeed` (PA-3) beat-for-beat. No
  bounce needed. Only open item: `test/10_apothecary-ponder-scene.md` is still `status: pending`
  — needs a human with a display to run `./gradlew runClient` and confirm the scene visually
  plays correctly; this is a pre-existing known gap (no runClient capability in this
  environment), not something this review found. Ready to move to `merge` once that visual
  confirmation lands.
