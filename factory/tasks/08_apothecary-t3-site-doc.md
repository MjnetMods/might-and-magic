---
type: task
gate: done
last-updated: 2026-07-03
links: ["[[magic/10_apothecary]]"]
---

# T3 Sacred Apothecary — Site Doc

Add the Sacred Apothecary (Verdant T3) to the player-facing crafting/apothecary site doc: the
goblet recipe (6 Sacred Living Rock + an Infused Apothecary consumed as the secondary ingredient),
the 64+1 ingredient slot count, and that it's the same fill/throw/catalyst mechanic as T1/T2 —
just the endgame-headroom slot count. See [[magic/10_apothecary]] for the full tier table and
mechanic this doc is selling. Desecrated Apothecary (Dark T3) is out of scope — no dark-school
design doc exists yet.

## Handoffs

- 2026-07-03 — task created (human), gate: site-doc
- 2026-07-03 — site doc written (technical-writer agent), gate: done. Extended "The Apothecary"
  section in `site/content/crafting/_index.md` with a new "Sacred Apothecary" subsection
  immediately after "Infused Apothecary". Trimmed the old closing sentence of the Infused
  Apothecary subsection ("Sacred and Desecrated Apothecaries repeat the trick further up the
  tree...") since it's now superseded by the real Sacred subsection, and Desecrated Apothecary is
  explicitly out of scope for this task (no dark-school design doc exists yet — left unmentioned
  entirely, per the task's own guardrail, rather than teased).

  New subsection covers: the goblet recipe (6 Sacred Living Rock + an Infused Apothecary consumed
  whole in the center slot, same shape as T1/T2) via a real `{{< crafting >}}` shortcode — no ASCII
  diagram; the 64+1 slot jump framed as deliberate future headroom, not a currently-needed
  capacity (matches `design/magic/10_apothecary.md`'s own "no current recipe needs more than 8
  slots" framing, translated to prose rather than transcribed as a number); and an explicit line
  that the fill/throw/catalyst/retract/recraft mechanic itself is completely unchanged from the
  tiers below.

  Texture stand-ins: reused the same precedent task 01 established — `block/living_rock` for the
  six rock slots, `block/apothecary_side` for both the center secondary-ingredient slot (the
  consumed Infused Apothecary) and the output. Both confirmed present under
  `site/static/textures/block/` before use (no tier-specific Sacred texture exists — see
  `design/magic/27_tier-tinting.md`, tier art is a runtime tint over the same base texture, not a
  separate file). No mechanics invented beyond `design/magic/10_apothecary.md`'s tier table and
  "Using the Apothecary" section; no `Q:` items touched; Desecrated Apothecary not written about or
  timelined, per task scope.

  Composed commit (not run):

  ```bash
  git add site/content/crafting/_index.md factory/tasks/08_apothecary-t3-site-doc.md
  git commit -m "docs: add Sacred Apothecary (T3) to crafting site doc"
  ```
