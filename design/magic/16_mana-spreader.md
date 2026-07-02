---
type: design
status: wip
last-updated: 2026-07-02
links: ["[[magic/00_energy]]", "[[magic/15_mana-pool]]", "[[magic/27_tier-tinting]]", "[[11_magic-implementation-status]]"]
---

# MAM — Mana Spreader

Cross-school transport block. Moves energy from a generator to a Pool (directly, or relayed through other Spreaders). Universal — carries Mana or Nox depending on what feeds it, same block for every school. See [[magic/00_energy]] for the energy types and pool tainting/rejection rules this block delivers into.

Model is a close port of Botania's Mana Spreader (manual aim, flying burst entity, relay chains) — see the Botania reference at the paths named throughout this doc for the source this was ported from.

---

## Model

Any generator (flower, dark source, Apothecary bootstrap) emits energy into a Spreader. The Spreader fires a **burst** — a real flying projectile entity, not an instant/homing delivery — along its currently-aimed direction. Whatever the burst physically hits first receives the energy: a Pool (delivered), another Spreader (relayed onward along that Spreader's own aim), or nothing (the burst eventually runs out of energy and despawns, see Range below).

```
Generator → Mana Spreader → [Mana Spreader → ...] → Pool
```

This makes multi-hop transport a player-built physical routing puzzle (aim Spreader A at Spreader B at the Pool), not automatic pathfinding — same as Botania.

The Spreader is energy-agnostic — it carries whatever energy type the connected generator produces. A Spreader fed by a Nox source delivers Nox; fed by a Mana source delivers Mana. Delivery has no mechanic of its own beyond the pool's existing rules: pointed at a T3 aligned pool, the pool's rejection mechanic applies on receipt ([[magic/00_energy]] § Pool tiers); Nox delivered to an unaligned pool triggers the standard Tainting rule ([[magic/00_energy]] § Energy Conversion — Tainting).

---

## Crafting

Locked in (2026-07-02). Same shape for all 4 tiers, tier-matched log + rock swapped in — mirrors Mana Pool's recipe pattern ([`MamRecipeProvider.manaPool()`](../../src/main/java/org/mjli/mam/infrastructure/datagen/MamRecipeProvider.java)) rather than Botania's ingot-cored frame, since MAM has no ingot items registered yet.

```
WWW
LPL
WWW
```

- `W` — tier-matched Livingwood log (`LIVINGWOOD_LOG` / `INFUSED_LIVINGWOOD_LOG` / `SACRED_LIVINGWOOD_LOG` / `DESECRATED_LIVINGWOOD_LOG`)
- `L` — tier-matched Living Rock (`LIVING_ROCK` / `INFUSED_LIVING_ROCK` / `SACRED_LIVING_ROCK` / `DESECRATED_LIVING_ROCK`), standing in for Botania's ingot slot
- `P` — any mystical petal (`mam:mystical_petals` tag, color-agnostic, matching Botania's any-color petal)

| MAM tier | Frame (`W`) | Core (`L`) |
|---|---|---|
| T1 | `LIVINGWOOD_LOG` | `LIVING_ROCK` |
| Infused | `INFUSED_LIVINGWOOD_LOG` | `INFUSED_LIVING_ROCK` |
| Sacred | `SACRED_LIVINGWOOD_LOG` | `SACRED_LIVING_ROCK` |
| Desecrated | `DESECRATED_LIVINGWOOD_LOG` | `DESECRATED_LIVING_ROCK` |

`unlockedBy` follows the Mana Pool convention: keyed on possession of the tier's frame material (`has_<tier>_livingwood_log`).

---

## Open Questions

**Q:** Range — how far can a Spreader reach its target Pool?
**A:** No fixed max-range value, same as Botania. A burst flies untouched for `preLossTicks` ticks, then bleeds `lossPerTick` energy per tick until it hits 0 and despawns. Effective range is emergent from burst speed × `preLossTicks` × `lossPerTick`, tuned per tier (see Tiers below). Exact constants are a balancing-pass detail, not a design blocker.

**Q:** Burst size — how much energy does a single burst carry?
**A:** Per-tier `burstMana` value, see Tiers table below.

**Q:** Burst frequency — how often does a Spreader fire?
**A:** Not a fixed tick cooldown. A Spreader holds at most one burst in flight at a time and refires as soon as that burst either delivers (receiving Pool/Spreader acks it) or times out/despawns without hitting anything.

**Q:** Tiers — does the Spreader tier up like pools do, or is it a single untiered block?
**A:** Yes — 4 tiers, reusing MAM's existing `T1` / `Infused` / `Sacred` / `Desecrated` naming already used by Mana Pool/Altar/Apothecary ([[magic/27_tier-tinting]]), rather than introducing new tier names. Stat progression adapted from Botania's variants, treated as a pure power ladder:

| MAM tier | ~ Botania source | burst energy | buffer | pre-loss ticks | loss/tick | speed |
|---|---|---|---|---|---|---|
| T1 | Mana / Redstone | 160 | 1000 | 60 | 4 | 1.0x |
| Infused | Elven | 240 | 1000 | 80 | 4 | 1.25x |
| Sacred | Gaia (light-aligned) | 640 | 6400 | 120 | 20 | 2.0x |
| Desecrated | Gaia (Nox-aligned) | 640 | 6400 | 120 | 20 | 2.0x |

Sacred and Desecrated are parallel top-tier branches (light vs. dark alignment) sharing Gaia's power level, not a further step above Infused — matching how Sacred/Desecrated already work as a diverging pair on Mana Pool/Altar/Apothecary rather than a strict linear ladder. Botania's Redstone variant shares T1's exact stats and differs only in trigger mode (fires once per redstone rising edge instead of continuously) — reflected here by folding it into T1 rather than a separate tier; redstone-gated firing remains a possible toggle/mechanic on T1 rather than its own tier.

**Q:** Targeting — is the target Pool manually bound, or auto-bound to nearby pools?
**A:** Manual, dispenser-style: right-clicking a Spreader with an empty hand rotates its aim in fixed angle steps toward the player's look direction. A dedicated precision-aim tool (below) sets exact aim in one click. Aim is a stored rotation, not a bound `BlockPos` — the burst always fires along that vector and delivers to whatever it hits, which is what makes relay chains possible. Not folded into the still-undecided Staves system ([[../03_staves]] spell-engine approach is TBD and shouldn't gate this) — independent item, in scope for this doc.

---

## Precision-Aim Tool

Companion item to the Spreader — right-click a target block while holding it (aimed at a placed Spreader, or Spreader-then-target in two clicks — exact interaction TBD in implementation) to set that Spreader's aim to point exactly at the clicked block, mirroring Botania's Wand of the Four Winds. Without it, aiming is limited to the dispenser-style fixed-angle-step rotation above.

- Works on any Spreader tier; the tool itself is **untiered** — a single item, no Focus/Staff-style tier gating, since aiming isn't a power-scaled action.
- **Recipe (draft):** Livingwood stick + Mana ingot — same ingredient shape as Focus's `2× Living Wood + Mana ingot` ([[../03_staves]]) since both are "shape a stick into a magic tool," but registered as its own item, not a Focus and not staff-gated.
- **Name:** placeholder "Spreader Wand" pending a naming pass.

---

## Validation

- `done` — Crafting recipe locked in (Crafting section above) and wired into datagen ([`MamRecipeProvider.spreader()`](../../src/main/java/org/mjli/mam/infrastructure/datagen/MamRecipeProvider.java))
- `done` — Block + item registration for all 4 tiers, no in-world mechanic yet — geometry and `outside`/`inside`/`back`/`side` textures ported from Botania's `mana_spreader_*` (see [[27_tier-tinting]] for the tinting mechanism reused here) —
  [`SpreaderBlock.java`](../../src/main/java/org/mjli/mam/block/SpreaderBlock.java),
  [`VerdantMana.java`](../../src/main/java/org/mjli/mam/verdant/VerdantMana.java) (2026-07-02)
- `todo` — Spreader block entity (aim state, refire-on-ack loop). Tracked as "Mana spreader / bursts" in [[11_magic-implementation-status]].
- `todo` — Burst projectile entity (flight, `preLossTicks`/`lossPerTick` decay, hit detection against Pools and other Spreaders, relay-onward on hitting a Spreader)
- `todo` — Precision-aim tool ("Spreader Wand" above) — item + recipe + right-click aim-set interaction, built alongside the Spreader itself rather than deferred
- `todo` — Tier stat tuning/balancing pass on the table above (placeholder numbers, not verified in-game)
