---
path: verdant
type: test
status: pass3-complete
last-updated: 2026-06-28
links: "[[20_verdant-path]], [[21_verdant-implementation-status]], [[ref/gametest-guide]]"
---

# MAM Automated Test Plan

Last updated: 2026-06-28

## Infrastructure

- **Framework**: NeoForge GameTest (`@GameTest` / `@GameTestHolder`)
- **Run**: `./gradlew runGameTestServer` or `/test runall` in-game
- **Test classes**: `src/main/java/org/mjli/mam/infrastructure/gametest/tests/`
- **Structure templates (SNBT)**: `src/gametest/structure/data/mam/structure/<folder>/<name>.snbt`
- **Unit tests**: `src/test/java/org/mjli/mam/` (plain JUnit, no MC bootstrap)

## Already covered

### VerdantPath — `TestVerdantPath`

| Test | What it verifies |
|------|-----------------|
| `verdant_path.pure_daisy_converts_log` | Oak log → livingwood_log within 200 ticks |
| `verdant_path.pure_daisy_converts_stone` | Stone → living_rock within 200 ticks |

### Pass 3 — `TestVerdantPath` continued (4 tests)

| ID | Test | What it verifies |
|----|------|-----------------|
| PD-1 | `pureDaisyConvertsAnyLogVariant` | Birch log matches `minecraft:logs` tag → converts to livingwood_log |
| PD-2 | `pureDaisyIgnoresNonMatchingBlock` | Dirt adjacent to Pure Daisy is NOT converted after 300 ticks |
| PD-4 | `pureDaisyNoDoubleConversion` | Livingwood log adjacent to Pure Daisy is NOT re-converted after 300 ticks |
| PD-5 | `pureDaisyTimerResetsOnTargetRemoved` | Removing stone at tick 100, replacing at tick 101 → daisy still converts the fresh block |

### Unit tests — `ManaPoolTest` (12 tests)

| Test | What it verifies |
|------|-----------------|
| `comparatorLevel_emptyPool_isZero` | Signal 0 at 0 mana |
| `comparatorLevel_fullPool_isFifteen` | Signal 15 at max mana |
| `comparatorLevel_anyMana_isAtLeastOne` | Even 1 mana → signal ≥ 1 |
| `comparatorLevel_halfFull_isSevenOrEight` | 500k/1M → signal 7 or 8 |
| `comparatorLevel_quarterFull_isThreeOrFour` | 250k/1M → signal 3 or 4 |
| `comparatorLevel_neverExceedsFifteen` | No overflow above 15 |
| `manaClamping_cannotExceedMax` | Overflow clamp |
| `manaClamping_cannotGoBelowZero` | Underflow clamp |
| `manaClamping_normalAddition` | Normal add path |
| `nbt_manaAndOutputting_roundtrip` | mana + outputting survive CompoundTag round-trip |
| `nbt_color_presentRoundtrip` | DyeColor present → survives round-trip |
| `nbt_color_absentRoundtrip` | Color absent → loads as Optional.empty() |

### Unit tests — `ManaNetworkHandlerTest` (4 tests)

Covers GF-2 and GF-3.

| Test | What it verifies |
|------|-----------------|
| `queryClosest_returnsNull_whenMapEmpty` | No NPE / null when registry empty |
| `queryClosest_returnsNull_whenOnlyPoolIsOutOfRadius` | Pool beyond radius → not returned |
| `queryClosest_prefersNearer_whenMultiplePools` | Two in-range pools → closer wins |
| `queryClosest_excludesFarPool_whenOnlyNearIsInRadius` | One in, one out → only near returned |

### Pass 1 — `TestVerdantFlowers` (14 tests, all green as of 2026-06-28)

Shared structure: `verdant_flowers/small_platform` (7×5×7 dirt floor). All tests sync, no player.

| ID | Test | What it verifies |
|----|------|-----------------|
| MF-1 | `bonemealFlowerGrowsTall` | `performBonemeal` places TallFlower LOWER at pos, UPPER at pos+1 |
| MF-2 | `flowerBonemealBlockedWhenObstructed` | `isValidBonemealTarget` → false when block above is occupied |
| MF-3 | `flowerDropSelfOnBreak` | Breaking a single flower drops the flower item |
| TF-4 | `tallFlowerNotBonemealable` | `isValidBonemealTarget` always false on TallMysticalFlowerBlock |
| BP-1 | `bonemealBuriedPetalGrowsFlower` | `performBonemeal` replaces buried petal with matching-color flower |
| BP-2 | `buriedPetalBonemealBlockedWhenObstructed` | `isValidBonemealTarget` → false when block above is occupied |
| BP-3 | `buriedPetalColorMatchesFlower` | White/red/blue petals each grow their exact matching-color flower |
| BP-4 | `buriedPetalDropOnBreak` | Breaking a buried petal drops the petal item, not the block |
| FP-5 | `flowerCanSurviveOnDirt` | `canSurvive` true for mystical flower above dirt |
| MM-1 | `mushroomSurvivesOnSolidBlock` | `canSurvive` true when below is stone |
| MM-2 | `mushroomSurvivesOnMushroomGrowBlock` | `canSurvive` true when below is mycelium (`MUSHROOM_GROW_BLOCK` tag) |
| MM-3 | `mushroomCannotSurviveOnAir` | `canSurvive` false when below is air |
| TAG-1 | `all16FlowersInMysticalFlowersTag` | `mam:mystical_flowers` contains all 16 DyeColor flowers |
| TAG-2 | `allFlowersInMinecraftSmallFlowersTag` | All MAM flowers appear in `minecraft:small_flowers` |

**Side fix discovered during Pass 1:** tag directories were at `data/<ns>/tags/blocks/` (plural) — the pre-1.20.5 path. MC 1.21 uses `data/<ns>/tags/block/` (singular). Files at the wrong path are silently ignored by the tag loader. Renamed all tag dirs; also fixed `FloralPowderItem.useOn` which was silently returning FAIL in production.

### Pass 2 — `TestVerdantFlowers` continued (7 tests, mock player)

| ID | Test | What it verifies |
|----|------|-----------------|
| TF-1 | `tallFlowerBreakLowerDrops2Petals` | `destroyBlock(LOWER, drops=true)` → exactly 2 petals |
| TF-2 | `tallFlowerBreakSuppressedDropsNothing` | `destroyBlock(LOWER, drops=false)` → 0 petals (no-drop path) |
| TF-3 | `tallFlowerShearDrops2PetalsAndRemoves` | Shears in MAIN_HAND → whole plant removed, 2 petals dropped |
| BP-5 | `petalItemPlantsBuriedPetal` | Right-clicking petal item on dirt places matching BuriedPetalBlock |
| FP-1 | `floralPowderScattersFlowersOnDirt` | useOn dirt → 5–7 flowers in the 7×7 layer |
| FP-2 | `floralPowderConsumesItem` | Stack of 3 → 2 remaining after successful use |
| FP-3 | `floralPowderBlockedOnStoneFloor` | Stone floor → 0 flowers placed |

### Pass 2 — `TestManaPool` (3 GameTests)

| ID | Test | What it verifies |
|----|------|-----------------|
| MP-1 | `manaPoolComparatorSignalInGame` | `getAnalogOutputSignal` returns 7 at 500k mana in-game |
| MP-2 | `manaPoolRegistersOnPlace` | Pool BE is in ManaNetworkHandler set after first server tick |
| MP-3 | `manaPoolDeregistersOnRemove` | Removing pool block removes it from ManaNetworkHandler synchronously |

### Pass 2 — `TestApothecary` (2 GameTests)

| ID | Test | What it verifies |
|----|------|-----------------|
| PA-1 | `apothecaryInteractEmptyHandPasses` | `interact()` with empty hand → `PASS` |
| PA-2 | `apothecaryNbtRoundtrip` | `FluidState` + petal list survive `saveCustomOnly`/`loadCustomOnly` |

### Pass 4 — `TestGeneratingFlowers` (6 GameTests)

Shared structure: `verdant_flowers/small_platform`. No pool placed — flowers buffer mana in their own BE when unbound.

| ID | Test | What it verifies |
|----|------|-----------------|
| GF-1 | `solarbudGeneratesManaInDaylight` | Solarbud accumulates mana at time 6000 with open sky |
| GF-2 | `solarbudNoManaWhenSkyBlocked` | Mana freezes for 15 ticks after stone is placed above the flower |
| GF-3 | `emberwortBurnsCoalAndGeneratesMana` | Coal ItemEntity dropped at flower → mana > 0 after 40 ticks |
| GF-4 | `emberwortIgnoresNonFuelItems` | Dirt ItemEntity dropped near flower → mana remains 0 |
| GF-5 | `dewpetalGeneratesManaAdjacentToWater` | Water placed north of flower → mana > 0 after 20 ticks |
| GF-6 | `dewpetalNoManaWithoutWater` | No water, no rain → mana remains 0 |

### Pass 2 — `TestRecipes` (3 GameTests)

| ID | Test | What it verifies |
|----|------|-----------------|
| RC-1 | `flowerToPetalsYields4` | Flower → 4 petals (spot-check white/red/blue via RecipeManager) |
| RC-2 | `petalToDyeYields1` | Petal → 1 matching dye (spot-check white/red/blue) |
| RC-3 | `floralPowderRecipeCorrect` | Floral powder recipe exists, yields 1, requires bonemeal |

---

## Backlog

Complexity legend: **low** = sync call + assert, no player; **med** = needs mock player or tick wait; **high** = multi-block/event chain or blocked on impl.

### Pure Daisy / PureDaisyBlockEntity — ✅ all done (Pass 3)

Won't implement: **PD-3** (chunk unload/timer persistence — no chunk-unload trigger in GameTest).

### GeneratingFlower

All 6 GF tests implemented in Pass 4. The original pool-push integration test (flower → pool) is covered implicitly: GF-3/5 verify mana accumulates in the flower buffer, and the pool-push path (`emptyManaIntoCollector`) is covered by MP-2/3 and the `ManaNetworkHandlerTest` unit tests.

### ApothecaryBlockEntity

| ID | Test name | What it verifies | Complexity | Notes |
|----|-----------|-----------------|------------|-------|
| PA-3 | `apothecary_crafts_recipe_output` | Water + petals + reagent → correct item output | high | **Blocked** — crafting logic is a TODO stub |

Won't implement: **FP-4** (nether ultraWarm check — no dimension override in GameTest).

---

## Summary

### Done

| Category | Tests done |
|----------|-----------|
| MysticalFlowerBlock | MF-1/2/3 |
| TallMysticalFlowerBlock | TF-1/2/3/4 |
| BuriedPetalBlock | BP-1/2/3/4/5 |
| FloralPowderItem | FP-1/2/3/5 |
| MysticalMushroomBlock | MM-1/2/3 |
| Tags | TAG-1/2 |
| Pure Daisy (integration + edge cases) | 2 integration + PD-1/2/4/5 |
| ManaPool (GameTest) | MP-1/2/3 |
| Apothecary (GameTest) | PA-1/2 |
| Recipes (GameTest) | RC-1/2/3 |
| ManaPool (unit) | 12 (comparator math, NBT, clamp) |
| ManaNetworkHandler (unit) | 4 (GF-2/3 variants) |
| GeneratingFlowers (GameTest) | GF-1/2/3/4/5/6 |
| **Total** | **~57** |

### Remaining backlog

| Category | Tests left | Med | High/Blocked |
|----------|-----------|-----|------|
| Pure Daisy | ✅ 0 | — | — |
| GeneratingFlower | ✅ 0 | — | — |
| Apothecary | 1 | 0 | 1 (blocked on impl) |
| **Total** | **1** | **0** | **1** |

---

## Not testable (client-only or infeasible)

- `BuriedPetalBlock` `RenderShape.INVISIBLE` — client render, not server
- `VerdantPathGuideItem.use()` — opens Patchouli book GUI, client-only
- Block model / blockstate correctness — datagen output; verify visually
- Floral powder in nether (`ultraWarm`) — no dimension override in GameTest (FP-4)
- Chunk unload / timer persistence (PD-3) — no chunk-unload trigger in GameTest

## Implementation order

1. ✅ **Pass 1 — low complexity, `TestVerdantFlowers`**: MF-1/2/3, TF-4, BP-1/2/3/4, FP-5, MM-1/2/3, TAG-1/2 (14 tests)
2. ✅ **Pass 2 — medium, mock player**: TF-1/2/3, BP-5, FP-1/2/3, MP-1/2/3, PA-1/2, RC-1/2/3 + unit tests for GF-2/3 and expanded ManaPool (33 tests)
3. **Pass 3 — remaining Pure Daisy edge cases**: PD-1/2/4/5 (4 med tests, no blockers)
4. **Pass 4 — when implementations land**: GF-1 (generating flower tick), PA-3 (apothecary crafting)
