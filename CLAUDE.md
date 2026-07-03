# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Collaboration rules

- **Explain before acting:** Always state what you are about to do and why before running any tool. No silent tool calls.
- **Short loops:** After each finding, check in before the next step. Don't chain multiple investigation steps without pausing.
- **Fork research:** If a question requires more than 2 file reads to answer, fork it — don't fill the main conversation with raw output.
- **Continuous improvement is the top priority:** When working patterns break down, stop and fix the process before continuing the task.

## Feature pipeline

New content (a flower, a mechanic, a school feature) moves through five stages, in order:

1. **Design** — write the design doc first (see `ref/design-doc-guide.md`). Surface tradeoffs as `Q:`/`A:` pairs in the doc itself, not just in chat — a decision resolved only in conversation is lost the moment the session ends.
2. **Site docs** (`site/content/`) — sell the feature to the player. Written before implementation exists, describing the intended behavior.
3. **In-game docs** — the Patchouli book entry. Also written before implementation, alongside the site docs.
4. **Implement — as an implement/test loop, sized to match risk.** Don't build an entire batch (e.g. all 6 flowers' mechanics) and then test everything at the end — that's how tests get skipped under momentum. But the loop's unit doesn't have to be "one flower" either: uniform, low-risk, mechanical work (e.g. the recipe JSON + recipe-matching test for every flower in a batch) can be done together as one pass, since there's little to go wrong between instances. Novel or complex mechanics (a new trigger condition, unusual state tracking) should be looped one at a time, since that's where bugs actually hide. Pick the batch size the risk justifies, then implement → test → confirm before moving to the next batch.

   **JUnit vs GameTest — pick the right one, not whichever is habitual:**
   - **JUnit** (`src/test/java/`, `./gradlew test`) — pure logic that runs in a bare JVM with no live Minecraft server: math/formulas, state machines, NBT round-trips on a directly-instantiated object. Existing examples: `ManaPoolTest`, `EnergyNetworkHandlerTest`. Fast, no world needed — prefer this whenever the thing under test doesn't actually require a running world.
   - **GameTest** (`src/main/java/org/mjli/mam/infrastructure/gametest/tests/`, `./gradlew runGameTestServer`) — anything needing a real world: block placement, block entity ticking in-world, entity spawning/collision, capability interaction across blocks, recipe matching against a live `RecipeManager`. Read `ref/gametest-guide.md` before writing one. Existing examples: `TestGeneratingFlowers`, `TestApothecary`.
   - A single mechanic often needs both: JUnit for its internal math/state, GameTest for its in-world trigger/placement behavior. Don't force one to cover what the other is better suited for.
5. **Ponder** — polish pass, in-game tutorial scene, once the mechanic is real and testable.

Docs precede code deliberately: writing the site/book copy before implementation forces the design to be concrete enough to explain to a player, and catches gaps (missing mechanic, unclear recipe) before they're baked into code.

## Project

**Might And Magic** (`mam`) — a Minecraft 1.21.1 mod built on NeoForge 21.1.234, using Java 21. Currently scaffolded from the NeoForge MDK template with placeholder content.

## Commit convention

This repo uses **conventional commits**. Prefix every commit message:

| Prefix | Use for |
|--------|---------|
| `feat:` | New blocks, items, mechanics, content |
| `fix:` | Bug fixes |
| `refactor:` | Code restructuring, no behavior change |
| `perf:` | Performance improvements |
| `test:` | GameTests |
| `docs:` / `design:` | Design docs, site content, CLAUDE.md |
| `chore:` / `build:` / `ci:` | Gradle, workflows, tooling |

Releases are tagged `vX.Y.Z`. `git-cliff` reads these prefixes to generate the changelog automatically.

## Commands

```bash
# Build the mod JAR
./gradlew build

# Run the Minecraft client with the mod loaded
./gradlew runClient

# Run a dedicated server with the mod loaded
./gradlew runServer

# Run data generators (outputs to src/generated/resources/)
./gradlew runData

# Run game tests
./gradlew runGameTestServer

# Refresh dependency cache when things break
./gradlew --refresh-dependencies

# Clean build outputs (does not affect source)
./gradlew clean
```

Built JAR lands in `build/libs/`. Run configurations write their working directories under `run/<configName>/`.

Headless tasks (`compileJava`, `runData`, `runGameTestServer`, `build`) are fine to run directly —
no need to ask first. Launching the interactive client (`runClient`) stays the user's own action —
prompt them to run it, naming the exact command and what to check/verify, rather than attempting
it directly.

## Architecture

### Entry points

- **`MightAndMagic.java`** — the `@Mod` main class. All `DeferredRegister` objects for blocks, items, and creative tabs live here as static fields. Registers to both the mod event bus (for mod lifecycle events like `FMLCommonSetupEvent`) and `NeoForge.EVENT_BUS` (for game events like `ServerStartingEvent`).
- **`MightAndMagicClient.java`** — client-only `@Mod` class (`dist = Dist.CLIENT`). Safe to access client APIs here. Uses `@EventBusSubscriber` to auto-register static `@SubscribeEvent` methods on the mod bus.
- **`Config.java`** — NeoForge `ModConfigSpec`-based config. Registered as `ModConfig.Type.COMMON` in the main constructor. Config values are static fields accessed directly.

### Registration pattern

NeoForge uses **Deferred Registers** — declare a `DeferredRegister` for a registry type, register objects into it with `.register()`/`.registerSimple*()`, then call `deferredRegister.register(modEventBus)` in the mod constructor. Do this for every new registry type (entities, sounds, data components, etc.).

### Mixins

Mixin config is at `src/main/resources/mam.mixins.json`; mixin classes go in `org.mjli.mam.mixin`. The mixins array in the JSON is currently empty.

### Resources

- `src/main/resources/` — hand-authored assets and data
- `src/generated/resources/` — output of `runData` (data generators); gitignored, run `runData` before building on a fresh clone
- Translations: `src/main/resources/assets/mam/lang/en_us.json`
- Mod metadata: `src/main/resources/META-INF/neoforge.mods.toml` — uses `${property}` expansion from `gradle.properties`

### Key version properties (`gradle.properties`)

| Property | Value |
|---|---|
| `minecraft_version` | 1.21.1 |
| `neo_version` | 21.1.234 |
| `mod_id` | mam |
| `mod_group_id` | org.mjli.mam |

### Registrate tab population

All domain classes (`VerdantFlowers`, `VerdantRock`, etc.) expose `appendToTab(CreativeModeTabModifier)`.
Items **must** be added with `CreativeModeTab.TabVisibility.PARENT_TAB_ONLY`:

```java
modifier.accept(BLOCK.asStack(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
```

Using the default `PARENT_AND_SEARCH_TABS` causes a crash: Registrate's `defaultCreativeModeTab = SEARCH`
mechanism also adds items to SEARCH, so SEARCH ends up with each item twice.

### NeoForge docs

Community docs: https://docs.neoforged.net/

### Docs layout

| Folder | Purpose |
|--------|---------|
| `design/NN_*-path.md` | Creative / system design per magic path |
| `design/N1_*-implementation-status.md` | In-game verification checklist per path |
| `design/N2_*-test-plan.md` | Test coverage tracking per path |
| `design/magic/NN_*.md` | Cross-school magic infrastructure (energy, Apothecary, Altar, Weavery, runes) — shared by all paths, not owned by one. Numbered loosely by dependency, not strict reading order. |
| `todo/NN_*.md` | Pre-design ideas and tasks-to-validate, one per file. Promote into `design/` once work starts (fold into the relevant doc or start a new numbered one), then delete the todo file. |
| `test/NN_*.md` | Manual/visual regression checks GameTest can't cover (renderer output, in-game feel) — one small file per check: steps + expected result. Link to it from a design doc's Validation line the same way you'd link to a GameTest class. Disposable like `/todo`: delete once no longer needed (automated coverage lands, or the risk has passed), not tracked forever by default. See `ref/verify-man.md`. |
| `ref/design-doc-guide.md` | Generic design-doc format (front matter, status vocabulary, Q&A decisions, Validation items) — applies to `design/`, portable to other projects |
| `ref/todo-doc-guide.md` | Format for `/todo` — reuses design-doc-guide's numbering/dependency rules, adds the idea→design promotion lifecycle |
| `ref/verify-man.md` | Format for `/test` — one file per manual/visual regression check GameTest can't cover, `pending`/`verified` status, required "delete this file when" clause |
| `ref/gametest-guide.md` | NeoForge GameTest reference — read this before writing any `@GameTest` |
| `ref/ponder-guide.md` | Ponder (Create's in-game tutorial system) reference — deps, scenes, SNBT format, localization |
| `ref/site-guide.md` | Hugo site authoring — crafting shortcode usage, texture paths, running locally |
| `ref/book-guide.md` | Patchouli book authoring — entry/category JSON structure, lang-key content split, voice conventions |
| `ref/texture-guide.md` | NeoForge/Minecraft texture-atlas conventions — e.g. why shared block-model textures must live under `textures/block/`, not `textures/misc/` |
| `ref/registrate-guide.md` | NeoForge Registrate usage patterns — block-type/item-model strategy, fence/wall inventory models, slab loot tables, datagen vs. hand-authored recipes |
| `ref/worldgen-guide.md` | NeoForge worldgen reference — how MAM places mystical flowers/mushrooms, one custom `Feature` type in Java with the rest in JSON datapacks |
| `site/content/` | Player-facing documentation (source of truth — `docs/` removed) |
| `factory/agent-factory-guide.md` | Multi-agent pipeline reference — roles, charters, task-file format, rollout phases, and where each doc type's responsibility ends (design vs. `todo`/`test`/task handoff logs). Read before creating or picking up a `factory/tasks/*.md` file. |
| `factory/charters/*.md` | One charter per agent role (Coder, Tester, Reviewer, Site Writer, Book Writer, Ponder) — scope, directive, handoff rules, guardrails. |
| `factory/tasks/*.md` | One file per unit of pipeline work, numbered like `/todo`. Front matter `gate` tracks pipeline stage; body is a running handoff log. |
| `factory/tasks.md` | Master index of open tasks and their current gate. |
| `factory/collaboration-stages.md` | Maturity ladder for how much of the Feature Pipeline runs as agents vs. by hand — see `factory/agent-factory-guide.md` for the current stage. |

Legacy tracking files (pre-migration, not actively updated — see `ref/design-doc-guide.md` for the
current convention: per-doc inline `## Validation` sections):
- `design/21_verdant-implementation-status.md`
- `design/22_verdant-test-plan.md`

### Flower identity & Botania compat

MAM treats its flowers as the **same flowers** as Botania — shared furniture in the Minecraft universe, not a rename or reimagining. Visual identity, names, and tags are kept compatible with Botania. Mechanics differ.

**Policy:**
- Never rename flower block IDs to diverge from Botania naming (`<color>_mystical_flower`, `<color>_tall_mystical_flower`, `pure_daisy`, etc.)
- Always write MAM flower blocks into the relevant `botania:` namespace tags so they are recognised if both mods are loaded
- Texture strategy: originals are wiiv's 16×16 Botania textures, upscaled 2x (nearest-neighbor, no interpolation) directly into the final asset path. Hand-edit in Aseprite as bandwidth permits — ship the 2x upscale if no hand-edit exists yet, it's better than raw 16×16.

**Botania tags to maintain** (files under `data/botania/tags/block/`):

| File | Covers |
|------|--------|
| `mystical_flowers.json` | All 16 `mam:<color>_mystical_flower` blocks |
| `double_mystical_flowers.json` | All 16 `mam:<color>_tall_mystical_flower` blocks |
| `generating_special_flowers.json` | `mam:daybloom`, `mam:endoflame`, `mam:hydroangeas` — add new generating flowers here |

Add entries to the relevant file whenever a new flower block is registered. Do not add functional flowers (no mana generation) to `generating_special_flowers`. Full name↔tag mapping: `design/24_verdant-flowers-botania-compat.md`.

### Rune identity & Botania compat

Runes (`design/magic/25_runes.md`) are MAM-native — no dependency on Botania runes. Separately, additive Botania compat: all `mam:rune_*` items go into `data/botania/tags/items/runes.json` (Botania's single flat `runes` tag — it has no per-concept tags, so this is "recognised as a rune", not a semantic pairing). Add new rune items to this file the same session they're registered. Full name↔tag mapping and placeholder-art provenance: `design/magic/26_runes-botania-compat.md`.

**Attribution:** Original artwork by wiiv (Botania). See `site/content/credits/` and `neoforge.mods.toml`.

### Ref on disk

This project: `/Users/mannil/mcmod/mam`

Reference source code:
- Botania → `/Users/mannil/mcmod/Botania`
- Create → `/Users/mannil/mcmod/Create`
- Malum-Mod → `/Users/mannil/mcmod/Malum-Mod`
- Ponder → `/Users/mannil/mcmod/Ponder`


