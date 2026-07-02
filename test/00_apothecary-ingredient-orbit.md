---
type: test
status: verified
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# Manual test — Apothecary ingredient orbit rendering

GameTest can't verify renderer output (client-only draw code) — this is a by-eye check.

## Steps

1. `./gradlew runClient`
2. Place `mam:apothecary`
3. Right-click it with a water bucket to fill the tank
4. Throw up to 4 white mystical petals onto it

## Expected

Each ingested petal orbits visibly above the fluid surface (small bobbing circle), instead of
vanishing on contact — matches Botania's floating-petal visual for the Petal Apothecary.

**Verified 2026-07-02.** Two tuning passes were needed before this read clearly: items are
rendered full-bright (`LightTexture.FULL_BRIGHT`, not resampled per-position — an earlier
attempt at resampling world light per-item caused visible blinking as the bob animation crossed
the block-above boundary each cycle) and scaled up twice (`0.15F` → `0.22F` → `0.32F`) from the
initial Botania-derived value, which read as too small in practice.

## Delete this file when

The render code has been stable long enough that regression risk is low, or an automated
screenshot/pixel-diff test replaces this check.
