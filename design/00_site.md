---
type: infra
status: done
last-updated: 2026-06-29
---

# Hugo Doc Site

Player-facing wiki hosted on GitHub Pages. Auto-deployed on push to `main`.

**Reference implementation:** `/Users/mannil/pfn/projects/mjnet/studio-m` — read `hugo.toml`, `Makefile`, and `go.mod` before starting.

---

## Setup Pattern

Hugo modules (not git submodules). Theme pulled via `go.mod` + `hugo mod tidy` at setup time. No `themes/` directory, no `.gitmodules`.

```
site/
  hugo.toml         # site config + Blowfish import
  go.mod            # module declaration + Blowfish version pin
  go.sum
  Makefile          # serve / modules / build targets
  content/
    getting-started/
      _index.md
    verdant-path/
      _index.md     # player-facing source of truth (docs/ removed)
    dev/
      _index.md     # draft: true — hidden from nav, reachable by direct URL
  static/
    textures/       # populated by Track C syncTextures Gradle task
```

---

## Track B: Site Scaffold

### File targets

| File | Change |
|------|--------|
| `site/go.mod` | Create with Blowfish v2 module declaration |
| `site/hugo.toml` | Create with Blowfish config (see template below) |
| `site/Makefile` | Create with serve / modules / build targets (see template below) |
| `site/content/getting-started/_index.md` | Create intro page |
| `site/content/verdant-path/_index.md` | Player-facing source of truth — edit directly when content changes |
| `site/content/dev/_index.md` | Create with `draft: true` |

### go.mod template

```
module github.com/mjli/mam-site

go 1.25.0

require github.com/nunocoracao/blowfish/v2 v2.100.0 // indirect
```

After creating, run `make modules` to generate `go.sum`.

### hugo.toml template

```toml
baseURL = "https://mjli.github.io/mam/"
title = "Might & Magic"
languageCode = "en-us"

canonifyURLs = true
relativeURLs = false

[[module.imports]]
path = "github.com/nunocoracao/blowfish/v2"

[params]
colorScheme = "avocado"
defaultAppearance = "dark"
autoSwitchAppearance = false
description = "A Minecraft magic & technology mod for NeoForge 1.21.1"

[params.sidebar]
emoji = "🌿"
subtitle = "NeoForge 1.21.1"

[menu]

[[menu.main]]
name = "Getting Started"
url = "/getting-started/"
weight = 10

[[menu.main]]
name = "Verdant Path"
url = "/verdant-path/"
weight = 20
```

### Makefile template

Port of studio-m Makefile. Hugo image pinned to `v0.160.1` (matches local install).

```makefile
IMAGE=ghcr.io/gohugoio/hugo:v0.160.1

SITE=/site
PORT=1313

all: build

## Initialize Hugo module (run once, already done if go.mod exists)
init:
	docker run --rm \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	mod init github.com/mjli/mam-site

## Download modules / themes
modules:
	docker run --rm \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	mod tidy

## Update modules/themes to latest
update-modules:
	docker run --rm \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	mod get -u

## Build static site
build:
	docker run --rm \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	--minify

## Run development server (local Hugo — instant reload)
serve:
	hugo server \
		--bind 0.0.0.0 \
		--port $(PORT) \
		--baseURL http://localhost:$(PORT)/ \
		--disableFastRender \
		--buildDrafts \
		--buildFuture \
		--source .

## Run development server via Docker (no auto-reload on macOS)
docker-serve:
	docker run --rm \
	-p $(PORT):1313 \
	-v $(PWD):$(SITE) \
	-w $(SITE) \
	$(IMAGE) \
	server \
	--bind 0.0.0.0 \
	--port 1313 \
	--baseURL http://localhost:1313/ \
	--disableFastRender \
	--buildDrafts \
	--buildFuture

## Clean generated files
clean:
	rm -rf public resources

help:
	@echo ""
	@echo "MAM Doc Site"
	@echo ""
	@echo "make modules      Download/update theme (run after clone or go.mod change)"
	@echo "make serve        Start dev server at localhost:1313 (requires local Hugo)"
	@echo "make build        Build static site via Docker"
	@echo ""
```

### Local dev workflow

```bash
cd site
make modules     # first time or after theme version bump
make serve       # http://localhost:1313 — live reload, drafts visible
```

### Acceptance criteria

- `todo` — `make modules` (from `site/`) pulls Blowfish and generates `go.sum`
- `todo` — `make serve` starts without errors, site loads at `localhost:1313`
- `todo` — Nav shows: Getting Started, Verdant Path
- `todo` — `/dev/` accessible by direct URL, not in nav
- `todo` — Dark mode, avocado color scheme

---

## Track C: Recipe Display

**Goal:** Render crafting/processing recipes in Hugo pages using mod textures — no screenshots, no external tools, auto-synced.

### Step C-1: Texture sync (Gradle task)

Add to `build.gradle`:

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

New file: `site/assets/css/crafting.css`:

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

### Acceptance criteria

- `todo` — `./gradlew syncTextures` → PNGs appear in `site/static/textures/item/`
- `todo` — Shortcode renders correctly in `hugo server` local preview
- `todo` — Missing texture (air slot) shows as grey box, no broken-image icon
- `todo` — CI: textures synced before Hugo build step

---

## Track D: GitHub Pages Deploy Workflow

New file: `.github/workflows/site.yml`

- Trigger: push to `main`
- Steps: setup Java 21 → `./gradlew syncTextures` → setup Hugo → `hugo --minify --source site` → upload artifact → deploy to GitHub Pages
- Reference: `/Users/mannil/best/site/.github/workflows/build.yml` — strip CV download step

### Acceptance criteria

- `todo` — Push to `main` → GitHub Pages URL serves player content
- `todo` — `/dev/getting-started` accessible by direct URL, not in nav

---

## Workflow

`site/content/` is the source of truth for all player-facing documentation. The `docs/` folder has been removed. When lore, recipes, or mechanics change:

1. Update `site/content/<path>/_index.md` directly
2. Run `./gradlew syncTextures` if new block/item textures were added
3. Run `cd site && make serve` to verify locally before committing

`design/` remains the source of truth for implementation decisions, work packages, and tracking. It stays separate from the site.

---

## Status

| Item | Status |
|------|--------|
| B: site scaffold + Blowfish | ✅ done |
| B: getting-started content | ✅ done |
| B: verdant-path content | ✅ done |
| B: all school placeholder pages | ✅ done |
| B: per-path color schemes + atmospheric backgrounds | ✅ done |
| B: modpack reference page (/reference/) | ✅ done |
| C-1: texture sync Gradle task | ✅ done |
| C-2: crafting shortcode | ✅ done |
| C-3: recipe CSS | ✅ done |
| D: GitHub Pages deploy workflow | ⬜ planned |
