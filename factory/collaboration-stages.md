---
type: decision-log
status: wip
last-updated: 2026-07-02
links: ["[[agent-factory-guide]]"]
---

# Collaboration Stages

The stages this project's human/agent workflow has passed through, in order, and the two still
ahead. Each stage solves a problem the previous one couldn't — understanding the ladder matters
more than any single rung, since the top rung (factory) only holds if the ones under it do.

## Stages

- `done` — **Learn to code.** Solo authorship, no AI assistance. Baseline: the ability to judge
  whether generated code is correct, not just whether it runs — everything above this stage
  depends on that judgment still being exercised, just less constantly.
- `done` — **Pair with Claude.** Interactive, session-based — one person, one model, one
  conversation. Still the default mode for most work in this repo; every stage below adds
  structure around this, none replaces it.
- `done` — **Design + ref + todo.** Formalized design-first: `design/` docs carrying `Q:`/`A:`
  tradeoffs, `ref/` format guides, `todo/` for pre-design ideas. Solves what pairing alone
  doesn't — a decision resolved only in conversation is lost the moment the session ends;
  writing it into the doc's own `Q:`/`A:` makes it durable.
- `wip` — **Task introduction.** Promoting a `/todo` item still means the Designer (human +
  Claude, pairing) folds it into the design doc — but promotion now also forks the task files
  that follow: one each for site-doc, book-doc, and ponder-doc. Solves what a design doc alone
  doesn't (a feature description isn't a checked-off build plan), and gives later stages a
  concrete unit — the task file — to fork an agent against.
- `todo` — **Manual single-agent fork.** Human forks one agent against one task at a time — no
  commit rights, agent works in the background, human reviews, course-corrects, and commits by
  hand before forking the next agent for the next task. Fully sequential, fully supervised:
  proves the charter/task format actually works before anything runs unattended.
- `todo` — **Chained agents, branch commits.** Once the manual fork proves out: agents get
  commit rights to a task branch, and an agent's completion creates and hands off the next task
  itself — the human stops re-forking each stage by hand.
- `todo` — **Design agent.** The Designer role becomes an agent too: `todo → design + task` runs
  unattended. Review and Merge are the last human-only roles left at this point.
- `todo` — **Review + merge agent ("final boss").** An agent reviews and merges its own
  factory's output. Aspirational, not on the near-term roadmap — revisiting "merge stays human"
  (agreed for every earlier stage) is exactly the kind of authorization-scoped call that needs
  deliberate re-examination when this stage is actually in reach, not an assumption made now.

See `[[agent-factory-guide]]` §3–§4 for the mechanics behind the last four stages.

## Why the order matters

Each stage assumes the one below it holds, so skipping a rung doesn't skip the problem it
solves — it just defers the problem to whichever stage comes after. Handing tasks to agents
before there's a task list to hand them means there's nothing to hand off. A task list built on
design docs that still carry open `Q:` gaps pushes that ambiguity downstream into agents that
were never meant to resolve it. Jumping straight from Pairing to Factory would mean giving agents
scope and judgment calls that no one — human or otherwise — ever wrote down and agreed to first.
