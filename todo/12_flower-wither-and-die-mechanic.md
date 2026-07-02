---
type: todo
last-updated: 2026-07-02
links: ["[[20_verdant-path]]", "[[23_verdant-generating-flowers]]"]
---

# Flowers Should Wither and Die

No flower block currently has any age, lifespan, or decay mechanic — grepping `block/flower/` and `block_entity/flower/` for wither/decay/age/lifespan turns up nothing. Every mystical flower and generating flower (Daybloom, Endoflame, Hydroangeas, Pure Daisy) persists forever once placed/grown.

**First concrete instance decided:** Daybloom — generates 10 lifetime mana (1/tick, capped at 10 ticks), then withers and dies. See [[23_verdant-generating-flowers]] § Daybloom. The exact wither/death specifics below (visuals, drops, reversibility) are still open even for Daybloom; the questions below cover both that gap and whether/how the mechanic extends to the other flowers.

**Why it matters:** a wither/die mechanic is a real design gap, not just polish — it affects whether flowers are a renewable-but-tended resource (replant on death) versus permanent infrastructure once placed, which has knock-on balance implications for generating flowers especially (does a dead Daybloom stop producing mana entirely, forcing replanting?).

**Open scope, needs a real design pass before promotion to `/design`:**

**Q:** Which flowers wither — generating flowers only (ties into upkeep/balance), all mystical flowers, or everything including Pure Daisy?

**Q:** What triggers withering — age/lifespan timer, resource depletion (soil, nearby mana), out-of-range/unfavorable conditions (e.g. Daybloom losing sky access long-term), or random chance?

**Q:** What happens on death — block removal (bare dirt/nothing), a distinct "withered" block state/texture before removal, item drops (seeds? nothing?), does a generating flower's mana output taper off before it dies or stop abruptly?

**Q:** Is this reversible — can a withered/dying flower be revived (bonemeal, mana, water) or is death final?
