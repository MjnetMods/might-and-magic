---
type: design
status: wip
last-updated: 2026-06-29
links: ["[[20_verdant-path]]", "[[21_verdant-implementation-status]]"]
---

# Verdant Path — Equipment

Living materials shaped by mana, not forged by fire. Verdant equipment is cultivated through sacred infusion rather than crafted with a hammer. It sustains itself through mana — but run out, and it breaks.

---

## Thematic Framing

Verdant gear should feel **grown, not smithed**. Metal steeped in living mana until it transforms. Cloth woven from mana-saturated string, soft but resonant with natural power. Equipment that breathes with the path — and needs it to survive.

This is not enchanted gear. It is **mana-integrated gear** — the mana is intrinsic to the material, not added afterward.

---

## Tier Overview

| Tier         | Base Metal    | Cloth Patch         | Mana Pool           | Stat Baseline | Notes                            |
|--------------|---------------|---------------------|---------------------|---------------|----------------------------------|
| **Mana**        | Mana Ingot        | Mana Weave Patch        | Mana Pool           | ≈ Iron      | First step into Verdant gear     |
| **Infused**     | Infused Ingot     | Infused Weave Patch     | Infused Mana Pool   | ≈ Diamond   | Mid-game plateau                 |
| **Sacred**      | Sacred Ingot      | Sacred Weave Patch      | Sacred Mana Pool    | ≈ Netherite | Endgame; Verdant branch          |
| **Desecrated**  | Desecrated Ingot  | Desecrated Weave Patch  | Desecrated Mana Pool | ≈ Netherite | Endgame; dark branch             |

---

## Crafting Chain

### Step 1 — Pool Infusion (metal ingots)

Drop a **block** of base material into a charged Mana Pool. The pool consumes a large mana cost and outputs a single infused ingot.

| Input              | Output           | Pool Required     | Pool Mana Cost   |
|--------------------|------------------|-------------------|------------------|
| Block of Copper    | 1× Mana Ingot    | Mana Pool         | 75% of capacity  |
| Block of Gold      | 1× Infused Ingot | Infused Mana Pool | 75% of capacity  |
| Block of Diamond   | 1× Sacred Ingot  | Sacred Mana Pool  | 75% of capacity  |

1 block → 1 ingot is intentional. Copper is an upgrade to iron-tier — it should feel like a real investment.

### Step 2 — Weave Patch (cloth only)

Surround an infused ingot with string in a crafting table (full 3×3, ingot in center):

```
S S S
S ★ S   ★ = infused ingot (any tier)   → 1× Weave Patch (matching tier)
S S S
```

| Input                    | Output                 | Notes                              |
|--------------------------|------------------------|------------------------------------|
| Mana Ingot + 8 string    | 1× Mana Weave Patch    | Expensive: 1 block copper + string |
| Infused Ingot + 8 string | 1× Infused Weave Patch |                                    |
| Sacred Ingot + 8 string  | 1× Sacred Weave Patch  |                                    |

Expensive is intentional. It should feel like a real investment.

### Step 3 — Gear Crafting

Use vanilla armor/tool shapes, substituting infused ingots (metal) or weave patches (cloth).

---

## Equipment Sets

### Metal Tools & Armor (all three tiers)

Same vanilla crafting shapes, using the infused ingot for that tier.

| Category | Items                                    |
|----------|------------------------------------------|
| Tools    | Sword, Pickaxe, Axe, Shovel, Hoe, Shears |
| Armor    | Helmet, Chestplate, Leggings, Boots      |

### Cloth Armor (Mana Weave — all three tiers)

Armor only — no cloth tools. Uses weave patches in vanilla armor shapes.

| Vanilla Slot | Cloth Name      |
|--------------|-----------------|
| Helmet       | [Tier] Hood     |
| Chestplate   | [Tier] Robe     |
| Leggings     | [Tier] Sash     |
| Boots        | [Tier] Slippers |

Example: `Mana Hood`, `Infused Robe`, `Sacred Sash`.

Cloth armor starts with same stats as the equivalent metal tier.

### Weavery augmentation

Cloth armor pieces are augmented at the tier-matched Weavery by merging in a crafted item — a Mana Ring or a Trinket. The item is consumed; its effect lives permanently in the cloth piece. One ring merge and one trinket merge are independent — the same cloth piece can hold both.

**Recipe (all merges):** `smithing_transform` at the tier-matched Weavery:

```
[ template ] [ cloth piece ] [ ring or trinket ]  →  augmented cloth piece
```

Template slot: empty. Must use correct tier Weavery (Mana cloth → Weavery, Infused → Infused Weavery, Sacred → Sacred Weavery).

#### Mana Ring merge

The Mana Tablet must first be converted into a Ring before it can be woven into cloth. The Ring can be worn standalone as a curio, or committed to cloth — a deliberate one-way choice. Ring can be woven into **any** cloth slot. Tier must match.

| Input                           | Mana storage added |
|---------------------------------|--------------------|
| Any cloth piece + Mana Ring     | 500,000            |
| Any cloth piece + Infused Ring  | 2,000,000          |
| Any cloth piece + Sacred Ring   | 8,000,000          |

The merged piece repairs Verdant gear passively from its built-in mana. Full integrated set stacks capacity across all four slots.

#### Trinket merge

Trinkets have fixed slot affinity — woven only into the matching cloth slot. See [[20_verdant-path-items]] § Trinkets for the full list.

| Slot     | Example trinkets                                       |
|----------|--------------------------------------------------------|
| Hood     | Reach Weave, Breathing Weave, Aura Weave               |
| Robe     | Sacred Cloak, Veil Cloak, Pixie Cloak, Thorn Cloak…   |
| Sash     | Wanderer's Sash, Gale Sash, Repulse Sash               |
| Slippers | Feather Slippers, Ember Slippers, Shadow Step…         |

---

## Mana Integration Mechanic

Verdant equipment uses **standard durability** but is repaired by mana. Run out of mana, and the gear breaks like any normal tool.

### Repair sources

| Source          | How                                                                                |
|-----------------|------------------------------------------------------------------------------------|
| **Mana Pool**   | Drop the item in — instantly repairs using pool mana                               |
| **Mana Tablet** | Held in inventory; passively repairs equipped gear by draining the tablet. Four tiers (Mana / Infused / Sacred / Desecrated) — see [[magic/17_trinkets]] |

This is the same mechanic as Botania's mana gear. Straightforward to implement — durability is the existing repair hook, no custom serialization needed per item.

### Risk of running dry

When no mana is available (empty tablet, no pool nearby), gear takes normal durability damage and will eventually break. This is the intended risk model — Verdant gear rewards maintaining your mana infrastructure, not just having it.

---

## Mana Pool Tiers

Three tiers of Mana Pool exist, each with a larger mana capacity than the last. Higher-tier ingots require the matching pool tier — not just more pools, but a *better* pool.

| Pool              | Notes                                          |
|-------------------|------------------------------------------------|
| Mana Pool         | Base pool; crafted first                       |
| Infused Mana Pool | Larger capacity; unlocks Infused Ingot recipe  |
| Sacred Mana Pool  | Largest capacity; unlocks Sacred Ingot recipe  |

Pool capacity values (absolute mana numbers) are TBD — set during balancing pass alongside tablet drain rate.

---

## Open Questions

**Q:** Infused tier recipe — what does repairing an Infused-tier tablet cost?
**A:** Requires Tier 2 Mana Pool; costs 75% of its capacity (same rule as all tiers).

**Q:** Sacred tier recipe — what does repairing a Sacred-tier tablet cost?
**A:** Drop Block of Diamond in Sacred Mana Pool; costs 75% capacity. Same pattern as all tiers.

**Q:** Mana drain rate — how much mana per durability point repaired? Affects how quickly a tablet depletes.

**Q:** Cloth bonuses — full set mana capacity / cost reduction? Design TBD, hold for post-launch pass.

---

## Status

| Item                     | Status                               |
|--------------------------|--------------------------------------|
| Tier naming              | ✅ decided                            |
| Block → ingot infusion   | ✅ decided (1 block = 1 ingot)        |
| Weave Patch recipe       | ✅ decided (ingot + 8 string)         |
| Mana mechanic            | ✅ decided (durability + mana repair) |
| Metal gear set           | ⬜ planned                            |
| Cloth armor set          | ⬜ planned                            |
| Balancing (costs, drain) | ⬜ post-implementation                |
| Cloth mana bonuses       | ⬜ future pass                        |
