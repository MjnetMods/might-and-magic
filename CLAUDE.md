# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Collaboration rules

- **Explain before acting:** Always state what you are about to do and why before running any tool. No silent tool calls.
- **Short loops:** After each finding, check in before the next step. Don't chain multiple investigation steps without pausing.
- **Fork research:** If a question requires more than 2 file reads to answer, fork it — don't fill the main conversation with raw output.
- **Continuous improvement is the top priority:** When working patterns break down, stop and fix the process before continuing the task.

## Project

**Might And Magic** (`mam`) — a Minecraft 1.21.1 mod built on NeoForge 21.1.234, using Java 21. Currently scaffolded from the NeoForge MDK template with placeholder content.

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
| `ref/gametest-guide.md` | NeoForge GameTest reference — read this before writing any `@GameTest` |
| `site/content/` | Player-facing documentation (source of truth — `docs/` removed) |

Current active tracking files:
- `design/21_verdant-implementation-status.md`
- `design/22_verdant-test-plan.md`

### Ref on disk

This project: `/Users/mannil/mcmod/mam`

Reference source code:
- Botania → `/Users/mannil/mcmod/Botania`
- Create → `/Users/mannil/mcmod/Create`
- Malum-Mod → `/Users/mannil/mcmod/Malum-Mod`
- Ponder → `/Users/mannil/mcmod/Ponder`


