---
type: design
status: wip
last-updated: 2026-06-30
links: "[[magic/00_energy]], [[magic/25_runes]], [[00_infra]]"
---

# MAM — Rituals & Chalk

**Rituals are a standalone magic category** — not a sub-feature of Verdant, Sanguine, or Summoning. They are their own school of magic, with Chalk as the primary tool and polished stone (or living rock) as the medium.

The primary purpose of rituals is **permanent in-game multiblock structures and automation**. A completed ritual is a persistent world structure that does something — passively, continuously, or on trigger.

The most powerful rituals require Desecrated Chalk, which forces the player to generate and spend Nox — dabbling in the dark side is the cost of ritual power.

---

## Ritual Pattern Mechanic

Rituals are patterns of **runes drawn in concentric circles** around a central focus point. Each ring outward from the center adds more runes and increases power — and cost.

```
      [rune] [rune]
   [rune]  [focus]  [rune]
      [rune] [rune]
         ↑ inner ring
   (outer rings add more runes, more power, more chalk cost)
```

- **Center** — the focus item, placed last to activate
- **Inner rings** — fewer runes, lower cost, lower effect
- **Outer rings** — more runes, higher chalk drain per marking, stronger ritual

Each rune drawn consumes chalk durability. Larger patterns cost significantly more chalk.

---

## Substrate

Runes are drawn on stone placed in the world. Substrate type affects both **cost** and **effect magnitude**.

| Substrate | Effect |
|-----------|--------|
| Any polished stone | Base cost, base effect |
| Living Rock | Increased cost, increased effect |
| Infused Living Rock | Higher cost, stronger effect |
| Sacred Living Rock | High cost, Verdant-aligned amplification |
| Desecrated Living Rock | High cost, dark-aligned amplification |

Destroying a substrate block destroys the rune marking on it.

---

## Chalk

Chalk is a durable tool item used to draw rune markings. It comes in **16 colors** and **3 tiers**.

Colors determine which rune types can be drawn (and therefore which ritual patterns are available). Tier determines durability and which ritual complexity can be accessed.

Summoning and Sanguine players unlock the Desecrated tier through generating Nox. **There is no Sacred Chalk** — the ritual school has no pure-light endgame. The most powerful structures require going dark.

### Tiers

| Tier | Item key | Durability | Gates |
|------|----------|------------|-------|
| Chalk | `mam:chalk_*` | Low | Basic rituals |
| Infused Chalk | `mam:infused_chalk_*` | Medium | Intermediate rituals |
| Desecrated Chalk | `mam:desecrated_chalk_*` | High | Powerful / dark rituals |

`*` = color suffix (16 per tier, 48 items total).

### Repair

Chalk is repaired using Mana (Chalk / Infused) or Nox (Desecrated), consistent with the MAM item repair system. Player chooses: spend energy to repair, or craft fresh chalk.

### Crafting Chain

Raw chalk comes in three sizes (lump / ingot / block) matching the Minecraft nugget/ingot/block pattern. **Chalk size determines output tier** — any pool works as the energy source, as long as it holds enough energy at the time of drop.

```
Pig  ──(Butcher's Knife)──►  Lard
Lard + Bone Meal            ──►  Chalk Lump   (×9 → Chalk Ingot, ×9 → Chalk Block)

Chalk Lump   ──(drop in any pool with ≥75% T1 capacity)──►  Chalk           (add color → 16 variants)
Chalk Ingot  ──(drop in any pool with ≥75% T2 capacity)──►  Infused Chalk    (add color → 16 variants)
Chalk Block  ──(drop in any pool with ≥75% T3 capacity)──►  Desecrated Chalk (add color → 16 variants)
```

Color is added by combining imbued chalk with runes + petals (Verdant) or mushrooms (Summoning/dark) — TBD per school. Balancing pass required.

---

## Drawing Rituals

1. Place substrate blocks (polished stone or living rock) in the ritual area.
2. Select chalk of the required tier and color for the ritual pattern.
3. Right-click substrate blocks to draw rune markings — chalk loses durability per marking.
4. Complete all rings of the pattern outward from the center.
5. Place the **focus** item in the center — activates the ritual.

The completed pattern forms a persistent multiblock structure. Rune markings remain unless the substrate is destroyed.

---

## Focus

The activation catalyst placed at the ritual center. Also used as a crafting component for staves and side-effect management items (Ward, side-effect drain — combined with runes).

### Recipe

```
2× Living Wood  +  1× energy ingot  ──►  Focus (tier-matched)
```

| Tier | Ingot | Item key | Gates |
|------|-------|----------|-------|
| Focus | Mana ingot | `mam:focus` | Basic rituals, basic staves |
| Infused Focus | Infused ingot | `mam:infused_focus` | Intermediate rituals, intermediate staves |
| Sacred Focus | Sacred ingot | `mam:sacred_focus` | Verdant endgame staves |
| Desecrated Focus | Desecrated ingot | `mam:desecrated_focus` | Dark endgame staves, dark rituals |

Living Wood form TBD — Botania-compatible livingwood or MAM-native variant.

### Uses

- **Ritual activation** — placed in ritual center to trigger the pattern
- **Ward** — Focus + rune(s) → Ward structure; pushes side effect meter of one type to adjacent chunks. Power scales with Focus tier.
- **Anchor / Conduit** — Focus + rune(s) → Anchor structure; pulls side effect meter from adjacent chunks. Power scales with Focus tier.
- **Staff crafting** — Focus core + runes + school materials → school staff (see `[[03_staves]]`)

Ward and Anchor are school-specific — each targets one meter type (Tangle, Corruption, Warp, etc.). Exact rune combinations TBD with side-effect design.

---

## Open Questions

- [ ] **Color → rune mapping** — which colors map to which rune types / ritual schools? TBD with ritual content design.
- [ ] **Focus item type** — dedicated focus item, or a rune? TBD with first ritual design.
- [ ] **Ritual persistence** — persistent until substrate destroyed, or single-use on trigger? Leaning persistent, but per-ritual TBD.
- [ ] **Chalk recipe ingredients** — runes + mushrooms + petals + pig fat confirmed as direction; exact recipe TBD.
- [ ] **Ring count per ritual** — how many concentric rings max? TBD with ritual designs.

---

## Status

| Item | Status |
|------|--------|
| Chalk tiers (3: Chalk / Infused / Desecrated) | ✅ decided |
| Chalk colors (16 per tier, 48 items total) | ✅ decided |
| No Sacred Chalk tier | ✅ decided |
| Color gates rune/ritual type | ✅ decided |
| Tier gates ritual complexity / access | ✅ decided |
| Chalk repair via Mana / Nox | ✅ decided |
| Concentric ring pattern mechanic | ✅ decided |
| Outer rings = higher cost + power | ✅ decided |
| Substrate affects cost and effect | ✅ decided |
| Focus activation (item in center) | ✅ decided |
| Focus tiers (4: Focus / Infused / Sacred / Desecrated) | ✅ decided |
| Focus recipe (2× Living Wood + energy ingot) | ✅ decided |
| Focus secondary uses (Ward, staff crafting) | ✅ decided |
| Chalk crafting chain (Basic Chalk → pool dip → tiered chalk) | ✅ decided |
| Chalk pool gate (pool tier = chalk tier) | ✅ decided |
| Ritual persistence (multiblock structure) | ✅ decided |
| Color → rune mapping | ⬜ TBD — ritual content design |
| Chalk color recipe (dye vs rune+petal/mushroom) | ⬜ TBD |
| Focus + rune combinations (Ward, drain) | ⬜ TBD |
| Living Wood form (Botania-compatible vs MAM-native) | ⬜ TBD |
| Per-ritual designs | ⬜ TBD |
| Item registration | ⬜ not started |
