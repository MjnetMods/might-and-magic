# Book Guide

The in-game Patchouli book (`mam:guide`) lives across two locations — don't confuse them:

| Path | Holds | Touched how often |
|---|---|---|
| `src/main/resources/data/mam/patchouli_books/guide/book.json` | The book's shell: item, texture, creative tab, macros | Once — not part of per-feature book-doc work |
| `src/main/resources/assets/mam/patchouli_books/guide/en_us/categories/*.json` | Category shells (name, icon, sort order) | New category, occasionally |
| `src/main/resources/assets/mam/patchouli_books/guide/en_us/entries/<category>/*.json` | Entry shells: page list, page types, recipe IDs, icon | Every book-doc task |
| `src/main/resources/assets/mam/lang/en_us.json` | The actual page prose, under `patchouli.mam.guide.*` keys the entry files reference | Every book-doc task |

Entry/category JSON is structure only — no prose lives there. All player-facing text is a lang key
(`patchouli.mam.guide.verdant.page.<entry>.<n>`, `patchouli.mam.guide.verdant.entry.<entry>`)
resolved through `assets/mam/lang/en_us.json`. This mirrors Botania's own lexicon split between
`data/botania/patchouli_books` (book shell) and `assets/botania/patchouli_books/.../entries` +
`assets/botania/lang/en_us.json` (content) — see
`Botania/Xplat/src/main/resources/assets/botania/patchouli_books/lexicon/en_us/entries/basics/apothecary.json`
for the reference this project's own book structure was modeled on.

## Entry JSON

```json
{
  "name": "patchouli.mam.guide.verdant.entry.apothecary",
  "category": "mam:mana",
  "icon": "mam:apothecary",
  "sortnum": 2,
  "pages": [
    { "type": "patchouli:text", "text": "patchouli.mam.guide.verdant.page.apothecary.0" },
    { "type": "patchouli:crafting", "recipe": "mam:apothecary", "text": "patchouli.mam.guide.verdant.page.apothecary.1" }
  ]
}
```

- `icon` — an item/block registry name (`mam:apothecary`), not a texture path.
- `sortnum` — position within the category; existing sibling entries in the same category show
  the current sequence.
- Page `type` values are namespaced (`patchouli:text`, `patchouli:crafting`, `patchouli:image`,
  `patchouli:spotlight`) — this MC/NeoForge version of Patchouli requires the prefix, unlike
  older unprefixed examples you may find in older Botania history.
- `patchouli:crafting` pages reference a real recipe ID (`recipe: "mam:apothecary"`) — Patchouli
  renders the actual registered recipe automatically, no manual grid/texture definition needed
  (unlike the site's `{{< crafting >}}` shortcode, which has no recipe data to read from and must
  be told the grid by hand). If the task is documenting a tier not yet implemented, the recipe ID
  is still the right thing to write — it's a forward reference to the `impl` gate's future recipe
  JSON, consistent with the Feature Pipeline's docs-precede-code order; the page just won't render
  correctly in-game until that recipe exists.

## Category JSON

```json
{
  "name": "patchouli.mam.guide.category.mana",
  "description": "patchouli.mam.guide.category.mana.desc",
  "icon": "mam:mana_pool",
  "sortnum": 1
}
```

Most book-doc tasks add an entry to an *existing* category rather than creating a new one — check
`assets/mam/patchouli_books/guide/en_us/categories/` for the category the design doc's feature
belongs under before assuming a new one is needed.

## Voice

Same standard as the site (`[[site-guide]]` §Voice), adapted for a lexicon page rather than a web
page: thematic, second-person or scene-setting prose first, mechanical detail woven in rather than
listed. The existing entries are the reference — read a sibling entry in the same category before
writing (e.g. `entries/mana/apothecary.json` + its lang strings for the Apothecary; `entries/mana/daybloom.json`
for a generating-flower voice sample). Notice the pattern they already follow:

- Opens with a sentence establishing what the thing *is* or *does* in-world, not a spec line
  ("The Endoflame carries a faint warmth even in total darkness..." not "The Endoflame generates
  mana from furnace fuel.").
- Mechanical numbers (mana/tick, range, capacity) land in a second page or second sentence, after
  the theme is established — never the opening line.
- Use the project's existing macros for consistency: `$(thing)Name$()` for concept/mechanic
  references, `$(item)Name$(0)` for concrete items (see `book.json`'s `macros` block for how these
  expand), `$(p)` for a paragraph break within a page, `$(br2)` for a themed double line-break
  (used for the more evocative, scene-setting entries — see `daybloom.json`/`hydroangeas.json`).
- One page per beat, not one page per fact — a `patchouli:crafting` page's own `text` field is
  usually enough to introduce the recipe; it doesn't need a preceding text-only page repeating the
  same information.

## Reference

Full Patchouli page-type and formatting-macro reference:
https://vazkiimods.github.io/Patchouli/docs/patchouli-basics/getting-started
