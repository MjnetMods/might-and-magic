---
type: task
gate: impl
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# T2 Apothecary — Implementation

Implement the Infused Apothecary (T2) recipe: 6 Infused Living Rock in the goblet's stone slots +
a base Apothecary consumed in the center slot → Infused Apothecary. Mirrors T1's own
`MamRecipeProvider.apothecary()` recipe method — same goblet shape, tier-matched rock, output is
the next tier's block. Recipe ID should be `mam:infused_apothecary`, matching the tier-suffix
convention already used elsewhere (`infused_mana_pool`) and already forward-referenced by the
book-doc task (`02`).

Per `[[coder]]`, write the implementation and its own JUnit/GameTest coverage together in this
task — the human creates a separate `test` task (`04`) once this one is `done`, for [[tester]] to
audit that coverage; not folded into this task regardless of how simple the batch is (see
`[[agent-factory-guide]]` §1's Coder/Tester Q&A). See [[magic/10_apothecary]]'s Validation section
for the exact open item this closes ("Infused / Sacred / Desecrated Apothecary recipes — no recipe
methods exist beyond T1" — this task only covers T2; Sacred/Desecrated are separate future tasks).

## Handoffs

- 2026-07-02 — task created (human), gate: impl
