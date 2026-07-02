# Manual Verification Doc Format Guide

Format for `/test` — one small file per manual/visual regression check that GameTest can't
cover (renderer output, in-game feel, anything requiring a human eyeball on a running client).
See [[design-doc-guide]] and [[todo-doc-guide]] for the shared conventions this reuses; only
what's different is written here.

---

## 1. Why this exists, separate from `/design`

`design/N2_*-test-plan.md` tracks *automated* coverage (JUnit + GameTest). It has no place for
"this can only be checked by looking at it" — and without a written record, that check gets
skipped the next time someone touches the code, silently. `/test` is that record. It is not
part of `/design`: a design doc's Validation line *links* to a `/test` file the same way it
links to a GameTest class, it doesn't embed the check itself.

## 2. Front matter

```yaml
---
type: test
status: pending
last-updated: 2026-07-02
links: ["[[magic/10_apothecary]]"]
---
```

`status` is `pending` until someone actually runs the steps and confirms the result, then
`verified` (add the date inline in the body, next to the confirmation — see example). This is
the one field `/todo` docs don't need (a todo's presence in the folder *is* its status; a test
doc needs to distinguish "written" from "actually checked").

`links` points at the `/design` doc(s) whose Validation section references this file.

## 3. Numbering

Same `NN_*.md` numeric prefix as `/design` and `/todo`, but here it's just for ordering and a
stable filename — `/test` files don't depend on each other the way design docs can, so there's
no dependency-tier meaning to preserve.

## 4. Lifecycle: written → verified → deleted

A `/test` file is written when a change needs a by-eye check and GameTest genuinely cannot do
it. From there:

- **Verified** — someone ran the steps, confirmed the expected result, flips `status` to
  `verified` and dates it. The file doesn't disappear just because it passed once — it stays as
  the regression script for next time.
- **Deleted** — per its own "Delete this file when" section (required, see §5): typically once
  an automated test replaces it, or the code has been stable long enough that the regression
  risk it was guarding against is gone. Like `/todo`, an empty `/test` folder is a fine,
  unremarkable state — it means nothing currently needs a manual check, not that verification
  was skipped.

Don't let `/test` accumulate stale entries for code that's since been covered by GameTest or
JUnit — that defeats the "slows the process down" complaint this convention exists to fix. If
a check no longer earns its keep, delete it.

## 5. What goes in the body

- **Steps** — exact, runnable (`./gradlew runClient`, then numbered in-game actions). Someone
  who's never touched this feature should be able to follow them without guessing.
- **Expected** — what confirms the fix, stated concretely enough that "yes/no" is unambiguous.
- **Delete this file when** — required. Every `/test` doc names its own retirement condition up
  front, so it doesn't need a separate cleanup pass later to decide whether it's still earning
  its keep.

No Q&A section, no Validation checklist — those belong to `/design`. A `/test` file is a
procedure, not a decision record.

---

## Example

```markdown
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
```
