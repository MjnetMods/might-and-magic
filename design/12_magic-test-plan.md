---
path: magic
type: test
status: wip
last-updated: 2026-07-01
links: "[[11_magic-implementation-status]], [[ref/gametest-guide]]"
---

# Magic — Test Plan

Tests for shared magic infrastructure: Pure Daisy, Living Rock/Wood, Mana Pool, Apothecary.

- **Framework**: NeoForge GameTest + plain JUnit
- **Run**: `./gradlew runGameTestServer` or `/test runall` in-game
- **Test classes**: `src/main/java/org/mjli/mam/infrastructure/gametest/tests/`
- **Unit tests**: `src/test/java/org/mjli/mam/`

---

## Covered

### `TestVerdantPath` — Pure Daisy integration (2 GameTests)

| Test | What it verifies |
|------|-----------------|
| `verdant_path.pure_daisy_converts_log` | Oak log → livingwood_log within 200 ticks |
| `verdant_path.pure_daisy_converts_stone` | Stone → living_rock within 200 ticks |

### Pure Daisy edge cases (4 GameTests)

| ID | Test | What it verifies |
|----|------|-----------------|
| PD-1 | `pureDaisyConvertsAnyLogVariant` | Birch log matches `minecraft:logs` tag → converts to livingwood_log |
| PD-2 | `pureDaisyIgnoresNonMatchingBlock` | Dirt adjacent to Pure Daisy is NOT converted after 300 ticks |
| PD-4 | `pureDaisyNoDoubleConversion` | Livingwood log adjacent to Pure Daisy is NOT re-converted after 300 ticks |
| PD-5 | `pureDaisyTimerResetsOnTargetRemoved` | Removing stone at tick 100, replacing at tick 101 → daisy still converts the fresh block |

Won't implement: **PD-3** (chunk unload/timer persistence — no chunk-unload trigger in GameTest).

### Unit tests — `ManaPoolTest` (15 tests)

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
| `nbt_energyType_roundtripsNox` | NOX written → reads back as NOX |
| `nbt_energyType_roundtripsMana` | MANA written → reads back as MANA |
| `nbt_energyType_absentKeepsConstructorDefault` | energyType key absent → keeps the value set at construction (no silent reset) |

### Unit tests — `EnergyNetworkHandlerTest` (4 tests)

| Test | What it verifies |
|------|-----------------|
| `queryClosest_returnsNull_whenMapEmpty` | No NPE / null when registry empty |
| `queryClosest_returnsNull_whenOnlyPoolIsOutOfRadius` | Pool beyond radius → not returned |
| `queryClosest_prefersNearer_whenMultiplePools` | Two in-range pools → closer wins |
| `queryClosest_excludesFarPool_whenOnlyNearIsInRadius` | One in, one out → only near returned |

### `TestManaPool` (9 GameTests)

| ID | Test | What it verifies |
|----|------|-----------------|
| MP-1 | `manaPoolComparatorSignalInGame` | `getAnalogOutputSignal` returns 7 at 500k mana in-game |
| MP-2 | `manaPoolRegistersOnPlace` | Pool BE is in EnergyNetworkHandler set after first server tick |
| MP-3 | `manaPoolDeregistersOnRemove` | Removing pool block removes it from EnergyNetworkHandler synchronously |
| MP-4 | `poolTierCapacityIsCorrect` | mana_pool/infused/sacred/desecrated report max capacity 1M/4M/16M/16M |
| MP-5 | `poolTierEnergyTypeIsCorrect` | mana_pool/infused/sacred report MANA; desecrated reports NOX |
| MP-6 | `manaPoolTaintsToNoxOnContact` | T1 pool: Nox entering taints all stored Mana to Nox at 1:1, instant |
| MP-7 | `infusedManaPoolTaintsToNoxOnContact` | T2 pool: same taint behavior as MP-6 |
| MP-8 | `sacredManaPoolRejectsNox` | T3 aligned (Mana): Nox contact destroys equal Mana, no Nox stored |
| MP-9 | `desecratedManaPoolRejectsMana` | T3 aligned (Nox): Mana contact destroys equal Nox, no Mana stored |

### `TestApothecary` (2 GameTests)

| ID | Test | What it verifies |
|----|------|-----------------|
| PA-1 | `apothecaryInteractEmptyHandPasses` | `interact()` with empty hand → `PASS` |
| PA-2 | `apothecaryNbtRoundtrip` | `FluidState` + petal list survive `saveCustomOnly`/`loadCustomOnly` |

### `TestVerdantBlocks` — loot tables & harvest (6 GameTests)

| ID | Test | What it verifies |
|----|------|-----------------|
| LT-1 | `livingRockDropsSelf` | `living_rock` loot table (dropSelf) yields the block item on break |
| LT-2 | `livingRockRequiresPickaxe` | `requiresCorrectToolForDrops()` — bare hand → false, wooden pickaxe → true |
| LT-3 | `mushroomDropsSelf` | `white_mystical_mushroom` loot table (dropSelf) yields the mushroom item |
| LT-4 | `livingwoodLogDropsSelf` | `livingwood_log` loot table (dropSelf) yields the log item |
| LT-5 | `tieredLivingRockDropsSelf` | All 9 infused/sacred/desecrated living rock variants drop themselves |
| LT-6 | `tieredLivingwoodDropsSelf` | All 9 infused/sacred/desecrated livingwood variants drop themselves |

### `TestRecipes` — crafting chain & furniture (5 GameTests)

| ID | Test | What it verifies |
|----|------|-----------------|
| RC-4 | `livingRockCraftingChain` | living_rock_polished → 4, living_rock_brick → 4 |
| RC-5 | `livingwoodPlanksFromLog` | livingwood_log → 4 planks |
| RC-6 | `livingwoodFurnitureRecipes` | planks stairs→4, slab→6, fence→3, fence_gate→1 |
| RC-7 | `livingRockFurnitureRecipes` | stairs→4, slab→6, wall→6 for all 3 rock variants (9 checks) |
| RC-8 | `manaPoolCraftingRecipes` | mana_pool/infused/sacred/desecrated U-shape recipes all exist, yield 1 |

---

## Backlog

| ID | Test | Complexity | Notes |
|----|------|------------|-------|
| PA-3 | `apothecary_crafts_recipe_output` | high | **Blocked** — crafting logic is a TODO stub |
| — | Tablet ↔ Pool load/unload | medium | No automated test yet for `ManaPoolBlockEntity.interact()`/`transferChargingItem()` (insert, retrieve, bidirectional transfer, T1/T2 convert vs T3 aligned-drain). Manually verified only. Design: [[magic/17_trinkets]] § Loading / unloading mana |

---

## Summary

| Category | Tests |
|----------|-------|
| Pure Daisy integration + edge cases | 6 |
| ManaPool (unit) | 15 |
| EnergyNetworkHandler (unit) | 4 |
| ManaPool (GameTest) | 9 |
| Apothecary (GameTest) | 2 |
| Loot tables / harvest (GameTest) | 6 |
| Recipes — crafting chain + furniture (GameTest) | 5 |
| **Total** | **47** |
