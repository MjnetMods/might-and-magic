---
type: test
status: verified
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# Manual test — Infused/Sacred/Desecrated tier tinting

GameTest can't verify renderer output (BlockColor/ItemColor tintindex is client-only draw code)
— this is a by-eye check.

## Steps

1. `./gradlew runClient`
2. Open the creative inventory, search "living rock" — place Infused/Sacred/Desecrated
   plain/polished/brick variants in the world, and check the item icons in the inventory too.
3. Search "livingwood" — place Infused/Sacred/Desecrated log and planks variants. Rotate the
   log by placing it against different faces (floor vs. wall) to check both orientations.
4. Search "apothecary" — place the (T1, only tier that exists) Apothecary block.

## Expected

- Infused Living Rock (all 3 material variants) and Infused Livingwood (log + planks) render
  blue (`#4A90E2`), in-world and in the inventory icon.
- Sacred variants render green (`#4CAF50`), Desecrated variants render purple (`#8E44AD`).
- Livingwood logs keep correct axis rotation (bark rings visible on the side faces, not the
  top/bottom) in both vertical and horizontal placement — the tinted template should match
  vanilla's own log rotation behavior exactly.
- Apothecary renders neutral gray, not Living-Rock-tan (the pre-existing placeholder mistake
  this pass corrected).
- Known, intentionally out-of-scope: Living Rock (base tier) and Apothecary's top texture use
  packed multi-frame sheets referenced with default UV, which may show a vertically-squished
  pattern underneath the tint — this is a pre-existing quirk, not something this change touches.

## Delete this file when

Automated screenshot/pixel-diff coverage replaces this check, or the tinting mechanism has been
stable long enough (through further tiers being added onto the same infra) that the regression
risk is low.
