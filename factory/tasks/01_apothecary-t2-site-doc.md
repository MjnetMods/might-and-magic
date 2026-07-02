---
type: task
gate: done
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# T2 Apothecary — Site Doc

Add the Infused Apothecary (T2) to the player-facing crafting/apothecary site doc: the goblet
recipe (6 Infused Living Rock + petal/mushroom, with a T1 Apothecary consumed as the secondary
ingredient), the 6+1 ingredient slot count, and how it reads to a player as the next step up from
T1 — same fluid/ingredient/catalyst mechanic, just more slots. See [[magic/10_apothecary]] for the
full tier table and mechanic this doc is selling.

## Handoffs

- 2026-07-02 — task created (human), gate: site-doc
- 2026-07-02 — site doc written (technical-writer agent), gate: done. Extended the existing
  "The Apothecary" section in `site/content/crafting/_index.md` (T1 was already documented there)
  with a tier table (Apothecary / Infused / Sacred / Desecrated — built-from material, secondary
  ingredient, ingredient slot count) and a new "Infused Apothecary" subsection: the goblet recipe
  (6 Infused Living Rock + a base Apothecary consumed in the center slot, ASCII diagram since no
  distinct in-game texture exists yet for the tinted tier — same reasoning the site already
  applies to `infused_mana_pool`, which also has no crafting-shortcode image), and a note that the
  fill/throw/catalyst mechanic and retract/recraft behavior are unchanged from T1 — only the
  6-ingredient headroom (vs. 4) is new. No mechanics invented beyond `design/magic/10_apothecary.md`'s
  tier table; no `Q:` items touched.

  Composed commit (not run):

  ```bash
  git add site/content/crafting/_index.md factory/tasks/01_apothecary-t2-site-doc.md
  git commit -m "docs: add Infused Apothecary (T2) to crafting site doc"
  ```

- 2026-07-02 — redo per human review feedback (technical-writer agent), gate stays `done`. Two
  problems fixed in `site/content/crafting/_index.md`: (1) tone — the tier comparison table read
  like a spec; cut it entirely and folded the tier-progression idea ("you craft your way up by
  feeding the tier below into the tier above") into the lead-in prose instead, matching the
  prose-first voice used elsewhere in the section and in `site/content/verdant-path/_index.md`;
  (2) no image — replaced the ASCII goblet diagram with a real `{{< crafting >}}` shortcode,
  following the precedent already set by the T1 recipe just above it (which renders
  `out="block/apothecary_side"` as a representative stand-in even though T1 accepts any
  `c:stones`, not literal stone): uses `block/living_rock` for the six rock slots and
  `block/apothecary_side` for both the center secondary-ingredient slot (the consumed base
  Apothecary) and the output. Both textures confirmed present under `site/static/textures/block/`.
  Same guardrails as before — no mechanics invented, no `Q:` items touched, no git commit run.

  Composed commit (unchanged, still not run):

  ```bash
  git add site/content/crafting/_index.md factory/tasks/01_apothecary-t2-site-doc.md
  git commit -m "docs: add Infused Apothecary (T2) to crafting site doc"
  ```
