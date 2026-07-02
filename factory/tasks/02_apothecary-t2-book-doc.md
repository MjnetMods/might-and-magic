---
type: task
gate: done
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---

# T2 Apothecary — In-Game Book Doc

Add the Infused Apothecary (T2) entry to the Patchouli book: goblet recipe page (6 Infused Living
Rock + petal/mushroom, T1 Apothecary as secondary ingredient), and a note that it's the same
fluid/ingredient/catalyst mechanic as T1 with more slots (6+1). See [[magic/10_apothecary]] for the
full tier table this entry documents.

## Handoffs

- 2026-07-02 — task created (human), gate: book-doc
- 2026-07-02 — book-doc written (technical-writer agent), gate: done. Extended the existing
  `entries/mana/apothecary.json` entry with a third page (`patchouli:crafting` on the forward-referenced
  recipe ID `mam:infused_apothecary`, matching `MamRecipeProvider`'s tier-suffix convention) rather than
  creating a new entry — T1's entry is a compact 2-page unit (thematic intro + crafting page) and the
  T2 goblet upgrade reads as one more beat in the same progression, not a separate topic. Added lang key
  `patchouli.mam.guide.verdant.page.apothecary.2` covering the recipe (6 Infused Living Rock + a working
  Apothecary in the center slot, in place of T1's petal/mushroom) and the "same mechanic, more slots"
  note in a single page, per book-guide's "one page per beat" rule. Confirmed `mam:infused_apothecary`
  is already a registered block (`VerdantMana.INFUSED_APOTHECARY`, `src/main/java/org/mjli/mam/verdant/VerdantMana.java:80-81`)
  so the icon/recipe-output reference is valid even though the recipe JSON itself doesn't exist yet
  (blocked on the `impl` gate). Composed commit command below — not executed.

  ```
  git add src/main/resources/assets/mam/patchouli_books/guide/en_us/entries/mana/apothecary.json \
          src/main/resources/assets/mam/lang/en_us.json \
          factory/tasks/02_apothecary-t2-book-doc.md
  git commit -m "docs: add Infused Apothecary (T2) page to book Apothecary entry"
  ```

- 2026-07-02 — human review correction: `page.apothecary.2` echoed page 0's "rises from the
  water" verb for the *build* step, implying the Infused Apothecary itself is built via the
  fill/throw/catalyst mechanic rather than a plain crafting-table recipe. Reworded to "The result
  is the Infused Apothecary. Use it exactly as before — ..." to cleanly separate the build step
  (goblet recipe, previous page) from the usage step (fluid/ingredient/catalyst, unchanged from
  T1). No structural change, one sentence.
