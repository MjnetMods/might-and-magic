---
type: design
status: wip
last-updated: 2026-06-30
links: "[[01_rituals]], [[magic/25_runes]], [[magic/00_energy]]"
---

# MAM — Staves & Spells

Staves are school-flavoured spell-casting tools crafted from a **Focus** core combined with runes and other school-specific materials. They cast spells — either MAM-native or via an external spell engine.

---

## Spell System Approach

Two options (TBD — pick one before implementation):

| Approach | Pros | Cons |
|----------|------|------|
| **Borrow from existing mod** (e.g. Ars Nouveau, Spell Engine) | Spells already built, UI exists | Dependency, less control |
| **MAM-native spell engine** | Full control, no dep | More work |

Decision TBD. Design below is spell-engine-agnostic — staves are the delivery item regardless of backend.

---

## Crafting

Staves are crafted from a tier-matched **Focus** + runes + school-specific materials.

```
Focus (tier) + Runes + School materials → Staff (school + tier)
```

Exact recipes TBD with spell/school design. Staff tier is gated by Focus tier.

---

## Focus Tiers (recap)

| Focus | Recipe | Staff tier gate |
|-------|--------|----------------|
| Focus | 2× Living Wood + Mana ingot | Basic staves |
| Infused Focus | 2× Living Wood + Infused ingot | Intermediate staves |
| Sacred Focus | 2× Living Wood + Sacred ingot | Verdant endgame staves |
| Desecrated Focus | 2× Living Wood + Desecrated ingot | Dark endgame staves |

---

## Open Questions

- [ ] **Spell engine** — borrow from existing mod or MAM-native? TBD
- [ ] **Staff types per school** — what does each school's staff do? TBD with school design
- [ ] **Mana/Nox cost per cast** — TBD, balancing pass
- [ ] **Living Wood form** — Botania-compatible or MAM-native? See `[[01_rituals]]`

---

## Status

| Item | Status |
|------|--------|
| Staves crafted from Focus + runes + school materials | ✅ decided |
| Staff tier gated by Focus tier | ✅ decided |
| Spell engine approach | ⬜ TBD |
| Per-school staff designs | ⬜ TBD |
| Item registration | ⬜ not started |
