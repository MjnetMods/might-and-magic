---
type: guide
status: wip
last-updated: 2026-07-02
links: ["[[design-doc-guide]]", "[[todo-doc-guide]]", "[[gametest-guide]]", "[[site-guide]]", "[[book-guide]]", "[[ponder-guide]]", "[[verify-man]]", "[[collaboration-stages]]"]
---

# Agent Factory Guide

Format and rollout plan for running the Feature Pipeline (see root `CLAUDE.md`) as a set of
scoped agents instead of one person doing every stage. This is process structure, not a specific
task — see `factory/tasks/` for actual work items.

---

## 1. Roles

One agent role per pipeline stage, minus Design and Merge (see §4 — both stay human for now):

| Role | Pipeline stage | Scope | Reads |
|---|---|---|---|
| **Site Writer** | Site docs | `site/content/` | `[[site-guide]]` |
| **Book Writer** | Book docs | Patchouli book JSON + `patchouli.mam.guide.*` lang keys | `[[book-guide]]` |
| **Coder** | Implement | `src/main/java/`, `src/test/java/` (writes its own tests) | design doc, `[[gametest-guide]]`, `[[registrate-guide]]`, `[[worldgen-guide]]` |
| **Tester** | Test (audit) | `src/test/java/`, GameTest classes, `test/` (last resort) | same design doc, Coder's diff, `[[gametest-guide]]`, `[[verify-man]]` |
| **Ponder** | Ponder | ponder-doc script/spec + scene registration + SNBT + `mam.ponder.*` lang keys | `[[ponder-guide]]`, `[[verify-man]]` |
| **Reviewer** | Review | read-only, whole diff | `code-review` skill conventions |

**Q:** Site docs, book docs, and ponder-doc were originally one "Technical Writer" role covering
all three. Why split it into three charters (`[[site-writer]]`, `[[book-writer]]`, `[[ponder]]`)?
**A:** The generic role produced real mistakes the first time it was actually exercised: wrong
tone (spec-like prose instead of thematic copy) on the site-doc pass, and a Scope pointing at the
wrong path entirely (`data/mam/patchouli_books/` instead of `assets/mam/patchouli_books/`) on the
book-doc pass. A charter that reads only its own format/voice guide, with nothing to guess at
across artifact types, is the fix — same principle as Coder/Tester below, just discovered on the
docs side instead of the impl side. (2026-07-02)

**Q:** Coder and Tester — one agent doing both halves of the implement/test loop per batch (as
root `CLAUDE.md` already prescribes for a human), or two separate agents handing off within the
same stage?
**A:** Both write tests, but not the same tests. Coder always writes its own JUnit/GameTest
coverage alongside the implementation — deferring that to Tester would just bounce the task
between the two roles for no reason, since Coder is the one who knows what it just built. Tester
is a separate, always-on task created once Coder's is `done` (not skipped for "simple" batches):
an audit of that coverage for completeness, closing real gaps, and — as a last resort, only when
something genuinely can't be automated — flagging a manual verification note (`[[verify-man]]`).
Making this checkpoint unconditional means "coverage is adequate" is always a verified conclusion,
not an assumption nobody checked, and Coder can't leave gaps for the human to discover later.
(Revised 2026-07-02 — the original version of this answer had Tester skipped entirely for
mechanical batches; that undercut the actual point of the checkpoint.)

## 2. Charter format

One file per role under `factory/charters/`. Front matter same as a design doc
(`[[design-doc-guide]]` §2), `type: charter`. Body is exactly these sections, in order:

- **Scope** — which paths this agent may write to. Anything outside scope is read-only context.
- **Directive** — what "done" means for this stage. Concrete enough that the agent (or the
  human reviewing the handoff) can check it without judgment calls.
- **Inputs** — which guide docs and which upstream artifact (design doc, previous stage's
  output) it reads before starting.
- **Handoff** — the artifact it leaves behind, and who/what picks it up next. Any role that
  writes files also writes out the git commit command it would run (`git add` + `git commit -m
  "<message>"`) into the task's handoff log — composed, never executed. Lets the human see
  exactly where a change would land before running it themselves.
- **Guardrails** — the specific ways this role could overreach, named explicitly (e.g. "does not
  touch `src/test/java/`", "does not resolve open `Q:` items in the design doc — flags them back
  instead"). Every charter carries **no commit, no push, no merge** as a standing guardrail at
  Manual single-agent fork — stated explicitly in each charter, not just implied by "no commit
  rights yet," so it holds even if a charter is read on its own.

Each charter's **Scope** is backed by matching `Write`/`Edit` allow-rules in the project's
`.claude/settings.json`, keyed to the same paths (`site/content/**` for Site Writer, the Patchouli
book path for Book Writer, `src/main/java/**` and `src/test/java/**` for Coder, `src/test/java/**`
and `test/**` for Tester, `src/ponder/structure/**`, `mam.ponder.*` lang keys, and `test/**` for
Ponder). Without this, a backgrounded charter agent stalls on an interactive permission prompt the
human isn't present to answer — the allow-rule just lets the agent write inside the boundary its
own charter already declares, it doesn't widen that boundary. Git-mutating commands
(`add`/`commit`/`push`/merge) are never added to this allow-list; the no-commit-rights guardrail
above stays enforced at the permission layer too, not just as a written rule an agent could
ignore.

## 3. Task list format

One file per task under `factory/tasks/`, numbered like `/todo` (`[[todo-doc-guide]]` §1, §6 —
same dependency-tier numbering, own namespace). Front matter `type: task`, plus a `gate` field
tracking which pipeline stage it's currently at:

```yaml
---
type: task
gate: site-doc          # site-doc | book-doc | ponder-doc | impl | test | review | merge | done
last-updated: 2026-07-02
links: ["[[23_verdant-generating-flowers]]"]
---
```

That's the full vocabulary across all task *kinds*, not a chain every task's `gate` climbs
end-to-end — each task file starts at one label and ends at `done`, no exceptions. Every stage
transition (site-doc → impl, impl → test, test → ponder-doc, ponder-doc → review, review → merge)
is a **new task file** the human creates once its prerequisite task reaches `done` — see the
impl/test/ponder-doc/review split below for what this looks like in practice. (Earlier versions of
this guide had Coder and Tester sharing one file for the `impl`→`test` handoff — dropped once
Tester became an always-on task rather than a conditional one; see the Coder/Tester Q&A above.)

Body: what the task is, a link to the design doc it's implementing, and a running log of
handoffs (one line per gate transition, who/what did it, when). A master `factory/tasks.md`
index lists all open tasks and their current gate — the tick-off list.

A task file is created only once its design doc is `done` (per `[[design-doc-guide]]` status
vocabulary, §3) — this is the boundary where the factory takes over from you. Promoting a design
forks **two sibling tasks** at once — `site-doc` (picked up by `[[site-writer]]`), `book-doc`
(picked up by `[[book-writer]]`) — both pointing at the same design doc.

`ponder-doc` is **not** a same-day sibling of those two — root `CLAUDE.md`'s Feature Pipeline
places Ponder at stage 5, "once the mechanic is real and testable," strictly after Implement
(stage 4) and, per the Coder/Tester Q&A above, after that implementation's coverage has actually
been audited too. Writing a ponder script/spec before the mechanic exists (or before its test
coverage is verified — Tester might still turn up a real behavior fix) doesn't fit the same
"docs precede code" logic that justifies site-doc/book-doc running ahead of implementation. The
human creates `impl` once site-doc/book-doc are both `done`, then `test` once `impl` is `done`,
then `ponder-doc` (picked up by `[[ponder]]`) once `test` is `done`, then `review` once
`ponder-doc` is `done` — the review pass then covers the finished feature — implementation, its
audited tests, and the tutorial scene together — as a player will actually encounter it, not just
the code diff in isolation. (Corrected 2026-07-02, after the first real run of this pipeline — see
`factory/tasks/03_apothecary-t2-impl.md` through `06_apothecary-t2-review.md` for the task
sequence this produces in practice.)

**Q:** How does a task avoid colliding with another task touching the same file (e.g. two tasks
both registering blocks in `MightAndMagic.java`)?
**A:** Not a concern at Manual single-agent fork (§4) — strictly one task in flight at a time.
Revisit once Chained agents (§4) introduces more than one task running unattended.

**Q:** Does "you review/correct course between each handoff" mean the agent literally pauses and
waits for a resume, or does it keep working other tasks while one waits?
**A:** At Manual single-agent fork, sequential by construction — there's only ever one agent
running, so there's nothing else for it to work on while waiting. Becomes a live question again
at Chained agents, once the human isn't the one initiating each fork. (2026-07-02)

## 4. Rollout phases

Mirrors `[[collaboration-stages]]` — this section is the operational detail behind those stages.

- **Manual single-agent fork (current target):** human forks one agent against one task, no
  commit rights — agent works in the background, human reviews, course-corrects, and commits by
  hand, then forks the next agent for the next task. Fully sequential: exactly one agent running
  at any time, by construction, not by policy.
- **Chained agents, branch commits:** agents get commit rights to a task branch and open a PR at
  the Review gate. An agent's completion creates and hands off the next task itself — the human
  stops re-forking each stage by hand. This is where the two open `Q:` items in §3 (file
  collisions, pause-vs-continue) become live again.
- **Design agent:** Designer becomes an agent too — `todo → design + task` runs unattended, with
  its own escalation policy for open `Q:` items (resolve-and-document by default, escalate only
  when authorization-scoped, expensive to reverse, or genuinely underdetermined — decided in the
  2026-07-02 retro). Review and Merge are the last human-only roles left at this point.
- **Review + merge agent ("final boss"):** an agent reviews and merges its own factory's output.
  Aspirational, not on the near-term roadmap — "merge stays human" has held at every stage so
  far, and revisiting it is an authorization-scoped call for whenever this stage is actually in
  reach, not an assumption made now.

## 5. Prerequisite: existing docs up to spec

The task list (§3) only starts from a design doc marked `done` with no open `Q:` left in it. Any
existing `design/` doc still carrying unresolved `Q:` markers, or any `/todo` item not yet
promoted, isn't eligible to become a factory task until that's resolved — this is why the retro
that produced this guide flagged a docs audit as the actual next step, ahead of running any agent.
