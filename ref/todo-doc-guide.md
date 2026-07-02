# Todo Doc Format Guide

Format for `/todo` — one file per idea or pre-design task, ordered and grouped the same way
`/design` is. See [[design-doc-guide]] for the shared rules this reuses; only what's different
for todos is written here.

---

## 1. Same numbering, same one-topic rule

`/todo` reuses design-doc-guide.md §1 (one doc, one topic) and §6 (numeric prefix = dependency
tier, not sequence) unchanged. A todo numbered `20_` depends on nothing a `10_` todo doesn't
already cover, same as design docs.

`/todo` is its own namespace (§6) — its numbers don't need to line up with `/design`'s numbers
for the same subject, they just need to hold true within `/todo`.

## 2. Front matter

```yaml
---
type: todo
last-updated: 2026-07-02
links: ["[[20_verdant-path]]"]
---
```

No `status` field — a file's presence in `/todo` *is* its status. There's only one state a todo
doc can be in that design-doc-guide's four-value vocabulary would call `todo`; once work starts,
promote it (§3) rather than flipping a field.

`links` points at the `/design` doc(s) the idea relates to or would extend — same wikilink
convention as design docs.

## 3. Lifecycle: idea → design, then delete

A todo doc is written once, then either:

- **Promoted** — work starts. Fold its content into the `/design` doc it extends (the usual
  case, per `links`), or give it a new numbered doc if it's a genuinely new topic. Either way,
  delete the todo file in the same change — it's redundant the moment its content lives in
  `/design`.
- **Dropped** — the idea doesn't pan out. Delete it. No graveyard folder; git history is the
  record.

An empty `/todo` folder is a fine, unremarkable state, not a signal something's missing.

## 4. What goes in the body

Whatever's needed to pick the idea back up later: what it is, why it matters, what it depends
on. No Validation checklist (design-doc-guide §4) — that only makes sense once something is
built enough to verify. If a todo needs an open question answered before it's promotable, use
design-doc-guide's `Q:`/`A:` markers — they carry over unchanged.

---

## Example

```markdown
---
type: todo
last-updated: 2026-07-02
links: ["[[20_verdant-path]]"]
---

# Pollen Drift Weather Effect

Wind should visibly carry pollen particles between mystical flowers during Verdant weather
events, as a readability cue for cross-pollination range.

**Why:** playtesters can't tell which flowers are in range of each other without opening
debug overlays.

**Q:** Client-only particle effect, or does it need to reflect real pollination state?
```
