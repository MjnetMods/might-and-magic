---
type: guide
status: wip
last-updated: 2026-07-02
links: ["[[design-doc-guide]]", "[[todo-doc-guide]]", "[[gametest-guide]]", "[[site-guide]]", "[[collaboration-stages]]"]
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
| **Technical Writer** | Site docs, book docs, ponder-doc | `site/content/`, Patchouli book JSON, ponder-doc content in the task file | `[[site-guide]]`, `[[ponder-guide]]` |
| **Coder** | Implement | `src/main/java/` | design doc, `[[gametest-guide]]`, `[[registrate-guide]]`, `[[worldgen-guide]]` |
| **Tester** | Implement (test half) | `src/test/java/`, GameTest classes | same design doc, `[[gametest-guide]]` |
| **Reviewer** | Review | read-only, whole diff | `code-review` skill conventions |

**Q:** Coder and Tester — one agent doing both halves of the implement/test loop per batch (as
root `CLAUDE.md` already prescribes for a human), or two separate agents handing off within the
same stage?
**A:** Coder does the impl+test loop for uniform/mechanical batches (matches the risk-sized
batching already in `CLAUDE.md`). Tester exists as a separate agent only for novel/complex
mechanics, where a second, adversarial pass at test coverage is worth the extra handoff. (2026-07-02)

The ponder-doc (a script/spec for the eventual Ponder scene, written before code same as site and
book docs) is Technical Writer's output, one of the three sibling tasks forked at Task
Introduction — see §3. The actual Ponder *implementation* (pipeline stage 5, the in-game SNBT
scene) has no dedicated role yet — folded into Coder until a task reaches that stage and the
split proves necessary or not.

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
`.claude/settings.json`, keyed to the same paths (`site/content/**` and the Patchouli book path
for Technical Writer, `src/main/java/**` for Coder, `src/test/java/**` for Tester). Without this,
a backgrounded charter agent stalls on an interactive permission prompt the human isn't present to
answer — the allow-rule just lets the agent write inside the boundary its own charter already
declares, it doesn't widen that boundary. Git-mutating commands (`add`/`commit`/`push`/merge) are
never added to this allow-list; the no-commit-rights guardrail above stays enforced at the
permission layer too, not just as a written rule an agent could ignore.

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

Body: what the task is, a link to the design doc it's implementing, and a running log of
handoffs (one line per gate transition, who/what did it, when). A master `factory/tasks.md`
index lists all open tasks and their current gate — the tick-off list.

A task file is created only once its design doc is `done` (per `[[design-doc-guide]]` status
vocabulary, §3) — this is the boundary where the factory takes over from you. Promoting a design
forks **three sibling tasks** at once — `site-doc`, `book-doc`, `ponder-doc` — all pointing at
the same design doc, each independently picked up by Technical Writer. There's no single `impl`
task yet at this point; see §4 for when and how that gets created.

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
