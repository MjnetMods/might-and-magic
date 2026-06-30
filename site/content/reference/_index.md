---
title: "Modpack Reference"
date: 2026-06-29
draft: false
---

Full item and recipe tables for modpack makers and data-driven integration. Not linked from the main nav — share the URL directly.

This page reflects the **currently implemented** state of the mod. Items and recipes marked *(planned)* are designed but not yet in code.

---

## Mod Info

| Property | Value |
|---|---|
| Mod ID | `mam` |
| Group | `org.mjli.mam` |
| Loader | NeoForge 21.1.234 |
| Minecraft | 1.21.1 |

---

## Verdant Path — Items

| Item ID | Name | Notes |
|---|---|---|
| `mam:white_mystical_flower` … `mam:black_mystical_flower` | Mystical Flowers (16 colors) | Drops 1 petal; bonemeal → tall variant |
| `mam:white_tall_mystical_flower` … `mam:black_tall_mystical_flower` | Tall Mystical Flowers (16 colors) | Drops 2 petals (lower half only) |
| `mam:white_petal` … `mam:black_petal` | Petals (16 colors) | Item; right-click ground → Buried Petal |
| `mam:white_mystical_mushroom` … `mam:black_mystical_mushroom` | Mystical Mushrooms (16 colors) | Decorative; ingredient for Grimoire |
| `mam:floral_powder` | Floral Powder | Thrown; scatters random mystical flowers |
| `mam:living_rock` / `_polished` / `_brick` | Living Rock variants | Crafting material |
| `mam:living_rock_stairs` / `_slab` / `_wall` | Living Rock furniture (×3 variants) | Building block |
| `mam:livingwood_log` | Livingwood Log | Crafting material |
| `mam:livingwood_planks` | Livingwood Planks | Crafting material |
| `mam:livingwood_planks_stairs` / `_slab` / `_fence` / `_fence_gate` | Livingwood Planks furniture | Building block |
| `mam:guide` | Grimoire | Patchouli guidebook; Book ID `mam:guide` |

---

## Verdant Path — Blocks

| Block ID | Name | Block Entity |
|---|---|---|
| `mam:mystical_flower_*` | Mystical Flowers (16 colors) | No |
| `mam:tall_mystical_flower_*` | Tall Mystical Flowers (16 colors) | No |
| `mam:buried_petal_*` | Buried Petals (16 colors, internal) | No |
| `mam:pure_daisy` | Pure Daisy | Yes — `mam:pure_daisy` |
| `mam:living_rock` / `_polished` / `_brick` | Living Rock variants | No |
| `mam:living_rock_stairs` / `_slab` / `_wall` | Living Rock furniture (×3 variants, 9 blocks total) | No |
| `mam:livingwood_log` | Livingwood Log | No |
| `mam:livingwood_planks_stairs` / `_slab` / `_fence` / `_fence_gate` | Livingwood Planks furniture | No |
| `mam:mana_pool` | Mana Pool | Yes — `mam:mana_pool` |
| `mam:petal_apothecary` | Petal Apothecary | Yes — `mam:petal_apothecary` |

---

## Verdant Path — Tags

| Tag | Type | Contents |
|---|---|---|
| `botania:mystical_flowers` | block | All 16 `mam:*_mystical_flower` blocks |
| `botania:double_mystical_flowers` | block | All 16 `mam:*_tall_mystical_flower` blocks |
| `botania:special_flowers` | block | `mam:pure_daisy` |
| `minecraft:small_flowers` | block | All 16 `mam:*_mystical_flower` blocks |
| `mam:living_rock` | block | `mam:living_rock`, `_polished`, `_brick` |
| `mam:mystical_petals` | item | All 16 `mam:*_petal` items |
| `mam:mystical_flowers` | item | All 16 `mam:*_mystical_flower` items |
| `mam:mystical_mushrooms` | item | All 16 `mam:*_mystical_mushroom` items |

---

## Verdant Path — Recipes

### Crafting (Shaped / Shapeless)

| Recipe ID | Type | Input | Output |
|---|---|---|---|
| `mam:guide` | Shapeless | 1 petal + 1 mystical flower + 1 mushroom + 1 book (any color) | 1 Grimoire |
| `mam:<color>_mystical_flower_to_petals` (×16) | Shapeless | 1 mystical flower | 4 petals (matching color) |
| `mam:<color>_petal_to_dye` (×16) | Shapeless | 1 petal | 1 dye (matching color) |
| `mam:living_rock_polished` | Shaped 2×2 | 4 living_rock | 4 living_rock_polished |
| `mam:living_rock_brick` | Shaped 2×2 | 4 living_rock_polished | 4 living_rock_brick |
| `mam:living_rock_stairs` / `_polished_stairs` / `_brick_stairs` | Shaped stair | 6 source block | 4 stairs |
| `mam:living_rock_slab` / `_polished_slab` / `_brick_slab` | Shaped row | 3 source block | 6 slabs |
| `mam:living_rock_wall` / `_polished_wall` / `_brick_wall` | Shaped 2 rows | 6 source block | 6 walls |
| `mam:mana_pool` | Shaped U (3×3) | 8 living_rock_brick | 1 mana_pool |
| `mam:petal_apothecary` | Shaped goblet | living_rock + polished | 1 petal_apothecary |
| `mam:livingwood_planks` | Shapeless | 1 livingwood_log | 4 livingwood_planks |
| `mam:livingwood_planks_stairs` | Shaped stair | 6 livingwood_planks | 4 stairs |
| `mam:livingwood_planks_slab` | Shaped row | 3 livingwood_planks | 6 slabs |
| `mam:livingwood_planks_fence` | Shaped 2×3 | planks + sticks | 3 fence |
| `mam:livingwood_planks_fence_gate` | Shaped 2×3 | planks + sticks | 1 fence gate |

### Pure Daisy Conversions

| Recipe ID | Input Block | Output Block | Duration |
|---|---|---|---|
| `mam:living_rock_from_stone` | `minecraft:stone` | `mam:living_rock` | 200 ticks |
| `mam:livingwood_from_log` | Any log (tag) | `mam:livingwood_log` | 200 ticks |

