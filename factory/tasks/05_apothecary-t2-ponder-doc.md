---
type: task
gate: ponder-doc
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# T2 Apothecary — Ponder Doc

Owned end-to-end by [[ponder]] (script + scene, see that charter for why it's one role rather than
a spec-writer/builder handoff). Blocked on both `03` (impl) and `04` (test) being `done` — this
task scripts and builds against the real, implemented *and verified* T2 recipe, not the design
doc's description of it. Waiting on `04` too, not just `03`, matters here specifically: if Tester's
audit turns up a real gap and Coder has to adjust behavior to close it, a scene already built
against the pre-fix behavior would need rework.

Scene: the T1 → T2 upgrade path (gathering Infused Living Rock, placing the goblet, dropping the
T1 Apothecary in as the secondary ingredient), then a quick craft using the extra slots to make
the point of the tier concrete. See [[magic/10_apothecary]] for the mechanic and [[ponder-guide]]
for scene/SNBT format.

## Handoffs

- 2026-07-02 — task created (human), gate: ponder-doc
- 2026-07-02 — reassigned from Technical Writer (script-only) to the new dedicated [[ponder]]
  charter (script + scene build), part of splitting the original generic doc-writing role into
  one charter per artifact — see [[agent-factory-guide]] §1.
