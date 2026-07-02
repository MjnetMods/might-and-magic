---
type: todo
last-updated: 2026-07-02
links: ["[[23_verdant-generating-flowers]]"]
---

# Daybloom Sky-Blocked GameTest Failure

`daybloomNoManaWhenSkyBlocked` (GF-2, in [`TestGeneratingFlowers`](../src/main/java/org/mjli/mam/infrastructure/gametest/tests/TestGeneratingFlowers.java)) is failing as a required test:

```
[Server thread/INFO] [minecraft/GameTestServer]: ========= 66 GAME TESTS COMPLETE IN 1.186 s ======================
[Server thread/INFO] [minecraft/GameTestServer]: 1 required tests failed :(
[Server thread/INFO] [minecraft/GameTestServer]:    - daybloomnomanawhenskyblocked
```

The test places a Daybloom, lets it generate for 5 ticks, places Stone directly above it, waits 2 ticks, snapshots mana, waits 15 more ticks, and asserts mana did not increase. It's currently failing, meaning [`DaybloomBlockEntity.tickFlower()`](../src/main/java/org/mjli/mam/block_entity/flower/DaybloomBlockEntity.java) is still adding mana after the sky is blocked.

**Context:** the test's own comments already show one prior race-condition fix attempt ("Sample baseline 2 ticks AFTER stone is placed... to avoid a 1-tick race between the game-test callback and the BE tick ordering"). This suggests either that fix wasn't sufficient, or `level.canSeeSky(worldPosition)` isn't updating/evaluating the way the test assumes (heightmap propagation timing, or the GameTest structure's own bounding volume interfering with sky visibility).

**Why it matters:** this is a required test — a red required GameTest blocks trusting the rest of the suite (65 others passing) and blocks any release-hygiene check that runs the full suite.

**Q:** Is this a real behavior bug (Daybloom actually keeps generating with blocked sky) or a test-timing flakiness issue (assertion fires before the world state settles)?

**Note (2026-07-02):** ran the full suite again (unrelated change — Daybloom lifetime-tick rescale, see [[23_verdant-generating-flowers]]) and GF-2 passed, 67/67 green. One clean run doesn't rule out flakiness; leaving this open until it's been observed passing consistently across several runs, or the root cause is actually found.
