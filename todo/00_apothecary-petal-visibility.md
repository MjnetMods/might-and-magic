---
type: todo
last-updated: 2026-07-02
links: ["[[10_apothecary]]"]
---

# Apothecary Petal Visibility

In Botania, petals added to the Apothecary float visibly on the fluid surface. In MAM, added
petals aren't visible at all.

**Why:** same invisibility problem the fluid tank itself had before it got a real capability
render ([[10_apothecary]]) — petals may need the equivalent fix.

**Q:** Is this a missing renderer (petals need their own render contribution, same class of fix
as the fluid tank) or a state/tracking issue (petals aren't being kept anywhere a renderer could
read them)? Check how Botania renders floating petals (`/Users/mannil/mcmod/Botania`) before
scoping.
