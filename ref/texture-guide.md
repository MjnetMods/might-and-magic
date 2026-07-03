# Texture Guide

NeoForge/Minecraft texture-atlas conventions for this mod.

---

## Block texture atlas only indexes textures/block/

Shared/cross-feature textures referenced from a block model's `textures` map must live under
`assets/mam/textures/block/`, not `textures/misc/` — the block texture atlas
(`assets/minecraft/atlases/blocks.json`) only auto-indexes `textures/block/` recursively.
`textures/misc/` is for textures bound directly by hardcoded Java renderers (enchant glint,
forcefield) and is invisible to ordinary block-model `textures` map references.

Hit and fixed once already: Mana Spreader's Loop Marking decals
(`design/magic/16_mana-spreader.md`) initially placed shared rune-glyph textures under
`textures/misc/rune_marks/`, copying vanilla's convention for hardcoded-renderer textures — broke
in-game as missing-texture. Fixed by moving to `textures/block/rune_marks/`. Any future
shared/cross-feature texture referenced from a block model belongs under `textures/block/`,
regardless of how "not really block-specific" it conceptually is.
