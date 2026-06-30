---
type: infra
status: active
last-updated: 2026-06-30
---

# Hugo Doc Site

Player-facing wiki. Auto-deployed to GitHub Pages on push to `main`.

## URLs

- **Current:** https://mjnetmods.github.io/might-and-magic/
- **Future:** https://mjli.org/ — see [03_custom-domain.md](03_custom-domain.md)

## Stack

- Hugo with [Blowfish](https://blowfish.page/) theme (via Go modules, `site/go.mod`)
- Dark mode, avocado color scheme
- Deployed via `.github/workflows/pages.yml`

## Structure

```
site/
  hugo.toml           # site config + Blowfish import
  go.mod / go.sum     # Blowfish version pin
  Makefile            # serve / modules / build targets
  content/            # player-facing source of truth
    getting-started/
    verdant-path/
    summoning-path/
    sanguine-path/
    science-path/
    credits/
  static/
    textures/         # populated by ./gradlew syncTextures
    icon.png          # mod icon
```

## Local dev

```bash
cd site
make serve    # http://localhost:1313 — live reload, drafts visible
```

## Updating content

`site/content/` is the source of truth. When lore, recipes, or mechanics change:

1. Edit `site/content/<path>/_index.md`
2. Run `./gradlew syncTextures` if new block/item textures were added
3. `make serve` to verify locally
4. Commit — Pages deploy happens automatically on push to `main`

## Texture sync

`./gradlew syncTextures` copies `src/main/resources/assets/mam/textures/` → `site/static/textures/`.  
Run this whenever new blocks or items are added.

## Status

| Item                         | Status    |
|------------------------------|-----------|
| Blowfish scaffold            | ✅ done    |
| Getting Started page         | ✅ done    |
| Verdant Path content         | ✅ done    |
| All school placeholder pages | ✅ done    |
| Per-path color schemes       | ✅ done    |
| Credits page                 | ✅ done    |
| Texture sync Gradle task     | ✅ done    |
| Crafting shortcode           | ✅ done    |
| GitHub Pages deploy workflow | ✅ done    |
| Custom domain (mjli.org)     | ⬜ next up |
