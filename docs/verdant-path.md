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
  ↓ grow near mana sources
Generating Special Flowers (producing mana)
  ↓ emptyManaIntoCollector()
Mana Pool (stores up to 1,000,000 mana)
  ↓ mana spreader (future)
Functional Flowers / Abilities (consuming mana)
```

### Key Blocks

| Block | Function |
|---|---|
| **Mystical Flowers** (16 colors) | Decorative, drop petals, used in crafting |
| **Pure Daisy** | Special flower — converts adjacent blocks (stone → living rock) |
| **Living Rock** | Crafting material, obtained via Pure Daisy |
| **Petal Apothecary** | Primary crafting station — petals + reagent + water → items |
| **Mana Pool** | Stores mana; filled by generating flowers; drained by abilities |

## Crafting Progression

1. Find mystical flowers in the world
2. Craft a **Pure Daisy** via petal apothecary
3. Place Pure Daisy near stone → converts to **Living Rock**
4. Craft a **Mana Pool** from living rock
5. Craft generating special flowers → place near mana pool
6. Use accumulated mana for healing/buff abilities

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

A **Tome of Verdant Lore** is crafted early and serves as the in-game documentation. Content TBD — will cover lore of divine entities, mana mechanics, crafting recipes, and ability progression.

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
