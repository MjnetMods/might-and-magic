---
type: todo
last-updated: 2026-07-02
links: ["[[magic/00_energy]]", "[[magic/10_apothecary]]", "[[magic/20_altar]]"]
---

# Sacred / Desecrated Tier Tinting

Every T1→Infused→Sacred/Desecrated tier family (Living Rock, Livingwood, Apothecary, Altar, ...) is currently placeholder-textured by literally reusing the T1 art — `VerdantRock.java` comments say so explicitly ("placeholder: reuse tier-1 textures, no art yet"). The design docs already commit to a look ([[magic/10_apothecary]]'s texture-notes column says "tint it green" for Sacred, "tint it purple" for Desecrated) without saying how that tint is produced.

**Recommendation:** Minecraft's `tintindex` mechanism — one shared base texture per material family, plus a registered `BlockColor`/`ItemColor` handler (keyed off tier, e.g. a blockstate property or a tier enum) that multiplies in green or purple at render time. Zero new texture files per tier; trivial to wire (this codebase has no `BlockColor`/`ItemColor` registered anywhere yet, so it'd be new infra, not an extension of something existing). Same "ship the fast placeholder now, real art later" policy already used for flower/rune textures.

**Resolved approach:** tintindex against a *desaturated* copy of the existing T1 texture, not the original colored art directly — same trick vanilla uses for `grass_block_top.png`/leaves (grayscale/neutral-luminance base, `BlockColor` supplies the hue per biome). Desaturating keeps all the existing shading/grain/detail, the multiply only distorts hue rather than muddying it against the base's own baked-in color. One desaturated file per material (e.g. `living_rock_desaturated.png`) is shared by **both** Sacred and Desecrated — tinted green vs. purple at render time by the same `BlockColor`/`ItemColor` handler — so this is 1 new asset per material family, not 1 per tier. T1 keeps its original colored texture untouched; this doesn't touch Infused (still TBD, separate question).

**Q:** Block state property or a capability/NBT-derived tier value keyed to the color handler, to pick green vs. purple per instance?

**Q:** Desaturation method — plain luminance grayscale (`convert('L')`), or partial desaturation (keep some warmth) to avoid a flat, lifeless base before tinting?
