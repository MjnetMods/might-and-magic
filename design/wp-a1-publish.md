---
type: work-package
role: Infra
status: ready
track: A-1
---

# Work Package: A-1 Dual Publish (mod-publish-plugin)

**Role:** Infra  
**Design doc:** `design/00_infra.md` § Step A-1: Dual publish via Gradle (mod-publish-plugin)  
**Branch:** `claude/infra-a1-publish`

---

## Context

NeoForge 1.21.1 Minecraft mod project (`mod_id = mam`, group `org.mjli.mam`). Build system is Gradle with NeoForge toolchain. Goal: wire up `mod-publish-plugin` so `./gradlew publishMods` can publish the mod JAR to both Modrinth and CurseForge in one step.

Reference implementation: `/Users/mannil/java/build.gradle` — read it before starting. The NeoForge-adapted template is in `design/00_infra.md` § Step A-1.

---

## File Targets

| File | Change |
|------|--------|
| `build.gradle` | Add `id 'me.modmuss50.mod-publish-plugin' version '2.1.1'` to the `plugins {}` block |
| `build.gradle` | Add `publishMods {}` block — use template from `design/00_infra.md` verbatim |
| `gradle.properties` | Add `modrinth_project_id=FILL_ME_IN` and `curseforge_project_id=FILL_ME_IN` |
| `CHANGELOG.md` | Create stub with a `## [0.1.0]` section |

---

## Implementation Notes

- `file = jar.archiveFile` may need to be `jarJar.archiveFile` if the build produces two JARs. Check `build/libs/` — if two JARs exist, the larger one is the jarJar (embeds Registrate). Leave a `// TODO: verify file` comment in the block so the reviewer can check before first real publish. Do not run the build.
- `file` takes a `Provider<RegularFile>` — use `jar.archiveFile`, not the bare `jar` task reference. This differs from the Fabric/travelpack pattern.
- `modrinth_project_id` and `curseforge_project_id` are placeholders — the human fills them in from the respective dashboards after review.
- The `changelogText` closure reads `CHANGELOG.md` lazily at task execution time — this is intentional, do not change it to eager evaluation.

---

## Acceptance Criteria

- [ ] `./gradlew build` succeeds with no new errors or warnings from this change
- [ ] `./gradlew tasks --group publishing` lists `publishMods`, `publishModrinth`, and `publishCurseforge` tasks
- [ ] `publishMods {}` block matches the template in `design/00_infra.md` (both `modrinth` and `curseforge` subblocks present)
- [ ] `CHANGELOG.md` exists with at least one `## [x.y.z]` section
- [ ] `gradle.properties` has both `modrinth_project_id=FILL_ME_IN` and `curseforge_project_id=FILL_ME_IN`

---

## Out of Scope

- `.github/` — Track A-2 handles the CI workflow
- `src/` — no source changes
- `site/` — Track B/C
- Any refactoring of existing `build.gradle` content beyond the two targeted additions
