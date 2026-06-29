---
type: infra
status: planned
last-updated: 2026-06-28
links: "[[21_verdant-implementation-status]]"
---

# Infrastructure

Cross-cutting delivery concerns: publishing the mod and hosting player-facing docs.

---

## Track A: Mod Publish Pipeline

**Goal:** `git tag v1.0.0 && git push --tags` → GitHub Release + Modrinth upload, automated.

### Step A-1: Dual publish via Gradle (mod-publish-plugin)

Port from `/Users/mannil/java/build.gradle`. Publishes to Modrinth and CurseForge in one task.

| File | Change |
|------|--------|
| `build.gradle` | Add `id 'me.modmuss50.mod-publish-plugin' version '2.1.1'` to `plugins` block |
| `build.gradle` | Add `publishMods {}` block (see template below) |
| `gradle.properties` | Add `modrinth_project_id=FILL_ME_IN` and `curseforge_project_id=FILL_ME_IN` |
| `CHANGELOG.md` | Create stub — changelog closure reads `## [x.y.z]` section for release notes |

**publishMods block template** (port of travelpack, NeoForge-adapted):

```groovy
def changelogText = {
    def content = rootProject.file("CHANGELOG.md").text
    def header = "## [${project.mod_version}]"
    def start = content.indexOf(header)
    if (start < 0) return "No changelog entry for ${project.mod_version}"
    def end = content.indexOf("\n## [", start + 1)
    return end >= 0 ? content.substring(start, end).trim() : content.substring(start).trim()
}()

publishMods {
    file = jar.archiveFile   // ⚠ verify: may need jarJar if Registrate embed is in that task
    changelog = changelogText
    type = STABLE
    modLoaders.add("neoforge")

    modrinth {
        projectId = project.modrinth_project_id
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        minecraftVersions.add(project.minecraft_version)
        optional { slug = "patchouli" }
    }

    curseforge {
        projectId = project.curseforge_project_id
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        minecraftVersions.add(project.minecraft_version)
        client = true
        server = true
        optional { slug = "patchouli" }
    }
}
```

**JAR task note:** Run `./gradlew build` and check `build/libs/` — if there are two JARs (plain + jarJar), use the larger one (the jarJar output includes embedded Registrate). Update `file` accordingly. Note: `file` takes a `Provider<RegularFile>` (`jar.archiveFile`), not the bare task reference used in Fabric builds.

### Step A-2: GitHub Actions publish workflow

New file: `.github/workflows/publish.yml`

- Trigger: `push` on tags matching `v*`
- Steps:
  1. Checkout + setup Java 21
  2. `./gradlew build`
  3. `gh release create ${{ github.ref_name }}` — attach JAR, use CHANGELOG.md section as body
  4. `./gradlew publishMods`
- Secrets required in repo settings: `MODRINTH_TOKEN`, `CURSEFORGE_TOKEN`, `GITHUB_TOKEN` (auto-provided)

Reference: `best/site/.github/workflows/build.yml` — strip the CV download step, swap Hugo build for Gradle build.

### Verification

- [ ] `./gradlew publishModrinth` locally with `MODRINTH_TOKEN` env var set → file appears on Modrinth project page
- [ ] `./gradlew publishCurseforge` locally with `CURSEFORGE_TOKEN` env var set → file appears on CurseForge project page
- [ ] Push a `v0.0.1-test` tag → GitHub Release created, both platform uploads triggered

---

## Status

| Item | Status |
|------|--------|
| A-1: mod-publish-plugin (dual publish) | ⬜ planned |
| A-2: publish workflow | ⬜ planned |

_Site and recipe display tracked in `design/00_site.md`._

---

## Agent Roles

Roles are defined by **file scope constraints**, not personality. Each agent is given a work package brief that specifies which files it can touch. Touching files outside scope is the primary failure mode to guard against.

| Role | File Scope | When to Use |
|------|-----------|-------------|
| **Dev** | `src/main/java/` | Feature implementation, new blocks/items/mechanics |
| **Test** | `src/main/java/.../gametest/` | GameTest coverage for a completed feature |
| **Site** | `site/` | Hugo content, shortcodes, CSS |
| **Infra** | `build.gradle`, `.github/`, `gradle.properties` | CI, publish pipeline, Gradle tasks |

### Work Package Format

```
Role: <Dev|Test|Site|Infra>
Design doc: design/<NN_filename.md> § <Section heading>
File targets:
  - <path/to/file> — <what to change>
Acceptance criteria:
  - [ ] <verifiable check>
  - [ ] <verifiable check>
Out of scope: <explicit list of what NOT to touch>
```

### When to spawn vs. stay inline

Spawn when:
- Task is file-disjoint from current work (no merge conflicts possible)
- Acceptance criteria are verifiable with `git diff` + checklist
- Task will take more than ~10 tool calls (no value in filling main context)

Stay inline when:
- Task requires back-and-forth decisions
- Design is still exploratory — criteria not yet clear
- Task is small enough that agent overhead isn't worth it

---

## Handoff Protocol

After an agent completes:

1. **Review the diff** — `git diff main..claude/<branch>`
2. **Verify checklist** — tick off each acceptance criterion from the work package
3. **Merge**:
   ```bash
   git switch main
   git merge --no-ff claude/<branch>   # preserves agent commits as a group in log
   git branch -d claude/<branch>
   git worktree remove <worktree-path>
   ```
4. **Update status table** above (or the relevant `design/NN_*` tracking doc)

Use `gh pr create --head claude/<branch>` instead of direct merge when you want CI to run on the branch before it lands on main.
