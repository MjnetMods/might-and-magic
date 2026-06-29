---
title: "The Verdant Path"
date: 2026-06-29
draft: false
weight: 20
colorScheme: avocado
---

A light, positive school of magic centered on floral mana, nature rituals, and alignment with divine entities.

> Practitioners do not worship named gods but rather align themselves with unnamed divine presences — ancient entities who shaped the land and whose influence lingers in bloom and root.

**Plays like:** healer / sustain / ritual  
**Visual language:** flowers, soft light, living stone, sparkling mana particles  
**Power level:** intentionally basic and supportive, not aggressive

---

## Mana System

Mana is the fundamental resource of the Verdant Path. It is gathered passively by special flowers placed in the world and stored in mana pools.

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

---

## Blocks & Items

| Block / Item | Function |
|---|---|
| **Mystical Flowers** (16 colors) | Decorative; drop 1 petal when broken; bonemeal grows to tall variant |
| **Tall Mystical Flowers** (16 colors) | Double-tall; drop 2 petals (lower half only) |
| **Petals** (16 colors) | Item — dropped by flowers or crafted (1 flower → 4 petals) |
| **Floral Powder** | Thrown item — scatters random Mystical Flowers |
| **Pure Daisy** | Converts adjacent blocks (stone → living rock, any log → livingwood log) |
| **Living Rock** | Crafting material; plain, polished, and brick variants |
| **Livingwood Log** | Crafting material; refined into planks |
| **Petal Apothecary** | Primary crafting station — fill with water, throw petals + seed to conjure items |
| **Mana Pool** | Stores up to 1,000,000 mana; filled by nearby generating flowers; comparator-readable |
| **Verdant Tome** | Patchouli guidebook, written as a scholar's journal — lore, recipes, and mechanics |

---

## Crafting Progression

1. Collect **Petals** by breaking Mystical Flowers (or bonemeal → tall → 2× yield)
2. Craft: 1 flower → 4 petals (shapeless)
3. Fill a **Petal Apothecary** with water, throw petals + a seed → conjure a **Pure Daisy**
4. Place Pure Daisy near stone → **Living Rock**; near logs → **Livingwood Log**
5. Craft Living Rock into polished → bricks → **Mana Pool** (U-shape, 8 brick)
6. Craft generating flowers via Petal Apothecary → place within 6 blocks of Mana Pool
7. Use accumulated mana for healing and buff abilities

---

## Recipes

### Living Rock Polished

{{< crafting in="block/living_rock,block/living_rock,|block/living_rock,block/living_rock,|,," out="block/living_rock_polished" count=4 >}}

### Living Rock Brick

{{< crafting in="block/living_rock_polished,block/living_rock_polished,|block/living_rock_polished,block/living_rock_polished,|,," out="block/living_rock_brick" count=4 >}}

### Mana Pool

{{< crafting in="block/living_rock,,block/living_rock|block/living_rock,,block/living_rock|block/living_rock,block/living_rock,block/living_rock" out="block/mana_pool_side" count=1 >}}

### Livingwood Planks

{{< crafting in="block/livingwood_log,,|,,|,," out="block/livingwood_planks" count=4 type="Crafting Table (Shapeless)" >}}

### Pure Daisy Conversions

Place a **Pure Daisy** adjacent to the source block. Conversion takes 200 ticks (~10 seconds).

| Source | Result |
|---|---|
| Any stone | Living Rock |
| Any log | Livingwood Log |

---

## Abilities

| Name | Type | Mana Cost | Effect |
|---|---|---|---|
| Verdant Touch | Active | 500 | Heal target for 4 HP |
| Bloom Aura | Passive | 50/s | Slow regen to nearby allies |
| Nature's Ward | Active | 2,000 | Briefly resist damage |
| Living Light | Active | 200 | Place a light source |

---

## Verdant Exposure & Path Dissonance

As you invest in this path you accumulate a **Verdant Exposure** counter. This is not a punishment — it measures how deeply your soul has been touched by the divine-natural.

If you also invest in other paths, that depth works against you. Competing influences produce **Path Dissonance** — structural tension between your exposures. At low levels it is merely inefficiency. At high levels something changes. The paths notice. In multiplayer, a committed practitioner can read your dissonance and exploit it.

Higher exposure:
- Unlocks deeper Patchouli guidebook entries
- Gates certain Curios accessories (amulets only attunable at high exposure)
- Produces subtle cosmetic changes (particles, eye glow — coming later)
- Marks you as "of the path" for cross-school interactions in multiplayer

---

## Accessories

Worn in [Curios](https://www.curseforge.com/minecraft/mc-mods/curios) slots:

| Item | Slot | Effect |
|---|---|---|
| Verdant Amulet | Necklace | +20% mana pool capacity |
| Bloom Ring | Ring | Mana regen from nearby flowers |
| Petal Charm | Charm | Reduce Petal Apothecary cost |
