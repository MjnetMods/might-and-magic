---
type: test
status: pending
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

## Delete this file when

The render code has been stable long enough that regression risk is low, or an automated
screenshot/pixel-diff test replaces this check.
