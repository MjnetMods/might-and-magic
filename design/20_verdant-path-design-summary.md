---
type: design
status: draft
last-updated: 2026-06-29
links: "[[20_verdant-path]], [[20_verdant-path-quipment]], [[20_verdant-path-mana-pool]], [[20_verdant-path-items]]"
---

# Verdant Path — Design Summary

A single-page overview of the full Verdant Path system as designed. For detail on any section, follow the linked design docs.

---

## The Core Loop

The Verdant Path is a **mana-driven progression** school. Everything gates behind mana infrastructure — you build pools, pools unlock materials, materials unlock stations, stations unlock gear. The loop is:

```
flowers → mana → pools → materials → stations → gear/trinkets
```

Mana is not consumed by the player directly — it is stored in pools and drawn down by infusion recipes. Managing that draw-down is the core challenge.

---

## The Tier System

Everything in the Verdant Path comes in three tiers: **Mana**, **Infused**, and **Sacred**. The tier naming is consistent across all material and item types — no exceptions.

| Tier       | Pool Required     | Stat Baseline |
|------------|-------------------|---------------|
| **Mana**   | Mana Pool         | ≈ Iron        |
| **Infused**| Infused Mana Pool | ≈ Diamond     |
| **Sacred** | Sacred Mana Pool  | ≈ Netherite   |

Advancing a tier means building the next pool. That pool is itself made from the materials it produces — see the double lock below.

---

## Progression Arc

### Step 1 — Bootstrap (no mana infrastructure yet)

- Gather stone, craft **Petal Apothecary** (any rock + petal, goblet shape)
- Craft flowers in the Apothecary; use them to grow mana-generating flowers
- Craft **Pure Daisy** in the Apothecary; place it to convert stone → **Living Rock**, logs → **Livingwood**

### Step 2 — First Pool

- Craft **Mana Pool** from Living Rock (U shape: `R.R / RRR`)
- Route flower mana into the pool
- Pool enables all Tier 1 infusion recipes

### Step 3 — Tier 1 Materials & Gear

From the Mana Pool (75% cost per infusion):
- **Mana Ingot** ← Block of Copper
- **Mana Diamond** or **Mana Pearl** ← Diamond / Ender Pearl
- Craft **Mana Weave Patch** (Mana Ingot + 8 string)
- Craft **Mana Tablet** (Living Rock hollow square + Mana Diamond or Pearl)
- Craft **Mana Ring** (Mana Tablet + 5 Mana Ingots, ring shape)
- Craft **Weavery** (2 string + 4 Livingwood)
- Craft Tier 1 metal tools, armor, and cloth armor

### Step 4 — Tier 2 Gate (the double lock)

To reach Infused tier you need an **Infused Mana Pool**, crafted from **Infused Living Rock**. But Infused Living Rock is only produced by infusing Living Rock in an Infused Mana Pool. The bootstrap path for acquiring the first Infused Pool is a design open question — see [[20_verdant-path-mana-pool]] § Open Questions.

Once the Infused Pool exists:
- Produce **Infused Living Rock** (Living Rock → Infused Mana Pool, 75%)
- Produce **Infused Livingwood** (Livingwood → Infused Mana Pool, 75%)
- Craft **Infused Mana Pool** from Infused Living Rock (same U shape)
- Craft **Infused Apothecary** from Infused Living Rock (goblet shape)
- Craft **Infused Weavery** from Infused Livingwood + string

### Step 5 — Tier 3 Gate

Same double lock, one level up: Sacred Living Rock / Livingwood produced only in the Sacred Mana Pool, which is built from Sacred Living Rock.

---

## Materials

All Tier 2/3 materials are produced by pool infusion. All cost **75% of the pool's capacity** — the double lock in practice.

| Material             | T1 Source         | T2/T3 Source                     |
|----------------------|-------------------|----------------------------------|
| Living Rock          | Pure Daisy (stone)| —                                |
| Infused Living Rock  | —                 | Living Rock → Infused Mana Pool  |
| Sacred Living Rock   | —                 | Living Rock → Sacred Mana Pool   |
| Livingwood           | Pure Daisy (log)  | —                                |
| Infused Livingwood   | —                 | Livingwood → Infused Mana Pool   |
| Sacred Livingwood    | —                 | Livingwood → Sacred Mana Pool    |
| Mana Ingot           | Copper Block → Mana Pool | —                         |
| Infused Ingot        | Gold Block → Infused Mana Pool | —                   |
| Sacred Ingot         | Diamond Block → Sacred Mana Pool | —                 |
| Mana Diamond         | Diamond → Mana Pool | —                               |
| Infused Diamond      | Diamond → Infused Mana Pool | —                         |
| Sacred Diamond       | Diamond → Sacred Mana Pool | —                          |
| Mana Pearl           | Ender Pearl → Mana Pool | —                             |
| Infused Pearl        | Ender Pearl → Infused Mana Pool | —                     |
| Sacred Pearl         | Ender Pearl → Sacred Mana Pool | —                      |

*See [[20_verdant-path-mana-pool]] for the full infusion table.*

---

## Crafting Stations

| Station            | Tiers | Recipe                         | Unlocks |
|--------------------|-------|--------------------------------|---------|
| Petal Apothecary   | × 3   | Any rock + petal (goblet)      | Floral crafting per tier |
| Mana Pool          | × 3   | Living Rock tier (U shape)     | All pool infusion |
| Weavery            | × 3   | 2 string + 4 Livingwood tier   | Cloth augmentation |

All three station families follow the same recipe shape at every tier, substituting the tier's material.

*See [[20_verdant-path-mana-pool]] for pool recipes and [[20_verdant-path]] for apothecary and weavery shapes.*

---

## Equipment

### Metal (tools + armor)

Vanilla shapes using tier-matched ingots. All three tiers: Sword, Pickaxe, Axe, Shovel, Hoe, Shears, Helmet, Chestplate, Leggings, Boots.

### Cloth armor (Hood, Robe, Sash, Slippers)

Vanilla armor shapes using Weave Patches (ingot + 8 string). Cloth starts at the same stats as the equivalent metal tier.

Cloth is augmented at the **tier-matched Weavery** using `smithing_transform`. Each piece can hold two independent augments:

| Augment | Input | Slot |
|---------|-------|------|
| Mana storage | Mana Ring (tier-matched) | Any slot |
| Passive effect | Trinket (tier-matched) | Slot-specific |

*See [[20_verdant-path-quipment]] for full equipment and augmentation detail.*

---

## Items

### Progression chain

```
gem infusion → tablet → ring → (wear as curio  OR  weave into cloth)
```

Each step is a one-way commitment. The ring is the deliberate choice point.

### Gems

Produced by pool infusion (75% cost). Two tracks — Diamond and Pearl — each in three tiers. Used to craft tablets.

### Tablets

Crafted from tier-matched Living Rock (hollow square) + either gem track. Portable mana storage — sits in any inventory slot, passively repairs Verdant gear.

### Rings

Crafted from the tablet + 5 tier-matched ingots (ring shape). **Dual purpose:**
- Worn standalone in the ring curio slot
- OR consumed at the Weavery to permanently integrate mana storage into a cloth piece

### Trinkets

Crafted items adapted from Botania's accessory system — renamed and themed for the Verdant Path. Each trinket has a fixed **slot affinity**:

| Slot     | Trinkets |
|----------|----------|
| Hood     | Reach Weave, Breathing Weave, Aura Weave |
| Robe     | Sacred Cloak, Thorn Cloak, Balance Cloak, Veil Cloak, Pixie Cloak, Presence Weave |
| Sash     | Wanderer's Sash, Gale Sash, Repulse Sash |
| Slippers | Feather Slippers, Frost Slippers, Ember Slippers, Draw Slippers, Shadow Step |

Each trinket is a choice point: wear standalone in a curio slot, or weave permanently into the matching cloth slot.

*See [[20_verdant-path-items]] for gem, tablet, ring, and trinket detail.*

---

## Key Mechanics

### Pool infusion (75% rule)
All infusion recipes cost exactly 75% of the pool's mana capacity. Every tier, every material — no exceptions. Tuning the tier system means tuning pool capacities; the rate stays fixed.

### The double lock
Tier 2 and Tier 3 materials are produced *by* the pool that requires them to build. You need the Infused Mana Pool to produce Infused Living Rock, but the pool is built from Infused Living Rock. The bootstrap path for breaking this cycle is a design open question. This gate is intentional — it represents the mid-game threshold.

### Mana repair
Verdant gear uses standard durability but is repaired by mana rather than materials. Mana comes from the tablet (inventory), ring (curio), or cloth-integrated ring. Run the mana dry and the gear takes normal durability damage and eventually breaks. Infrastructure maintenance is the risk model.

### Cloth augmentation
A cloth piece can hold one ring merge (mana storage, any slot) and one trinket merge (passive effect, slot-specific). Both are independent Weavery operations. A full augmented cloth set gives 4× ring capacity and 4 passive effects simultaneously — a powerful but deeply invested endgame state.

---

## Open Questions

- [ ] **T2/T3 pool bootstrap** — how does the player get their first Infused Mana Pool without Infused Living Rock? See [[20_verdant-path-mana-pool]]
- [ ] **Mana Pool recipe shape** — decided (U shape); Infused/Sacred pool recipes blocked on bootstrap resolution
- [ ] **Trinket crafting recipes** — all TBD; materials and shapes not yet designed
- [ ] **Tablet/ring slot** — does the tablet also fit a curio offhand slot, or inventory only?
- [ ] **Repair rate** — how fast does the ring drain to repair gear? Affects feel significantly
- [ ] **Ring passive bonuses** — held for post-launch pass

---

## Status at a Glance

| Area | Status |
|------|--------|
| Tier system | ✅ designed |
| Pool infusion mechanic | ⬜ WP-A |
| Mana Pool T1 block | ✅ implemented |
| Mana Pool T2/T3 blocks | ⬜ WP-B |
| Petal Apothecary T1 | 🔨 in progress |
| Petal Apothecary T2/T3 | ⬜ planned |
| Weavery (all tiers) | ⬜ planned |
| Metal tools & armor | ⬜ planned |
| Cloth armor | ⬜ planned |
| Gems (all tiers) | ⬜ planned |
| Tablets (all tiers) | ⬜ planned |
| Rings (all tiers) | ⬜ planned |
| Trinkets | ⬜ design only — recipes TBD |
| T2/T3 bootstrap path | ⬜ open question |
