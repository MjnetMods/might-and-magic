---
type: work-package
role: Infra
status: ready
track: A-1
---

# Work Package: A-1 Modrinth Publish (minotaur)

**Role:** Infra  
**Design doc:** `design/00_infra.md` § Step A-1: Modrinth publish via Gradle (minotaur)  
**Branch:** `claude/infra-a1-minotaur`

---

## Context

NeoForge 1.21.1 Minecraft mod project (`mod_id = mam`, group `org.mjli.mam`). Build system is Gradle with NeoForge toolchain. Goal: wire up the minotaur Gradle plugin so `./gradlew modrinth` can publish the mod JAR to Modrinth.

A reference implementation exists at `/Users/mannil/java/travelpack/build.gradle` — read it before starting. The NeoForge-adapted template is already in `design/00_infra.md` § Step A-1.

---

## File Targets

| File | Change |
|------|--------|
| `build.gradle` | Add `id 'com.modrinth.minotaur' version '2.+'` to the `plugins {}` block |
| `build.gradle` | Add `modrinth {}` block — use template from `design/00_infra.md` verbatim |
| `gradle.properties` | Add `modrinth_project_id=FILL_ME_IN` |
| `CHANGELOG.md` | Create stub with a `## [0.1.0]` section |

---

## Implementation Notes

- `uploadFile = jar` may need to be `jarJar` if the build produces two JARs. Check `build/libs/` — if two JARs exist, the larger one is the jarJar (embeds Registrate). Leave a `// TODO: verify uploadFile` comment in the block so the reviewer can check before first real publish. Do not run the build.
- `modrinth_project_id` is a placeholder — the human fills it in from the Modrinth dashboard after review.
- The `changelog` closure reads `CHANGELOG.md` lazily at task execution time — this is intentional, do not change it to eager evaluation.

---

## Acceptance Criteria

- [ ] `./gradlew build` succeeds with no new errors or warnings from this change
- [ ] `./gradlew tasks --group publishing` lists a `modrinth` task
- [ ] `modrinth {}` block matches the template in `design/00_infra.md` (all fields: token, projectId, versionNumber, versionType, uploadFile, gameVersions, loaders, changelog, dependencies)
- [ ] `CHANGELOG.md` exists with at least one `## [x.y.z]` section
- [ ] `gradle.properties` has `modrinth_project_id=FILL_ME_IN`

---

## Out of Scope

- `.github/` — Track A-2 handles the CI workflow
- `src/` — no source changes
- `site/` — Track B/C
- Any refactoring of existing `build.gradle` content beyond the two targeted additions
