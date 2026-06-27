# The Verdant Path

A light/positive school of magic centered on floral mana, nature rituals, and alignment with divine entities.

## Thematic Identity

The Verdant Path draws power from living flowers and the divine will flowing through the natural world. Practitioners do not worship named gods but rather align themselves with unnamed divine presences — ancient entities who shaped the land and whose influence lingers in bloom and root.

- **Not powerful** — intentionally basic and supportive, not aggressive
- **Plays like**: healer / sustain / ritual (avoid exact class terminology — not "cleric" or "druid")
- **Visual language**: flowers, soft light, living stone, sparkling mana particles
- **Contrast with tech**: where the tech path builds machines, this path grows gardens

## Magic Exposure Counter

As a player invests in this school, they accumulate a **Verdant Exposure** counter. This is not a punishment — it is a measure of how deeply their soul has been touched by the divine-natural. Higher exposure:

- Unlocks deeper Patchouli guidebook entries
- Gates certain Curios accessories (e.g., amulets only attunable at high exposure)
- Produces subtle cosmetic changes (particles, eye glow, etc. — later)
- Marks the player as "of the path" for future inter-school interactions

## Mana System

Mana is the fundamental resource. It is gathered passively by special flowers and stored in mana pools.

```
Mystical Flowers (placed in world)
  ↓ bonemeal → Tall Mystical Flowers (more petals)
  ↓ break → Petals (items)
Petal Apothecary (petals + water + reagent → items)
  ↓ craft Pure Daisy
Pure Daisy (placed near stone/logs)
  ↓ converts to Living Rock / Livingwood Log
Mana Pool (crafted from Living Rock)
  ↓ filled by generating special flowers
Functional Flowers / Abilities (consuming mana)
```

### Key Blocks & Items

| Block / Item | Function |
|---|---|
| **Mystical Flowers** (16 colors) | Decorative; drop 1 petal when broken; bonemeal grows to tall variant |
| **Tall Mystical Flowers** (16 colors) | Double-tall variant; drop 2 petals (lower half only) |
| **Petals** (16 colors) | Item — dropped by flowers or crafted (1 flower → 4 petals); right-click on ground plants a Buried Petal |
| **Buried Petals** (16 colors, internal) | Hidden underground sprout; grows into matching Mystical Flower on random tick |
| **Floral Powder** | Thrown item — scatters random Mystical Flowers from the `mam:mystical_flowers` tag |
| **Pure Daisy** | Special flower — converts adjacent blocks (stone → living rock, any log → livingwood log) |
| **Living Rock** | Crafting material; obtained via Pure Daisy or found; comes in plain, polished, and brick variants |
| **Livingwood Log** | Crafting material; obtained via Pure Daisy on any log; refined into planks |
| **Petal Apothecary** | Primary crafting station — fill with water, throw petals + a seed reagent to conjure items |
| **Mana Pool** | Stores up to 1,000,000 mana; filled by generating flowers within 6 blocks; comparator-readable |
| **Verdant Path Guide** | Patchouli guidebook — all lore and mechanics documented in-game |

## Crafting Progression

1. Find Mystical Flowers in the world (or wait for world gen to ship)
2. Bonemeal flowers → Tall Mystical Flowers (2× petal yield)
3. Break flowers to collect **Petals** — or craft: 1 flower → 4 petals shapeless
4. Fill a crafting table **Petal Apothecary** with water, throw petals + reagent → conjure a **Pure Daisy**
5. Place Pure Daisy near stone → converts to **Living Rock**; near logs → converts to **Livingwood Log**
6. Craft from Living Rock: polished → bricks → **Mana Pool** (U-shape) and **Petal Apothecary** block
7. Craft generating special flowers via Petal Apothecary → place near Mana Pool
8. Use accumulated mana for healing/buff abilities

## Recipes (Implemented)

| Recipe | Type | Result |
|---|---|---|
| Any flower → 4 petals | Shapeless crafting | Petals (item) |
| 4 living_rock (2×2) → 4 polished | Shaped crafting | living_rock_polished |
| 4 polished (2×2) → 4 bricks | Shaped crafting | living_rock_brick |
| 8 living_rock (U-shape) → mana_pool | Shaped crafting | mana_pool |
| living_rock + polished (goblet) → apothecary | Shaped crafting | petal_apothecary |
| livingwood_log → 4 planks | Shapeless crafting | livingwood_planks |
| stone → living_rock | Pure Daisy (200 ticks) | living_rock |
| any log → livingwood_log | Pure Daisy (200 ticks) | livingwood_log |

## Abilities (placeholder names)

| Name | Type | Cost | Effect |
|---|---|---|---|
| Verdant Touch | Active | 500 mana | Heal target for 4HP |
| Bloom Aura | Passive | 50 mana/s | Slow regen to nearby allies |
| Nature's Ward | Active | 2000 mana | Briefly resist damage |
| Living Light | Active | 200 mana | Place a light source |

## Accessories (Curios)

Items worn in Curios slots that enhance this school:

| Item | Slot | Effect |
|---|---|---|
| Verdant Amulet | Necklace | +20% mana pool capacity |
| Bloom Ring | Ring | Mana regen from nearby flowers |
| Petal Charm | Charm | Reduce petal apothecary cost |

_Exact slots and stats TBD._

## Patchouli Guidebook

The **Verdant Path Guide** is crafted early and serves as in-game documentation. Book ID: `mam:verdant_path`.

Planned categories and entries:

| Category | Entries |
|---|---|
| Introduction | Welcome, The Verdant Path (overview) |
| Flowers | Mystical Flowers, Tall Flowers, Petals, Floral Powder |
| Mana | Pure Daisy, Mana Pool, Petal Apothecary |
| *(future)* | Generating Flowers, Abilities, Accessories |

## Dependencies

| Mod | Role in this school |
|---|---|
| GeckoLib | Animated special flowers and future entities |
| Curios | Accessory slots for school-specific items |
| Patchouli | Guidebook for lore and mechanics |
| Lodestone | Rendering/utility backend |

## Tag Compatibility with Botania

This mod uses Botania's tag namespaces intentionally, so modpacks including both can work together:

- `botania:mystical_flowers` — includes mam flowers
- `botania:special_flowers` — includes Pure Daisy
- `minecraft:small_flowers` — all mystical flowers
- `mam:living_rock` — living rock and variants (mam-specific crafting)

## Negative Energy / Cross-School Interaction

*Planned, not yet implemented.* Using Verdant Path abilities accumulates Verdant Exposure (see above). In multiplayer, players on opposing schools (e.g., tech vs. magic) will have mechanisms to exploit each other's exposure. Details TBD when other schools are designed.
