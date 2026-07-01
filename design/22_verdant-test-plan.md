---
path: verdant
type: test
status: wip
last-updated: 2026-06-30
links: ["[[21_verdant-implementation-status]]", "[[12_magic-test-plan]]", "[[ref/gametest-guide]]"]
---

# Verdant Path — Test Plan

Verdant-specific tests: flowers, petals, generating flowers, world gen, flower recipes. Magic infrastructure tests (Pure Daisy, Mana Pool, Apothecary, Living Rock/Wood) live in [[12_magic-test-plan]].

- **Framework**: NeoForge GameTest (`@GameTest` / `@GameTestHolder`)
- **Run**: `./gradlew runGameTestServer` or `/test runall` in-game
- **Test classes**: `src/main/java/org/mjli/mam/infrastructure/gametest/tests/`
- **Structure templates (SNBT)**: `src/gametest/structure/data/mam/structure/<folder>/<name>.snbt`

---

## Covered

### Pass 1 — `TestVerdantFlowers` (14 tests)

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

**Side fix:** tag directories were at `data/<ns>/tags/blocks/` (plural) — the pre-1.20.5 path. MC 1.21 uses `data/<ns>/tags/block/` (singular). Renamed all tag dirs; also fixed `FloralPowderItem.useOn` which was silently returning FAIL.

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

### Pass 5 — `TestWorldGen` (3 GameTests)

Shared structure: `worldgen/large_platform` (40×5×40 dirt floor, center at helper (19,2,19)).
Calls `ConfiguredFeature.place()` directly — bypasses placed_feature modifiers.

| ID | Test | What it verifies |
|----|------|-----------------|
| WG-1 | `allWorldgenFeaturesRegistered` | All 18 `configured_feature` ResourceLocations resolve in the registry |
| WG-2 | `mysticalFlowerPatchPlacesBlock` | `white_mystical_flower_patch` places ≥1 `MysticalFlowerBlock` (5 invocations) |
| WG-3 | `mysticalMushroomFeaturePlacesBlock` | `mystical_mushrooms` places ≥1 `MysticalMushroomBlock` (5 invocations) |

### `TestRecipes` — flower recipes (3 GameTests)

| ID | Test | What it verifies |
|----|------|-----------------|
| RC-1 | `flowerToPetalsYields4` | Flower → 4 petals (spot-check white/red/blue via RecipeManager) |
| RC-2 | `petalToDyeYields1` | Petal → 1 matching dye (spot-check white/red/blue) |
| RC-3 | `floralPowderRecipeCorrect` | Floral powder recipe exists, yields 1, requires bonemeal |

---

## Backlog

| ID | Test | Complexity | Notes |
|----|------|------------|-------|
| FP-4 | `floralPowderBlockedInNether` | — | Won't implement — no dimension override in GameTest |

---

## Summary

| Category | Tests |
|----------|-------|
| MysticalFlowerBlock | MF-1/2/3 |
| TallMysticalFlowerBlock | TF-1/2/3/4 |
| BuriedPetalBlock | BP-1/2/3/4/5 |
| FloralPowderItem | FP-1/2/3/5 |
| MysticalMushroomBlock | MM-1/2/3 |
| Tags | TAG-1/2 |
| GeneratingFlowers | GF-1/2/3/4/5/6 |
| WorldGen | WG-1/2/3 |
| Flower recipes | RC-1/2/3 |
| **Total** | **33** |
