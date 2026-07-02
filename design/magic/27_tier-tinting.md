---
type: design
status: wip
last-updated: 2026-07-02
links: ["[[00_energy]]", "[[10_apothecary]]", "[[20_altar]]"]
---

# MAM — Tier Tinting

Every T1→Infused→Sacred/Desecrated tier family (Living Rock, Livingwood, Apothecary, Altar, ...)
needs a distinct look per tier, but hand-painting unique art for each tier of each material isn't
worth the cost yet. This doc covers the shared mechanism: one desaturated base texture per
material, tinted at render time via Minecraft's `tintindex` + a registered `BlockColor`/
`ItemColor`, instead of shipping real per-tier art.

Tiers in this codebase are separate registered `Block` instances (e.g. `SACRED_LIVING_ROCK`,
`DESECRATED_LIVING_ROCK` are distinct fields), not a blockstate property or NBT/capability value
— no `Tier` enum exists. This means the tint handler doesn't need to inspect any state: the same
color lambda is registered directly against every block instance of a given tier, keyed by which
`Block` it is.

## Mechanism

- **Desaturation:** plain luminance grayscale (`convert('L')`, alpha preserved), not partial
  desaturation. One shared file per material (e.g. `living_rock_desaturated.png`), used by all
  of that material's Infused/Sacred/Desecrated tiers. T1's own original colored texture is left
  untouched — only higher tiers point at the desaturated companion.
- **Model templates** (`src/main/resources/assets/mam/models/block/`): `tinted_cube.json` (copy
  of vanilla `block/cube.json` geometry with `"tintindex": 0` added to every face),
  `tinted_cube_all.json` and `tinted_cube_column.json` (both parent `tinted_cube`, remap texture
  vars exactly like vanilla's own `cube_all.json`/`cube_column.json`), and
  `tinted_cube_column_horizontal.json` (standalone, mirrors vanilla's `cube_column_horizontal`
  structure since that one doesn't chain through `block/cube`). Per-block models parent these
  and supply just the texture variable(s) — no per-block tintindex duplication.
- **Color handlers:** `MightAndMagicClient.onRegisterBlockColors`/`onRegisterItemColors`
  (`RegisterColorHandlersEvent.Block`/`.Item`) register one lambda per tint color against the
  relevant `Block[]` list. Built lazily inside the event handler (not as eager static fields) —
  static field initializers run at class-load/mod-construction time, before Registrate's
  deferred blocks are registered, so an eager `Block[] X = { ...get() }` field risks throwing.
  Item color needs its own registration separate from block color (`Block` implements
  `ItemLike`, so the same `Block[]` list is passed to both).

**Q:** Tier signal for the color handler — blockstate property, capability/NBT, or block
identity?
**A:** Block identity — each tier is already a separate registered `Block` instance in this
codebase (confirmed via `VerdantRock.java`/`VerdantWood.java`), so the handler is just registered
per-instance with no new state needed. (2026-07-02)

**Q:** Desaturation method — plain luminance grayscale, or partial (keep some warmth)?
**A:** Plain luminance grayscale. (2026-07-02)

**Q:** Tint colors?
**A:** Infused = `#4A90E2` (blue), Sacred = `#4CAF50` (green), Desecrated = `#8E44AD` (purple).
(2026-07-02)

## Scope (2026-07-02 pass)

Implemented for **Living Rock** (plain/polished/brick) and **Livingwood** (log + bark-only +
planks) — all three higher tiers, all material variants.

**Apothecary** is a special case: T1 (the only tier that exists in code today) was found to be
using textures (`apothecary_bottom/side/top.png`) that are pixel-identical to Living Rock's
palette — an accidental copy-paste placeholder, not intended art, since Apothecary is built from
`c:stones` generically, not Living Rock specifically (see the crafting table in
[[10_apothecary]]). Fix: desaturated those textures in place (no dual-purpose need, since only
one tier exists) and registered a `0xFFFFFF` identity tint — the grayscale alone reads as neutral
stone, no hue multiply needed. When Infused/Sacred/Desecrated Apothecary tiers get built later,
they reuse this same desaturated base tinted blue/green/purple like every other family.

**Blocked, not implemented this pass:** Apothecary's Infused/Sacred/Desecrated tiers and all of
Altar don't exist as registered blocks yet ([[10_apothecary]]'s tier table marks them "Not yet
implemented"; Altar has no Java class at all) — there's nothing to attach a tint handler to.
Building those tier blocks is separate, larger work belonging to those features' own
implementation stage, not this tinting pass.

**Known, intentionally untouched:** `living_rock.png` (32×192) and `apothecary_top.png`
(32×128) are packed multi-frame sheets (used with explicit UV elsewhere, by T1's own custom
model) that the placeholder `cubeAll`/shape calls reference with default UV — likely squishing
the frames together. Pre-existing, unrelated to tinting; reproduced as-is rather than fixed
here, to keep this pass scoped to "add a tint."

## Validation

- `done` — Living Rock Infused/Sacred/Desecrated (plain/polished/brick) tinted via
  `tinted_cube_all` + `living_rock*_desaturated.png` —
  [`VerdantRock.java`](../../src/main/java/org/mjli/mam/verdant/VerdantRock.java)
- `done` — Livingwood Infused/Sacred/Desecrated (log, bark, planks) tinted via
  `tinted_cube_column`/`tinted_cube_column_horizontal`/`tinted_cube_all` —
  [`VerdantWood.java`](../../src/main/java/org/mjli/mam/verdant/VerdantWood.java)
- `done` — Apothecary T1 corrected from Living-Rock-tan to neutral gray —
  [`shapes/apothecary.json`](../../src/main/resources/assets/mam/models/block/shapes/apothecary.json),
  desaturated `apothecary_bottom/side/top.png`
- `done` — Color handlers registered lazily —
  [`MightAndMagicClient.java`](../../src/main/java/org/mjli/mam/MightAndMagicClient.java)
- `done` — compiles, datagen produces expected blockstate/model JSON (verified by hand against
  generated output for `sacred_living_rock`/`sacred_livingwood_log`)
- `done` — visual confirmation in a running client (rotation correctness, actual tint colors,
  Apothecary gray correction) — [manual steps](../../test/09_tier-tinting.md) (2026-07-02)
- `blocked` — Apothecary/Altar higher-tier tinting — blocked on those tier blocks being built
