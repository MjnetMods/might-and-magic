---
type: infra
status: todo
last-updated: 2026-06-30
---

# Custom Domain (mjli.org)

Route the site from `mjnetmods.github.io/might-and-magic/` to `mjli.org`.

## Prerequisites

- Site stable and live at `https://mjnetmods.github.io/might-and-magic/` ✅
- Domain `mjli.org` owned, hosted on Route53 ✅

## Steps

### 1. Route53 — add CNAME record

| Field | Value                                                          |
|-------|----------------------------------------------------------------|
| Type  | CNAME                                                          |
| Name  | `www` (or `@` for apex — use ALIAS record for apex, not CNAME) |
| Value | `mjnetmods.github.io`                                          |
| TTL   | 300                                                            |

For apex domain (`mjli.org` without www): Route53 supports ALIAS records pointing to GitHub Pages. Use an ALIAS A record pointing to GitHub's IPs instead:
```
185.199.108.153
185.199.109.153
185.199.110.153
185.199.111.153
```

### 2. GitHub Pages — set custom domain

Repo → Settings → Pages → Custom domain → enter `mjli.org` → Save.  
GitHub will run a DNS check and issue a TLS cert automatically (Let's Encrypt).

### 3. Update hugo.toml

```toml
baseURL = "https://mjli.org/"
```

### 4. Verify

- `todo` — DNS propagated (`dig mjli.org` returns GitHub's IPs)
- `todo` — `https://mjli.org/` loads the site with valid TLS
- `todo` — All internal links resolve correctly (canonifyURLs handles this)
- `todo` — Old GitHub Pages URL redirects or returns 404 (expected)

## Notes

- GitHub Pages enforces HTTPS automatically once the cert is issued — no extra config needed
- DNS propagation can take up to 48h but is usually minutes on Route53
- Do NOT set `relativeURLs = true` in hugo.toml — current config uses `canonifyURLs = true` which is correct for a custom domain
