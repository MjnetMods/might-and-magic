---
type: todo
last-updated: 2026-07-02
links: ["[[23_verdant-generating-flowers]]"]
---

# Rafflowsia — Large Multiblock Flower

Botania's Rafflowsia is a generating flower that grows into a large multi-block structure and produces mana per unique "streak" (`STREAK_OUTPUTS` table, 2000 → 638554 mana, range 5, ticks every 40). MAM has no equivalent of Botania's large-flower multiblock growth system — no other MAM flower grows beyond a single block.

**Why:** came up while scoping [[23_verdant-generating-flowers]] (Batch 2 of generating flowers). Every other flower in that batch fits MAM's existing single-block `GeneratingFlowerBlock` pattern; Rafflowsia is the one exception, and building multiblock plant growth just to support one flower wasn't worth doing inside that batch.

**Q:** Is single-block-with-scaling-output a good enough approximation, or does the multiblock growth matter to the identity of this flower specifically?

**Q:** If multiblock growth is built, does anything else in MAM want it (other Verdant content, or is Rafflowsia the only consumer)? Worth knowing before investing in general infrastructure vs. a one-off.
