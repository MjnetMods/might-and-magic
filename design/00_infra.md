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

## Track C: Recipe Display

**Goal:** Render crafting/processing recipes in Hugo pages using mod textures — no screenshots, no external tools, auto-synced.

### Step C-1: Texture sync (Gradle task)

Add a Gradle task `syncTextures` that copies item and block textures into `site/static/textures/`:

```groovy
tasks.register('syncTextures', Copy) {
    from 'src/main/resources/assets/mam/textures'
    into 'site/static/textures'
    include '**/*.png'
}
```

Wire it so `syncTextures` runs automatically before `hugo` in CI (Track B deploy workflow).

**Scope:** only `assets/mam/textures/` — vanilla textures are not bundled; shortcode falls back to a blank slot for missing images.

### Step C-2: Crafting table shortcode

New file: `site/layouts/shortcodes/crafting.html`

- Accepts `in` (9-slot string, `|`-separated rows, `,`-separated columns; empty = air), `out` (item name), `count` (stack size, default 1)
- Renders a 3×3 CSS grid + output slot using `<img src="/textures/item/{{ slot }}.png">`
- Falls back to an empty styled `<div>` for blank slots

### Step C-3: CSS

New file: `site/assets/css/crafting.css` (or inline in the shortcode):

```css
.crafting-grid { display: grid; grid-template-columns: repeat(3, 48px); gap: 2px; }
.crafting-grid .slot { width: 48px; height: 48px; background: #8b8b8b; border: 2px inset #373737; }
.crafting-grid img { width: 100%; image-rendering: pixelated; }
.crafting-output .slot { background: #8b8b8b; border: 2px inset #373737; }
```

Scale factor 3× (16px → 48px) keeps pixel art crisp at doc-page widths.

### Usage in markdown

```
{{< crafting in=",,|,verdant_leaf,,|,," out="verdant_dust" count=4 >}}
```

### Verification

- [ ] `./gradlew syncTextures` → PNGs appear in `site/static/textures/item/`
- [ ] Shortcode renders correctly in `hugo server` local preview
- [ ] Missing texture (air slot) shows as grey box, no broken-image icon
- [ ] CI: textures synced before Hugo build step

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
| C-1: texture sync Gradle task | ⬜ planned |
| C-2: crafting shortcode | ⬜ planned |
| C-3: recipe CSS | ⬜ planned |

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
