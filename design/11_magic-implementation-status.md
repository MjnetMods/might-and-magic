---
path: magic
type: impl
status: wip
last-updated: 2026-07-01
links: "[[12_magic-test-plan]], [[21_verdant-implementation-status]]"
---

# Magic — Implementation Status

Shared magic infrastructure used across all paths. Legend:
- ✅ done & verified in-game
- 🔨 code exists, not yet verified
- ⬜ planned, not started
- ❌ known broken

---

## Implementation Plan (next up)

Design audit complete as of 2026-07-01 (see design/magic/*.md) — no contradictions, core loop ready to implement. Ordered by dependency, not priority:

1. ✅ **Mana Pool crafting recipe** — datagen entry in `MamRecipeProvider`, same pattern as the Apothecary recipe already shipped. Design: [[magic/15_mana-pool]] § Pool Crafting Recipes. Code done; not yet verified in-game.
2. **Mana Pool infusion mechanic** — `ManaPoolBlockEntity` needs item-scan + recipe matching; new `PoolInfusionRecipe` type; recipe JSONs from the Infusion Recipes table. Design: [[magic/15_mana-pool]] § Infusion Mechanic. Depends on #1 existing (pool needs to be obtainable to test).
3. **Gems** (Mana/Infused/Sacred/Desecrated Diamond + Pearl) — items + pool-infusion recipes. Design: [[magic/17_trinkets]] § Gems. Depends on #2 (infusion recipe type).
4. **Tablets** — crafting recipe + passive repair drain. Design: [[magic/17_trinkets]] § Tablets. Depends on #3 (Gems) + tier-matched Living Rock. *(Ahead of order: Mana Tablet (T1) item + Pool load/unload already implemented via `/give`-only stub, since the pool-side mechanic didn't need Gems to exist first — see Known Gaps/TODOs.)*
5. **Rings** — Tablet → Ring conversion. Design: [[magic/17_trinkets]] § Rings. Depends on #4.
6. **Altar station shell** — block, block entity, tier registration, crafting recipes, ingredient-detection/trigger/mana-sourcing mechanic. Design: [[magic/20_altar]]. No blockers.
7. **Infrastructure Runes** (`rune_infusion`, `rune_sacred`, `rune_desecrated`) — recipe data, registered as Altar recipes. Design: [[magic/25_runes]] § Infrastructure Runes. Depends on #6 (Altar must exist as the crafting station).
8. **Weavery station shell** — block (SmithingTableBlock subclass), crafting recipes. Design: [[magic/30_weavery]]. No blockers. Trinket catalog itself stays design-only (recipes TBD).

**Explicitly NOT in this pass** (blocked on open design questions, not doc gaps):
- Apothecary `interact()` — needs `ApothecaryRecipe` data-model rework first ([[magic/10_apothecary]] § Open Questions)
- The 22 school Runes (T1/T2/T3a/T3b) — ingredients TBD, balancing pass
- Weavery's actual Trinket items — recipes TBD ([[20_verdant-path-items]])

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
| infused_living_rock | 🔨 | 🔨* | 🔨 | ⬜ |
| infused_living_rock_polished | 🔨 | 🔨* | 🔨 | ⬜ |
| infused_living_rock_brick | 🔨 | 🔨* | 🔨 | ⬜ |
| sacred_living_rock | 🔨 | 🔨* | 🔨 | ⬜ |
| sacred_living_rock_polished | 🔨 | 🔨* | 🔨 | ⬜ |
| sacred_living_rock_brick | 🔨 | 🔨* | 🔨 | ⬜ |
| desecrated_living_rock | 🔨 | 🔨* | 🔨 | ⬜ |
| desecrated_living_rock_polished | 🔨 | 🔨* | 🔨 | ⬜ |
| desecrated_living_rock_brick | 🔨 | 🔨* | 🔨 | ⬜ |

\* placeholder model — reuses the tier-1 texture 1:1, no infused/sacred art yet (no tint logic either). See [[magic/15_mana-pool]] § Visual Design for the intended white/green/purple-tint scheme to implement later.

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
| infused_livingwood_log | 🔨 | 🔨* | 🔨 | 🔨 |
| infused_livingwood | 🔨 | 🔨* | 🔨 | 🔨 |
| infused_livingwood_planks | 🔨 | 🔨* | 🔨 | 🔨 |
| sacred_livingwood_log | 🔨 | 🔨* | 🔨 | 🔨 |
| sacred_livingwood | 🔨 | 🔨* | 🔨 | 🔨 |
| sacred_livingwood_planks | 🔨 | 🔨* | 🔨 | 🔨 |
| desecrated_livingwood_log | 🔨 | 🔨* | 🔨 | 🔨 |
| desecrated_livingwood | 🔨 | 🔨* | 🔨 | 🔨 |
| desecrated_livingwood_planks | 🔨 | 🔨* | 🔨 | 🔨 |

\* placeholder model — reuses the tier-1 texture 1:1, no infused/sacred/desecrated art yet.

**Verify:** Log rotates on placement (axis x/y/z). Glimmering logs glow.

---

### Pure Daisy

Converts adjacent blocks into magic materials. Produced via Apothecary.

| Block | Registered | Model | Mechanic | Recipe |
|---|:---:|:---:|:---:|:---:|
| pure_daisy | 🔨 | 🔨 | 🔨 | ⬜ |

**Verify:** Place Pure Daisy. Surround with stone → each converts to `living_rock` after ~200 ticks. Surround with any log → converts to `livingwood_log`.

---

### Mana System

| Block | Registered | Model | Mechanic | Comparator |
|---|:---:|:---:|:---:|:---:|
| mana_pool | 🔨 | 🔨 | 🔨 | ⬜ |
| infused_mana_pool | 🔨 | 🔨* | ⬜ | ⬜ |
| sacred_mana_pool | 🔨 | 🔨* | ⬜ | ⬜ |
| desecrated_mana_pool | 🔨 | 🔨* | ⬜ | ⬜ |
| apothecary | 🔨 | 🔨 | ⬜ | — |
| weavery | ⬜ | ⬜ | ⬜ | — |
| infused_weavery | ⬜ | ⬜ | ⬜ | — |
| sacred_weavery | ⬜ | ⬜ | ⬜ | — |
| desecrated_weavery | ⬜ | ⬜ | ⬜ | — |
| infused_apothecary | ⬜ | ⬜ | ⬜ | — |
| sacred_apothecary | ⬜ | ⬜ | ⬜ | — |
| desecrated_apothecary | ⬜ | ⬜ | ⬜ | — |

\* placeholder model — reuses the tier-1 `mana_pool` model/textures, no infused/sacred/desecrated art yet (see [[magic/15_mana-pool]] § Visual Design). Capacity is correct (4M / 16M via `MAX_CAPACITY_TIER_2/3` in `ManaPoolBlockEntity`); `sacred_mana_pool`/`desecrated_mana_pool` both correctly carry `MAX_CAPACITY_TIER_3` (same capacity, different `EnergyType`). Tainting (unaligned T1/T2 flip to incoming type) and T3 aligned rejection (opposing energy drains instead of converting) are implemented and covered by `TestManaPool` MP-6..MP-9 — see [[magic/00_energy]].

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
| Apothecary water fill | ⬜ | ⬜ | |
| Apothecary in-world crafting | ⬜ | ⬜ | Interaction hook exists, logic TODO |
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
| 7 living_rock (U-shape) → mana_pool | shaped | 🔨 | ⬜ |
| 7 infused_living_rock (U-shape) → infused_mana_pool | shaped | 🔨 | ⬜ |
| 7 sacred_living_rock (U-shape) → sacred_mana_pool | shaped | 🔨 | ⬜ |
| 7 desecrated_living_rock (U-shape) → desecrated_mana_pool | shaped | 🔨 | ⬜ |
| 7× any rock + 1 petal/mushroom (goblet) → apothecary | shaped | ✅ | ⬜ |
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
- [ ] Apothecary water/lava interaction not coded
- [ ] Apothecary in-world recipes (petals + seed → item)
- [ ] Pure daisy recipe (blocked on apothecary crafting)
- [x] Infused / sacred / desecrated living rock & livingwood blocks registered (placeholder textures, no obtain path yet — needs the Mana Pool infusion mechanic, plan item #2)
- [x] Infused / sacred / desecrated mana pool blocks registered + U-shape recipes from tier-matched Living Rock (placeholder textures, correct tiered capacity)
- [x] Mana/Nox energy-type groundwork — `EnergyType` enum, `EnergyPool.getEnergyType()`, persisted on `ManaPoolBlockEntity`; sacred=MANA, desecrated=NOX, mana_pool/infused_mana_pool=MANA
- [x] Tainting mechanic (Nox entering a pool converts mana 1:1) — implemented via `EnergyContainer.receive()`, covered by `TestManaPool` MP-6/MP-7
- [x] T3 pool alignment/rejection (wrong energy type → mutual loss) — implemented via `EnergyContainer.receive()`'s aligned branch, covered by MP-8/MP-9
- [x] Internal energy API generalized beyond Mana-only naming (`api.mana` → `api.energy`, `ManaPool`/`ManaReceiver`/`ManaCollector`/`ManaNetworkHandler` → `Energy*`) — more energy types than Mana/Nox are planned, so the API no longer assumes "Mana" as the default/only polarity
- [x] Mana Tablet (T1) — item registered with `EnergyContainer` data component; Mana Pool gained an internal charging slot (`interact()`/`transferChargingItem()` on `ManaPoolBlockEntity`) for insert/retrieve/bidirectional transfer. No crafting recipe (blocked on Gems, plan item #3) or repair drain yet. Design: [[magic/17_trinkets]] § Loading / unloading mana
- [ ] Infused / sacred / desecrated living rock & livingwood real texture art + tint
- [ ] Infused / sacred / desecrated mana pool real texture art + tint
- [ ] Infused / sacred / desecrated mana pool bootstrap recipe (Rune + Pool item) — blocked on Altar/Runes (plan items #6/#7)
- [ ] Infused / sacred / desecrated tiers of apothecary (future)
- [ ] Nox generation (dark flowers/sources) — design TBD, no obtain path for any Nox at all yet
- [ ] No sounds beyond vanilla defaults
