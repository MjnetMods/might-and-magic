---
path: magic
type: impl
status: wip
last-updated: 2026-06-30
links: "[[12_magic-test-plan]], [[21_verdant-implementation-status]]"
---

# Magic — Implementation Status

Shared magic infrastructure used across all paths. Legend:
- ✅ done & verified in-game
- 🔨 code exists, not yet verified
- ⬜ planned, not started
- ❌ known broken

---

## Blocks

### Living Rock

| Block | Registered | Model | Loot Table | Needs Tool |
|---|:---:|:---:|:---:|:---:|
| living_rock | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_stairs | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_slab | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_wall | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_polished | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_polished_stairs | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_polished_slab | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_polished_wall | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_brick | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_brick_stairs | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_brick_slab | 🔨 | 🔨 | 🔨 | ⬜ |
| living_rock_brick_wall | 🔨 | 🔨 | 🔨 | ⬜ |
| infused_living_rock | ⬜ | ⬜ | ⬜ | ⬜ |
| infused_living_rock_polished | ⬜ | ⬜ | ⬜ | ⬜ |
| infused_living_rock_brick | ⬜ | ⬜ | ⬜ | ⬜ |
| sacred_living_rock | ⬜ | ⬜ | ⬜ | ⬜ |
| sacred_living_rock_polished | ⬜ | ⬜ | ⬜ | ⬜ |
| sacred_living_rock_brick | ⬜ | ⬜ | ⬜ | ⬜ |

**Verify:** Break without a pickaxe → nothing drops. Break with pickaxe → drops self.

---

### Living Wood

| Block | Registered | Model | In Tab | Loot Table |
|---|:---:|:---:|:---:|:---:|
| livingwood_log | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_log_stripped | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_log_glimmering | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_log_stripped_glimmering | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_stripped | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_planks | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_planks_mossy | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_planks_stairs | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_planks_slab | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_planks_fence | 🔨 | 🔨 | 🔨 | 🔨 |
| livingwood_planks_fence_gate | 🔨 | 🔨 | 🔨 | 🔨 |
| infused_livingwood_log | ⬜ | ⬜ | ⬜ | ⬜ |
| infused_livingwood | ⬜ | ⬜ | ⬜ | ⬜ |
| infused_livingwood_planks | ⬜ | ⬜ | ⬜ | ⬜ |
| sacred_livingwood_log | ⬜ | ⬜ | ⬜ | ⬜ |
| sacred_livingwood | ⬜ | ⬜ | ⬜ | ⬜ |
| sacred_livingwood_planks | ⬜ | ⬜ | ⬜ | ⬜ |

**Verify:** Log rotates on placement (axis x/y/z). Glimmering logs glow.

---

### Pure Daisy

Converts adjacent blocks into magic materials. Produced via Petal Apothecary.

| Block | Registered | Model | Mechanic | Recipe |
|---|:---:|:---:|:---:|:---:|
| pure_daisy | 🔨 | 🔨 | 🔨 | ⬜ |

**Verify:** Place Pure Daisy. Surround with stone → each converts to `living_rock` after ~200 ticks. Surround with any log → converts to `livingwood_log`.

---

### Mana System

| Block | Registered | Model | Mechanic | Comparator |
|---|:---:|:---:|:---:|:---:|
| mana_pool | 🔨 | 🔨 | 🔨 | ⬜ |
| infused_mana_pool | ⬜ | ⬜ | ⬜ | ⬜ |
| sacred_mana_pool | ⬜ | ⬜ | ⬜ | ⬜ |
| apothecary | 🔨 | 🔨 | ⬜ | — |
| weavery | ⬜ | ⬜ | ⬜ | — |
| infused_weavery | ⬜ | ⬜ | ⬜ | — |
| sacred_weavery | ⬜ | ⬜ | ⬜ | — |
| infused_apothecary | ⬜ | ⬜ | ⬜ | — |
| sacred_apothecary | ⬜ | ⬜ | ⬜ | — |

**Verify Mana Pool:** Place pool. Place a comparator next to it → output 0 when empty.

**Verify Apothecary:** Place block. Right-click with a water bucket → should fill (not yet implemented).

---

## Mechanics

| Mechanic | Implemented | Tested | Notes |
|---|:---:|:---:|---|
| Pure Daisy: stone → living_rock | 🔨 | ⬜ | ~200 ticks per block, checks 8 neighbors |
| Pure Daisy: any log → livingwood_log | 🔨 | ⬜ | Tag-based BlockIngredient |
| Mana pool storage | 🔨 | ⬜ | MAX_MANA = 1,000,000 |
| Mana network (pool/collector registry) | 🔨 | ⬜ | Per-dimension WeakHashMap |
| GeneratingFlower → Pool mana push | 🔨 | ⬜ | `emptyManaIntoCollector()`, auto-binds ≤6 blocks |
| Mana pool comparator output | ⬜ | ⬜ | Planned, not coded |
| Petal apothecary water fill | ⬜ | ⬜ | |
| Petal apothecary in-world crafting | ⬜ | ⬜ | Interaction hook exists, logic TODO |
| Mana spreader / bursts | ⬜ | ⬜ | Out of scope |

---

## Recipes

### Pure Daisy (mam:pure_daisy)

| Input | Output | Data file | Verified |
|---|---|:---:|:---:|
| `minecraft:stone` | `mam:living_rock` | 🔨 | ⬜ |
| `minecraft:logs` (tag) | `mam:livingwood_log` | 🔨 | ⬜ |

### Crafting Table (minecraft:crafting)

| Recipe | Shape | Data file | Verified |
|---|---|:---:|:---:|
| 4 living_rock (2×2) → 4 living_rock_polished | shaped | 🔨 | ⬜ |
| 4 living_rock_polished (2×2) → 4 living_rock_brick | shaped | 🔨 | ⬜ |
| living_rock → stairs (×3 variants) | shaped | 🔨 | ⬜ |
| living_rock → slab (×3 variants) | shaped | 🔨 | ⬜ |
| living_rock → wall (×3 variants) | shaped | 🔨 | ⬜ |
| 8 living_rock_brick (U-shape) → mana_pool | shaped | 🔨 | ⬜ |
| 8× any stone (ring) → apothecary | shaped | ✅ | ⬜ |
| livingwood_log → 4 livingwood_planks | shapeless | 🔨 | ⬜ |
| livingwood_log_stripped → 4 livingwood_planks | shapeless | 🔨 | ⬜ |
| livingwood_planks → stairs | shaped | 🔨 | ⬜ |
| livingwood_planks → slab | shaped | 🔨 | ⬜ |
| livingwood_planks → fence | shaped | 🔨 | ⬜ |
| livingwood_planks → fence gate | shaped | 🔨 | ⬜ |

### Apothecary in-world (mam:apothecary)

| Recipe | Status |
|---|:---:|
| *(none yet — petals + seed → item)* | ⬜ |

---

## Tags

| Tag | Purpose | Status |
|---|---|:---:|
| `mam:living_rock` | living rock variants | 🔨 |

---

## Known Gaps / TODOs

- [x] Loot tables — all blocks covered by datagen
- [x] Living rock: stairs/slab/wall for all 3 variants (9 blocks)
- [x] Livingwood planks: stairs/slab/fence/fence_gate
- [x] Standard crafting recipes migrated to MamRecipeProvider (datagen)
- [ ] Mana pool comparator support not coded
- [ ] Petal apothecary water/lava interaction not coded
- [ ] Petal apothecary in-world recipes (petals + seed → item)
- [ ] Pure daisy recipe (blocked on apothecary crafting)
- [ ] Infused / sacred tiers of living rock, livingwood, mana pool, apothecary (future)
- [ ] No sounds beyond vanilla defaults
