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

### Step A-1: Modrinth publish via Gradle (minotaur)

Port from `/Users/mannil/java` (`travelpack/build.gradle`). Already read — ~15 lines.

| File | Change |
|------|--------|
| `build.gradle` | Add `id 'com.modrinth.minotaur' version '2.+'` to `plugins` block |
| `build.gradle` | Add `modrinth {}` block (see template below) |
| `gradle.properties` | Add `modrinth_project_id=<from Modrinth dashboard>` |
| `CHANGELOG.md` | Create stub — minotaur reads `## [1.0.0]` section for release notes |

**minotaur block template** (port of travelpack, NeoForge-adapted):

```groovy
def changelogText = {
    def content = rootProject.file("CHANGELOG.md").text
    def header = "## [${project.mod_version}]"
    def start = content.indexOf(header)
    if (start < 0) return "No changelog entry for ${project.mod_version}"
    def end = content.indexOf("\n## [", start + 1)
    return end >= 0 ? content.substring(start, end).trim() : content.substring(start).trim()
}()

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    projectId = project.modrinth_project_id
    versionNumber = project.mod_version
    versionType = "release"
    uploadFile = jar          // ⚠ verify: may need jarJar if Registrate embed is in that task
    gameVersions = ["1.21.1"]
    loaders = ["neoforge"]
    changelog = changelogText
    dependencies {
        optional.project "patchouli"
    }
}
```

**JAR task note:** Run `./gradlew build` and check `build/libs/` — if there are two JARs (plain + jarJar), use the larger one (the jarJar output includes embedded Registrate). Update `uploadFile` accordingly.

### Step A-2: GitHub Actions publish workflow

New file: `.github/workflows/publish.yml`

- Trigger: `push` on tags matching `v*`
- Steps:
  1. Checkout + setup Java 21
  2. `./gradlew build`
  3. `gh release create ${{ github.ref_name }}` — attach JAR, use CHANGELOG.md section as body
  4. `./gradlew modrinth`
- Secrets required in repo settings: `MODRINTH_TOKEN`, `GITHUB_TOKEN` (auto-provided)

Reference: `best/site/.github/workflows/build.yml` — strip the CV download step, swap Hugo build for Gradle build.

### Verification

- [ ] `./gradlew modrinth` locally with `MODRINTH_TOKEN` env var set → file appears on Modrinth project page
- [ ] Push a `v0.0.1-test` tag → GitHub Release created, Modrinth upload triggered

---

## Track B: Doc Site

**Goal:** Player-facing wiki at a GitHub Pages URL, auto-deployed on push to `main`. Hidden `/dev/` section for WIP and design notes.

### Structure

Lives in `mam/site/` — same repo so docs stay in sync with code changes.

```
site/
  hugo.toml               # baseURL, theme: blowfish
  content/
    getting-started/      # player onboarding (public)
    verdant-path/         # blocks, items, recipes, mechanics (public)
    dev/                  # draft: true — hidden from nav, accessible by direct URL
      roadmap.md
      design-notes.md     # links back to design/ docs
  static/
  themes/
    blowfish/             # git submodule from studio-m
```

### Content source

`docs/verdant-path.md` and `design/21_verdant-implementation-status.md` are the source of truth. Site content mirrors them — not a copy, these pages link to or summarise the tracked state.

Feedback: GitHub Issues link in site footer and in `dev/` index.

### Deployment

New file: `.github/workflows/site.yml`

- Trigger: push to `main`
- Working dir: `site/`
- Steps: setup Hugo → `hugo --minify --source site` → upload artifact → deploy to GitHub Pages
- Reference: `best/site/.github/workflows/build.yml` (strip CV download step)

### Verification

- [ ] `cd site && hugo server` — local preview works
- [ ] Push to `main` → GitHub Pages URL serves player content
- [ ] `/dev/getting-started` accessible by direct URL, not in nav

---

## Status

| Item | Status |
|------|--------|
| A-1: minotaur plugin | ⬜ planned |
| A-2: publish workflow | ⬜ planned |
| B: site scaffold | ⬜ planned |
| B: blowfish theme wired | ⬜ planned |
| B: verdant-path content | ⬜ planned |
| B: deploy workflow | ⬜ planned |
