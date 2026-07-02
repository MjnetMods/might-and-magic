---
type: design
status: done
last-updated: 2026-07-02
links: ["[[20_verdant-path]]", "[[23_verdant-generating-flowers]]", "[[magic/26_runes-botania-compat]]"]
---

# MAM — Flower Botania Compat

MAM's flowers are the same furniture as Botania's — shared visual identity and block IDs, different mechanics (see CLAUDE.md § Flower identity & Botania compat for the full policy). This doc is the concrete name/tag record of that policy: which MAM flower block goes in which `botania:` tag, kept in sync with the actual tag files whenever a new flower is registered. Same doc shape as [[magic/26_runes-botania-compat]], applied to flowers' three-tag structure instead of runes' single flat tag.

---

## Mystical flowers — single & tall

All 16 dye colors, in both the single-block and double-block (tall) form. Tag files: `data/botania/tags/block/mystical_flowers.json`, `data/botania/tags/block/double_mystical_flowers.json`.

| Color | `mystical_flowers` | `double_mystical_flowers` |
|-------|---------------------|----------------------------|
| White | `mam:white_mystical_flower` | `mam:white_tall_mystical_flower` |
| Orange | `mam:orange_mystical_flower` | `mam:orange_tall_mystical_flower` |
| Magenta | `mam:magenta_mystical_flower` | `mam:magenta_tall_mystical_flower` |
| Light Blue | `mam:light_blue_mystical_flower` | `mam:light_blue_tall_mystical_flower` |
| Yellow | `mam:yellow_mystical_flower` | `mam:yellow_tall_mystical_flower` |
| Lime | `mam:lime_mystical_flower` | `mam:lime_tall_mystical_flower` |
| Pink | `mam:pink_mystical_flower` | `mam:pink_tall_mystical_flower` |
| Gray | `mam:gray_mystical_flower` | `mam:gray_tall_mystical_flower` |
| Light Gray | `mam:light_gray_mystical_flower` | `mam:light_gray_tall_mystical_flower` |
| Cyan | `mam:cyan_mystical_flower` | `mam:cyan_tall_mystical_flower` |
| Purple | `mam:purple_mystical_flower` | `mam:purple_tall_mystical_flower` |
| Blue | `mam:blue_mystical_flower` | `mam:blue_tall_mystical_flower` |
| Brown | `mam:brown_mystical_flower` | `mam:brown_tall_mystical_flower` |
| Green | `mam:green_mystical_flower` | `mam:green_tall_mystical_flower` |
| Red | `mam:red_mystical_flower` | `mam:red_tall_mystical_flower` |
| Black | `mam:black_mystical_flower` | `mam:black_tall_mystical_flower` |

---

## Generating flowers

Functional (mana-generating) flowers only — never add a non-generating flower here. Tag file: `data/botania/tags/block/generating_special_flowers.json`.

| Flower | `generating_special_flowers` |
|--------|-------------------------------|
| Daybloom | `mam:daybloom` |
| Endoflame | `mam:endoflame` |
| Hydroangeas | `mam:hydroangeas` |

New generating flowers (see [[23_verdant-generating-flowers]] for the batch in progress) get added here the same session they're registered.

---

## Validation

- `done` — All 16 colors, single + tall, in their respective tags — [`mystical_flowers.json`](../src/main/resources/data/botania/tags/block/mystical_flowers.json), [`double_mystical_flowers.json`](../src/main/resources/data/botania/tags/block/double_mystical_flowers.json)
- `done` — 3 generating flowers tagged — [`generating_special_flowers.json`](../src/main/resources/data/botania/tags/block/generating_special_flowers.json)
- `todo` — Keep this table in sync as [[23_verdant-generating-flowers]] batch 2 lands (new generating flowers need a row here + a tag entry, same PR)
