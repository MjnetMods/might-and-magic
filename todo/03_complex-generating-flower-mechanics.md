---
type: todo
last-updated: 2026-07-02
links: ["[[23_verdant-generating-flowers]]"]
---

# Complex-Tier Generating Flower Mechanics

Four flowers — Spectrolus, Entropinnyum, Dandelifeon, Shulk Me Not — are shipping in Batch 2 ([[23_verdant-generating-flowers]]) as block + item + recipe only, with no mana-generation logic. Each needs its own design pass before the real mechanic gets built:

- **Spectrolus** — cycles dye colors, eats matching wool/sheep (Botania: `WOOL_GEN` 1200, `SHEEP_GEN` 5000, `BABY_SHEEP_GEN` 1, range 1). Needs a rotating `DyeColor` sequence state machine.
- **Entropinnyum** — detects nearby primed TNT explosions (Botania: range 12, full `getMaxMana()` per explosion). Needs `PrimedTnt` entity tracking.
- **Dandelifeon** — runs Conway's Game of Life on a flower grid (Botania: range/speed 12/10, 60 mana/generation, cap 100 generations). Needs a full cellular-automaton tick loop over flower blocks in radius — the most involved of the four.
- **Shulk Me Not** — levitates and eats Shulkers (Botania: 75,000 mana/shulker, radius 8). Needs Shulker entity detection + levitation effect application.

**Why:** these four are meaningfully more complex than the simple/medium tier (state machines, cellular automata, explosion/levitation detection) and were explicitly deferred so Batch 2 could ship the straightforward flowers without stalling on the hard ones. See [[23_verdant-generating-flowers]]'s "Complex tier — stub scope only" section for the recipe/book-entry scope that *did* ship.

**Q:** Order to tackle these in — Dandelifeon is the highest-effort (self-contained cellular automaton, no cross-cutting dependency); Spectrolus/Entropinnyum/Shulk Me Not are all medium-complexity entity-detection variants. Likely fine to pick off independently, one per implement/test loop iteration, in any order.
