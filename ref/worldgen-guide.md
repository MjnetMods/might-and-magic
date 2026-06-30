# NeoForge Worldgen Reference — MAM Project

How MAM places mystical flowers (overworld) and mushrooms (underground).  
Adapted from Botania's approach: one custom `Feature` type in Java, everything else in JSON datapacks.

---

## Architecture overview

Three JSON layers, one on top of the other:

```
configured_feature  — WHAT to place (block, patch shape, size)
        ↓
placed_feature      — WHERE / HOW OFTEN (height, frequency, biome filter)
        ↓
biome_modifier      — WHICH BIOMES (tag-driven add/remove)
```

Java only registers the `Feature` type. All tuning is in JSON.

---

## Layer 1 — configured_feature

### Per-color flower patch
`src/main/resources/data/mam/worldgen/configured_feature/<color>_mystical_flower_patch.json`

```json
{
  "type": "minecraft:random_patch",
  "config": {
    "tries": 32,
    "xz_spread": 6,
    "y_spread": 4,
    "feature": {
      "type": "minecraft:simple_block",
      "config": {
        "to_place": {
          "type": "minecraft:simple_state_provider",
          "state": { "Name": "mam:white_mystical_flower" }
        }
      }
    }
  }
}
```

If you want tall flowers to have a chance of spawning here, use a `minecraft:weighted_state_provider` and mix in `mam:white_tall_mystical_flower` at low weight (Botania uses ~5%).

### Random color selector
`data/mam/worldgen/configured_feature/mystical_flowers.json`

```json
{
  "type": "minecraft:simple_random_selector",
  "config": {
    "features": [
      "mam:white_mystical_flower_patch",
      "mam:orange_mystical_flower_patch"
      // … all 16 colors
    ]
  }
}
```

One color is picked randomly per placement attempt.

### Underground mushrooms
`data/mam/worldgen/configured_feature/mystical_mushrooms.json`

No custom Feature needed — vanilla `random_patch` with a `weighted_state_provider` handles this.

```json
{
  "type": "minecraft:random_patch",
  "config": {
    "tries": 40,
    "xz_spread": 16,
    "y_spread": 4,
    "feature": {
      "type": "minecraft:simple_block",
      "config": {
        "to_place": {
          "type": "minecraft:weighted_state_provider",
          "entries": [
            { "weight": 1, "data": { "type": "minecraft:simple_state_provider", "state": { "Name": "mam:white_mystical_mushroom" } } }
            // … all 16 colors, equal weight
          ]
        }
      }
    }
  }
}
```

---

## Layer 2 — placed_feature

### Flowers (overworld surface)
`data/mam/worldgen/placed_feature/mystical_flowers.json`

```json
{
  "feature": "mam:mystical_flowers",
  "placement": [
    { "type": "minecraft:count", "count": 2 },
    { "type": "minecraft:rarity_filter", "chance": 16 },
    { "type": "minecraft:in_square" },
    { "type": "minecraft:heightmap", "heightmap": "MOTION_BLOCKING_NO_LEAVES" },
    { "type": "minecraft:biome" }
  ]
}
```

Tuning knobs:
- `count` — attempts per chunk section
- `rarity_filter chance` — 1-in-N chunks actually fires; higher = rarer

### Mushrooms (underground)
`data/mam/worldgen/placed_feature/mystical_mushrooms.json`

```json
{
  "feature": "mam:mystical_mushrooms",
  "placement": [
    { "type": "minecraft:in_square" },
    {
      "type": "minecraft:height_range",
      "height": {
        "type": "minecraft:uniform",
        "min_inclusive": { "above_bottom": 0 },
        "max_inclusive": { "absolute": 30 }
      }
    },
    { "type": "minecraft:biome" }
  ]
}
```

No `count` or `rarity_filter` here — underground caves are already sparse so one attempt per chunk is fine. Adjust `max_inclusive` if you want mushrooms deeper or shallower.

---

## Layer 3 — biome_modifier

### Add flowers to overworld biomes
`data/mam/neoforge/biome_modifier/add_mystical_flowers.json`

```json
{
  "type": "neoforge:add_features",
  "biomes": "#minecraft:is_overworld",
  "features": "mam:mystical_flowers",
  "step": "vegetal_decoration"
}
```

### Add mushrooms to overworld + nether
`data/mam/neoforge/biome_modifier/add_mystical_mushrooms.json`

```json
{
  "type": "neoforge:add_features",
  "biomes": ["#minecraft:is_overworld", "#minecraft:is_nether"],
  "features": "mam:mystical_mushrooms",
  "step": "underground_decoration"
}
```

### Opt-out blocklist (optional)
Add a `neoforge:remove_features` modifier referencing a tag like `#mam:no_mystical_flowers` so modpack makers can exclude specific biomes without patching the mod.

---

## Java: custom Feature type (if needed)

Botania uses a custom `MysticalFlowerFeature` to control block placement conditions (e.g. must be on grass). If vanilla `random_patch` + `simple_block` is sufficient for MAM flowers, skip this entirely.

If you do need one:

```java
// In MamWorldgen.java (or wherever you keep worldgen registrations)
public static final DeferredRegister<Feature<?>> FEATURES =
    DeferredRegister.create(Registries.FEATURE, MightAndMagic.MODID);

public static final DeferredHolder<Feature<?>, MysticalFlowerFeature> MYSTICAL_FLOWER =
    FEATURES.register("mystical_flower", () -> new MysticalFlowerFeature(MysticalFlowerConfig.CODEC));
```

Then reference `mam:mystical_flower` as the `type` in your configured_feature JSON instead of `minecraft:simple_block`.

Register in the mod constructor:
```java
MamWorldgen.FEATURES.register(modEventBus);
```

---

## File layout summary

```
src/main/resources/
  data/mam/
    worldgen/
      configured_feature/
        white_mystical_flower_patch.json   (one per color)
        …
        mystical_flowers.json              (random selector across all colors)
        mystical_mushrooms.json            (weighted patch, all colors)
      placed_feature/
        mystical_flowers.json
        mystical_mushrooms.json
    neoforge/
      biome_modifier/
        add_mystical_flowers.json
        add_mystical_mushrooms.json
        remove_mystical_flowers.json       (opt-out, optional)
```

---

## Botania reference

Source: `/Users/mannil/mcmod/Botania`  
Key paths: `src/main/resources/data/botania/worldgen/`, `src/main/resources/data/botania/neoforge/biome_modifier/`  
Feature type: `vazkii.botania.common.world.MysticalFlowerFeature`
