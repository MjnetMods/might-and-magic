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
| `mam:mystical_flower_white` … `_black` | Mystical Flowers (16 colors) | Drops 1 petal; bonemeal → tall variant |
| `mam:tall_mystical_flower_*` | Tall Mystical Flowers (16 colors) | Drops 2 petals (lower half only) |
| `mam:petal_white` … `_black` | Petals (16 colors) | Item; right-click ground → Buried Petal |
| `mam:floral_powder` | Floral Powder | Thrown; scatters random mystical flowers |
| `mam:living_rock` | Living Rock | Crafting material |
| `mam:living_rock_polished` | Living Rock Polished | Crafting material |
| `mam:living_rock_brick` | Living Rock Brick | Crafting material |
| `mam:livingwood_log` | Livingwood Log | Crafting material |
| `mam:livingwood_planks` | Livingwood Planks | Crafting material |
| `mam:verdant_tome` | The Verdant Tome | Patchouli guidebook; Book ID `mam:verdant_tome` |

---

## Verdant Path — Blocks

| Block ID | Name | Block Entity |
|---|---|---|
| `mam:mystical_flower_*` | Mystical Flowers (16 colors) | No |
| `mam:tall_mystical_flower_*` | Tall Mystical Flowers (16 colors) | No |
| `mam:buried_petal_*` | Buried Petals (16 colors, internal) | No |
| `mam:pure_daisy` | Pure Daisy | Yes — `mam:pure_daisy` |
| `mam:living_rock` / `_polished` / `_brick` | Living Rock variants | No |
| `mam:livingwood_log` | Livingwood Log | No |
| `mam:mana_pool` | Mana Pool | Yes — `mam:mana_pool` |
| `mam:petal_apothecary` | Petal Apothecary | Yes — `mam:petal_apothecary` |

---

## Verdant Path — Tags

| Tag | Contents |
|---|---|
| `botania:mystical_flowers` | All 16 mam mystical flower blocks |
| `botania:special_flowers` | `mam:pure_daisy` |
| `minecraft:small_flowers` | All 16 mam mystical flower blocks |
| `mam:living_rock` | `mam:living_rock`, `_polished`, `_brick` |
| `mam:mystical_flowers` | All 16 mam mystical flower items |

---

## Verdant Path — Recipes

### Crafting (Shaped / Shapeless)

| Recipe ID | Type | Input | Output |
|---|---|---|---|
| `mam:petal_from_flower` | Shapeless | 1 mystical flower | 4 petals (matching color) |
| `mam:living_rock_polished` | Shaped 2×2 | 4 living_rock | 4 living_rock_polished |
| `mam:living_rock_brick` | Shaped 2×2 | 4 living_rock_polished | 4 living_rock_brick |
| `mam:mana_pool` | Shaped U (3×3) | 8 living_rock_brick | 1 mana_pool |
| `mam:petal_apothecary` | Shaped goblet | living_rock + polished | 1 petal_apothecary |
| `mam:livingwood_planks` | Shapeless | 1 livingwood_log | 4 livingwood_planks |

### Pure Daisy Conversions

| Recipe ID | Input Block | Output Block | Duration |
|---|---|---|---|
| `mam:living_rock_from_stone` | `minecraft:stone` | `mam:living_rock` | 200 ticks |
| `mam:livingwood_from_log` | Any log (tag) | `mam:livingwood_log` | 200 ticks |

