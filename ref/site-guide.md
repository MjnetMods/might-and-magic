# Site Guide

Player-facing docs live in `site/content/` and are built with Hugo. This guide covers how to author content, use shortcodes, and manage textures.

## Running locally

```bash
cd site
hugo serve
# → http://localhost:1313/
```

`hugo serve` hot-reloads on save. `site/public/` is the build output — don't edit it directly; run `hugo` to regenerate.

## Voice

Site content sells the feature to a player — prose-first and thematic, not a design-doc summary.
A design doc's tier table, slot counts, and tags are source material to translate, not content to
transcribe: read the existing prose immediately around where you're writing (same file, same
section) and match that voice before adding anything.

- Lead with what the feature *means* to the player, then back it with detail — not the reverse.
- Numeric/tabular reference content (tier tables, slot counts) is allowed but stays minor and
  secondary to the prose, never the section's opening or its bulk.
- No ASCII diagrams. If a recipe needs a visual, use the real `{{< crafting >}}` shortcode (below)
  — this site already has precedent for a representative stand-in texture when the exact one
  doesn't exist yet (e.g. the base Apothecary recipe renders `out="block/apothecary_side"` even
  though T1 accepts any rock, not literally stone). Reuse that pattern rather than reaching for
  prose-as-diagram.

## Content structure

| Path | Purpose |
|------|---------|
| `site/content/<section>/_index.md` | One page per section (getting-started, verdant-path, etc.) |
| `site/layouts/shortcodes/` | Reusable Hugo shortcodes |
| `site/static/textures/` | Item and block textures served at `/textures/` |

## Textures

Textures live under `site/static/textures/` and mirror the in-game texture paths:

| In-game path | Site path | URL |
|---|---|---|
| `assets/mam/textures/item/guide.png` | `site/static/textures/item/guide.png` | `/textures/item/guide.png` |
| `assets/mam/textures/block/living_rock.png` | `site/static/textures/block/living_rock.png` | `/textures/block/living_rock.png` |

When adding a new item or block, copy its texture from `src/main/resources/assets/mam/textures/` into the matching path under `site/static/textures/`.

**Vanilla textures** (e.g. `book.png`) must be extracted from the Minecraft client JAR:

```bash
unzip -p ~/.gradle/caches/ng_execute/<hash>/client.jar \
  assets/minecraft/textures/item/book.png \
  > site/static/textures/item/book.png
```

The hash can be found by running: `find ~/.gradle/caches/ng_execute -name client.jar`.

## Crafting shortcode

The `{{< crafting >}}` shortcode renders an interactive crafting grid.

```
{{< crafting in="<grid>" out="<texture-path>" count=<n> type="<label>" >}}
```

### Parameters

| Parameter | Default | Description |
|---|---|---|
| `in` | (empty grid) | 3×3 ingredient grid — rows separated by `\|`, slots by `,` |
| `out` | (empty) | Output texture path (relative to `/textures/`, no `.png`) |
| `count` | `1` | Output stack count (shown as badge when > 1) |
| `type` | `"Crafting Table"` | Label shown above the grid |

### Grid format

```
"row1col1,row1col2,row1col3|row2col1,...|row3col1,..."
```

Each slot is a texture path relative to `/textures/` without the `.png` extension. Empty slots are blank strings. Whitespace around commas/pipes is trimmed.

### Examples

**Shaped recipe (Mana Pool — U-shape):**

```
{{< crafting
  in="block/living_rock,block/living_rock,block/living_rock|block/living_rock,,block/living_rock|block/living_rock,block/living_rock,block/living_rock"
  out="block/mana_pool_side"
  count=1 >}}
```

**Shaped recipe (2×2):**

```
{{< crafting
  in="block/living_rock,block/living_rock,|block/living_rock,block/living_rock,|,,"
  out="block/living_rock_polished"
  count=4 >}}
```

**Shapeless recipe:**

```
{{< crafting
  in="item/pink_petal,block/pink_mystical_flower,block/pink_mystical_mushroom|,,|,,"
  out="item/guide"
  type="Crafting Table (Shapeless)" >}}
```

For shapeless recipes, pack ingredients left-to-right in row 1 and add an italic note: `*Any color of petal, flower, and mushroom will work.*`

### Texture path conventions

| Ingredient type | Prefix | Example |
|---|---|---|
| MAM block | `block/` | `block/living_rock` |
| MAM item | `item/` | `item/guide` |
| Vanilla item | `item/` | `item/book` |

Use the texture filename that matches the block/item registration name, not the lang name.
