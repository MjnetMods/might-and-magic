---
type: infra
status: active
last-updated: 2026-06-30
---

# Publish Pipeline

## GitHub

- **Org:** MjnetMods
- **Repo:** `MjnetMods/might-and-magic`
- **Visibility:** public

### Workflows

| File                            | Trigger           | Does                                                                                        |
|---------------------------------|-------------------|---------------------------------------------------------------------------------------------|
| `.github/workflows/build.yml`   | push/PR to `main` | Builds mod JAR, uploads as artifact                                                         |
| `.github/workflows/pages.yml`   | push to `main`    | Builds Hugo site, deploys to GitHub Pages                                                   |
| `.github/workflows/release.yml` | push `v*.*.*` tag | Builds JAR, generates changelog, creates GitHub Release, publishes to Modrinth + CurseForge |

### Secrets required

| Secret             | Scope                  | Used by                       |
|--------------------|------------------------|-------------------------------|
| `MODRINTH_TOKEN`   | org (all public repos) | `release.yml` → `publishMods` |
| `CURSEFORGE_TOKEN` | org (all public repos) | `release.yml` → `publishMods` |

## Modrinth

- **Org:** MjnetMods
- **Project ID:** `hjcHRg1d`
- **URL:** https://modrinth.com/mod/hjcHRg1d
- **Status:** ✅ live

## CurseForge

- **Project ID:** `1593032`
- **URL:** https://legacy.curseforge.com/minecraft/mc-mods/might-magic
- **Status:** ⏳ under review — submit for review was done, waiting on CurseForge staff approval

## Gradle publish config

Plugin: `me.modmuss50.mod-publish-plugin` in `build.gradle`.

- `publishMods` task publishes to both platforms
- Changelog fed from `CHANGELOG` env var (set by git-cliff in `release.yml`)
- Targets: NeoForge, Minecraft 1.21.1

## Conventional commits + changelog

Commits use conventional prefix format — see CLAUDE.md commit convention table.  
`git-cliff` reads `cliff.toml` and generates per-release changelog from prefixed commits.  
Unconventional commits are silently skipped.
