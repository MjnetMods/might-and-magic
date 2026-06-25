# Implementation Status

Quick-scan checklist for in-game verification. Legend:
- ✅ done & verified in-game
- 🔨 code exists, not yet verified
- ⬜ planned, not started
- ❌ known broken

Update this file as you test. Run `./gradlew runClient` and work through each section.

---

## Blocks

### Mystical Flowers (16 colors)
All registered, have models & textures copied from Botania.

| Block | Registered | Model | In Tab | Bonemeal | Suspicious Stew |
|---|:---:|:---:|:---:|:---:|:---:|
| white_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| orange_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| magenta_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| light_blue_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| yellow_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| lime_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| pink_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| gray_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| light_gray_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| cyan_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| purple_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| blue_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| brown_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| green_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| red_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |
| black_mystical_flower | 🔨 | 🔨 | 🔨 | ⬜ | ⬜ |

**Verify:** Open Verdant Path tab → all 16 present and textured. Place on grass, break, drops item.

---

### Mystical Mushrooms (16 colors)
All registered, cross model + Botania textures, light level 3.

| Block | Registered | Model | Glows | Survives Underground |
|---|:---:|:---:|:---:|:---:|
| white_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| orange_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| magenta_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| light_blue_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| yellow_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| lime_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| pink_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| gray_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| light_gray_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| cyan_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| purple_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| blue_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| brown_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| green_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| red_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |
| black_mystical_mushroom | 🔨 | 🔨 | ⬜ | ⬜ |

**Verify:** Place in a dark cave (no other light). Should emit faint glow. Place on mycelium and on stone — both should survive.

---

### Living Rock

| Block | Registered | Model | Loot Table | Needs Tool |
|---|:---:|:---:|:---:|:---:|
| living_rock | 🔨 | 🔨 | ⬜ | ⬜ |
| living_rock_polished | 🔨 | 🔨 | ⬜ | ⬜ |
| living_rock_brick | 🔨 | 🔨 | ⬜ | ⬜ |

**Verify:** Place all 3 variants. Should look like stone-adjacent material. Breaking without a pickaxe should drop nothing (requiresCorrectToolForDrops).

---

### Special Flowers

| Block | Registered | Model | Mechanic | Recipe |
|---|:---:|:---:|:---:|:---:|
| pure_daisy | 🔨 | 🔨 | 🔨 | ⬜ |

**Verify Pure Daisy mechanic:** Place Pure Daisy. Surround with stone blocks. After ~200 ticks (10 seconds) each stone should convert to living_rock one at a time.

---

### Mana System

| Block | Registered | Model | Mechanic | Comparator |
|---|:---:|:---:|:---:|:---:|
| mana_pool | 🔨 | 🔨 | 🔨 | ⬜ |
| petal_apothecary | 🔨 | 🔨 | ⬜ | — |

**Verify Mana Pool:** Place pool. ManaNetworkHandler should register it on load (check debug log for no crash). Place a comparator next to it — should output 0 when empty.

**Verify Petal Apothecary:** Place block. Right-click with a bucket of water — should fill (TODO: not yet implemented). Shape should look like a goblet on a pedestal.

---

### Living Wood

| Block | Registered | Model | In Tab | Loot Table |
|---|:---:|:---:|:---:|:---:|
| livingwood_log | 🔨 | 🔨 | 🔨 | ⬜ |
| livingwood_log_stripped | 🔨 | 🔨 | 🔨 | ⬜ |
| livingwood_log_glimmering | 🔨 | 🔨 | 🔨 | ⬜ |
| livingwood_log_stripped_glimmering | 🔨 | 🔨 | 🔨 | ⬜ |
| livingwood | 🔨 | 🔨 | 🔨 | ⬜ |
| livingwood_stripped | 🔨 | 🔨 | 🔨 | ⬜ |
| livingwood_planks | 🔨 | 🔨 | 🔨 | ⬜ |
| livingwood_planks_mossy | 🔨 | 🔨 | 🔨 | ⬜ |

**Verify:** Open Verdant Path tab → all 8 present. Log should rotate on placement (axis=x/y/z). Glimmering logs should glow.

**Not yet added:** stairs, slabs, fences, fence gates, wall — add when needed.

---

## Mechanics

| Mechanic | Implemented | Tested | Notes |
|---|:---:|:---:|---|
| Pure Daisy block conversion | 🔨 | ⬜ | Checks 8 neighbors every tick, converts on timer |
| Mana pool storage | 🔨 | ⬜ | MAX_MANA = 1,000,000 |
| Mana network (pool/collector registry) | 🔨 | ⬜ | Per-dimension WeakHashMap |
| GeneratingFlower → Pool mana push | 🔨 | ⬜ | `emptyManaIntoCollector()`, auto-binds within 6 blocks |
| Petal apothecary crafting | ⬜ | ⬜ | Interaction hook exists, logic TODO |
| Petal apothecary water fill | ⬜ | ⬜ | |
| Mana spreader / bursts | ⬜ | ⬜ | Out of scope this session |
| Magic exposure counter | ⬜ | ⬜ | Player data attachment, future |
| Verdant Path abilities (heal, regen) | ⬜ | ⬜ | Future |

---

## Recipes

| Recipe | Type | Implemented | Verified |
|---|---|:---:|:---:|
| stone → living_rock (Pure Daisy) | mam:pure_daisy | ⬜ | ⬜ |
| *(petal apothecary recipes)* | mam:petal_apothecary | ⬜ | ⬜ |
| living_rock crafting recipes | minecraft:crafting | ⬜ | ⬜ |
| living_rock_polished from living_rock | minecraft:crafting | ⬜ | ⬜ |

---

## Tags

| Tag | Purpose | Status |
|---|---|:---:|
| `mam:mystical_flowers` | all 16 mam flowers | 🔨 |
| `mam:special_flowers` | pure_daisy | 🔨 |
| `mam:living_rock` | living rock variants | 🔨 |
| `botania:mystical_flowers` | adds mam flowers to Botania compat | 🔨 |
| `minecraft:small_flowers` | adds mam flowers + pure_daisy | 🔨 |

---

## Dependencies

| Dep | Wired | Content Added |
|---|:---:|:---:|
| GeckoLib 4.8.3 | 🔨 | ⬜ |
| Curios 9.5.1+1.21.1 | 🔨 | ⬜ |
| Patchouli 1.21.1-93-NEOFORGE | 🔨 | ⬜ |
| Lodestone 1.8.2 | 🔨 | ⬜ |
| Create *(planned)* | ⬜ | ⬜ |

---

## Testing

Uses NeoForge GameTest (in-world, not JUnit). Tests live in `infrastructure/gametest/`.

| Test | Structure NBT | What it checks |
|---|:---:|---|
| `verdant_path/pure_daisy_converts_stone` | ⬜ | Stone → Living Rock conversion |
| `verdant_path/mana_pool_empty_comparator` | ⬜ | Comparator output = 0 when pool empty |

**To add a test:**
1. Build the scenario in creative, run `/test export <name>` to get the NBT
2. Copy NBT to `src/main/resources/data/mam/structures/gametest/verdant_path/`
3. Add a `@GameTest(template = "mam:verdant_path/<name>")` method to `TestVerdantPath`
4. Run with `./gradlew runGameTestServer` or `/test runall` in-game

---

## Patchouli Book

Book ID: `mam:verdant_path`

| Category | Entries | Status |
|---|---|:---:|
| Introduction | *(stub category)* | 🔨 |
| Mana | Pure Daisy, Mana Pool, Petal Apothecary | 🔨 |

**Verify:** Craft a Verdant Path Guide (recipe not yet added). Open it. Two categories should appear with basic text entries.

---

## Known Gaps / TODOs

- [ ] Loot tables — no blocks drop anything when broken yet
- [ ] Pure Daisy recipe JSON — recipe type registered but no data file written
- [ ] Petal apothecary water/lava interaction not coded
- [ ] No world gen — flowers/mushrooms don't spawn naturally
- [ ] Living wood: stairs/slabs/fences/walls not added
- [ ] No sounds beyond vanilla defaults
- [ ] No particle effects (Botania sparkles not ported)
- [ ] GameTest structure NBT files need in-game creation
- [ ] Verdant Path Guide item not yet registered (Patchouli book item)
