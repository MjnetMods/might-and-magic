---
type: infra
status: done
last-updated: 2026-06-30
---

# Infrastructure Overview

Cross-cutting delivery: CI, publishing, and the player-facing site.

## Status

| Area                     | Doc                                              | Status         |
|--------------------------|--------------------------------------------------|----------------|
| GitHub repo + CI         | [01_publish-pipeline.md](01_publish-pipeline.md) | ✅ live         |
| Modrinth                 | [01_publish-pipeline.md](01_publish-pipeline.md) | ✅ live         |
| CurseForge               | [01_publish-pipeline.md](01_publish-pipeline.md) | ⏳ under review |
| Hugo site + GitHub Pages | [02_site.md](02_site.md)                         | ✅ live         |
| Custom domain (mjli.org) | [03_custom-domain.md](03_custom-domain.md)       | ⬜ next up      |

## How to cut a release

```bash
export VERSION="v0.0.3" &&  git tag -a "$VERSION" -m "Release $VERSION" && git push origin "$VERSION"
```

Triggers `.github/workflows/release.yml` → builds JAR → GitHub Release → Modrinth + CurseForge publish.
