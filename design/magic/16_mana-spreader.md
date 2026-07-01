---
type: design
status: wip
last-updated: 2026-07-01
links: ["[[magic/00_energy]]", "[[magic/15_mana-pool]]", "[[11_magic-implementation-status]]"]
---

# MAM — Mana Spreader

Cross-school transport block. Moves energy from a generator to a Pool. Universal — carries Mana or Nox depending on what feeds it, same block for every school. See [[magic/00_energy]] for the energy types and pool tainting/rejection rules this block delivers into.

---

## Model

Any generator (flower, dark source, Apothecary bootstrap) emits energy into a Spreader; the Spreader fires a burst at a target Pool.

```
Generator → Mana Spreader → Pool
```

The Spreader is energy-agnostic — it carries whatever energy type the connected generator produces. A Spreader fed by a Nox source delivers Nox; fed by a Mana source delivers Mana.

Delivery has no mechanic of its own beyond the pool's existing rules: pointed at a T3 aligned pool, the pool's rejection mechanic applies on receipt ([[magic/00_energy]] § Pool tiers); Nox delivered to an unaligned pool triggers the standard Tainting rule ([[magic/00_energy]] § Energy Conversion — Tainting).

---

## Open Questions

**Q:** Range — how far can a Spreader reach its target Pool?

**Q:** Burst size — how much energy does a single burst carry?

**Q:** Burst frequency — how often does a Spreader fire?

**Q:** Tiers — does the Spreader tier up (T1/T2/T3) like pools do, with higher tiers increasing range/burst size/frequency, or is it a single untiered block?

**Q:** Targeting — is the target Pool manually bound (e.g. right-click to link), or auto-bound to nearby pools?

---

## Validation

- `todo` — Mana Spreader (block, burst mechanics) — no `Spreader` class exists yet. Tracked as "Mana spreader / bursts" in [[11_magic-implementation-status]].
