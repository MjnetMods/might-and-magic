# Design Doc Format Guide

A generic format for design/decision docs — front matter, a shared status vocabulary, and inline
item markers a checker script can parse. This is structure only; it says nothing about what to
design. Portable across projects, not tied to any one codebase.

---

## 1. One doc, one topic

Prefer several small, focused docs over one large one. Split when a doc needs more than one
top-level heading, or runs noticeably longer than sibling docs covering the same kind of thing —
both are signals it's picked up more than one concern. This applies to any oversized content
within a doc, not just prose: an overgrown Validation checklist (§4) splits into its own doc the
same way plain text does.

## 2. Front matter

```yaml
---
type: design            # free text — design, infra, decision-log, whatever fits
status: wip
last-updated: 2026-07-01
links: ["[[other-doc]]", "[[another-doc]]"]
---
```

`status` describes the maturity of **this document's content** — not whether the thing it
describes is built. Track build/verification separately with Validation items (§4).

`links` is a manually-maintained backlink list (`[[wikilink]]`-style, if your tooling supports
it). Not validated by hand — a checker script can confirm targets actually exist.

## 3. Status vocabulary

Four values, used everywhere a status is needed — front matter, Validation items, anywhere else:

| Value | Meaning |
|---|---|
| `todo` | Not started |
| `wip` | In progress |
| `done` | Finished |
| `blocked` | Can't proceed — note what it's blocked on |

Aliases are fine where a domain wants different words for the same states — e.g. Validation
items reading `broken`/`passed` instead of `wip`/`done`. Default: leave `todo`/`blocked` as-is
and alias only the pair that reads awkwardly in context; write the alias table down once, where
it's introduced.

## 4. Four kinds of content

Everything in a doc is one of:

- **Design** — the actual proposal. This is the doc's default prose; no marker needed. It
  describes the system/mechanic being designed — not the process of writing the doc itself (what
  wasn't documented before, what order things were discovered in, why a gap existed). If content
  is missing because something shipped without a design pass, the fix is backfilling the real spec
  from existing implementation/behavior, not a sentence explaining the gap.
- **Open Question** — something unresolved. Mark it so it's findable regardless of where it
  sits in the doc:
  ```
  **Q:** Should the cache evict on write or on a timer?
  ```
- **Recorded Decision** — a question that's been answered. Don't tick a box next to the
  original text — pair the answer with the question that prompted it, so the record explains
  itself without needing surrounding prose to make sense:
  ```
  **Q:** Should the cache evict on write or on a timer?
  **A:** Timer, 30s. Write-driven eviction thrashed under bursty load in testing. (2026-06-10)
  ```
  Closing a question *is* turning it into this pair — never just a checkbox.
- **Validation** — a checklist item tracking real-world verification (a test, a manual check,
  a QA pass), tagged with the status vocabulary from §3 and an optional link to how it's
  verified:
  ```
  - `todo` — cache returns fresh value after eviction
  - `done` — cache returns stale value before eviction fires ([test](../test/CacheTest.java#L42))
  - `blocked` — concurrent-write case ([manual steps](./manual-qa.md#concurrent-write))
  ```
  Use a backtick tag, not a `[status]` bracket or a `- [ ]` checkbox: brackets collide with
  markdown's own `[text](url)` link syntax the moment a verification link sits on the same line,
  and a real checkbox only has two states while this vocabulary has four. A code span has no
  competing meaning, so it stays unambiguous regardless of what else is on the line.

These are **items**, not sections — put them wherever they're relevant in the doc rather than
collecting them all at the bottom. A checker script only needs the marker, not the position.

## 5. Headings

Single top-level heading per doc, everything else nested under it. Needing a second top-level
heading partway through is the split signal from §1.

## 6. Folders are namespaces, prefixes are dependency tiers

A numeric prefix (`00_`, `10_`, `20_`...) marks a **dependency tier**, not a strict sequence —
lower tiers are depended on by higher ones. Docs sharing the same number are peers: same tier,
no dependency between them.

Prefixes are always plain digits, no letters — so a checker can validate them with a single
simple pattern. Width isn't fixed: use more digits when you need the room, including to suggest
a reading order between peers without implying a dependency — give them nearby-but-different
numbers (`300_`, `301_`) rather than tying them at the same one. A true tie means "no order
implied"; if order matters, they aren't actually tied.

Each folder is its own namespace: a subfolder restarting its own low numbers is correct, not an
inconsistency, because the folder itself supplies the grouping context.

## 7. Not enforced by hand — use a checker

None of the above holds up without something that notices when it's broken. A script can check,
mechanically: front matter present, `status` value in the allowed set, exactly one top-level
heading, `[[wikilink]]` targets resolve to a real file, filename prefixes are pure digits, and —
since items carry fixed markers — count open `Q:` entries per doc as a "how unresolved is this"
signal.

---

## Example

```markdown
---
type: design
status: wip
last-updated: 2026-07-01
links: ["[[00_foundational-system]]"]
---

# Widget Cache

Widgets are expensive to compute, so results are cached after first access.

## Eviction

**Q:** Should the cache evict on write or on a timer?
**A:** Timer, 30s. Write-driven eviction thrashed under bursty load in testing. (2026-06-10)

## Concurrency

**Q:** What happens to a reader mid-eviction?

## Validation

- `done` — cache returns stale value before eviction fires ([test](../test/CacheTest.java#L42))
- `todo` — cache returns fresh value after eviction
- `broken` — cold-start miss returns null instead of computing
- `blocked` — concurrent-write case (blocked on the concurrency question above)
```
