---
type: design
status: wip
last-updated: 2026-06-30
links: "[[magic/00_energy]], [[01_rituals]]"
---

# MAM — Side Effects & Gremlins

Magic is not clean. Prolonged exposure to magical energy warps the world around it. This doc covers **side effects** — the emergent consequences of operating machinery near active magic — and **Gremlins**, the mob that embodies that chaos.

---

## Core Concept

Each magic school generates **side effects** as a byproduct of operating. These accumulate in a per-school **Side Effect Pool**. When the pool overflows or reaches threshold, side effects **manifest** — physical chaos enters the world. Manifestations drain the pool as they exist.

Side effects **cannot be stopped once manifested** — only prevented. The player must manage their Side Effect Pool before it fills, or deal with the consequences.

```
School magic in use → Side Effect Pool fills → threshold reached → manifestation spawns → drains pool
```

One example for machines: prolonged proximity to an overflowing Side Effect Pool causes machines to **stop working** (halted, not destroyed). Breaking the halted machine block releases **Gremlins**.

```
Side Effect Pool overflows → nearby machines halt → player breaks halted machine → Gremlins spawn
```

**One Side Effect Meter per energy type, scoped per chunk.** Schools that share an energy type share a meter.

| Energy           | Meter          | Schools affected       |
|------------------|----------------|------------------------|
| Mana             | **Tangle**     | Verdant                |
| Nox              | **Corruption** | Sanguine, dark schools |
| Kinetic (Create) | **Rational**   | Rational / science     |
| Summoning energy | **Warp**       | Summoning              |
| Ritual activity  | **Wyrd**       | Ritual                 |

## Side Effect Meter

Each energy type has a chunk-scoped meter that fills as magic is actively used. When it overflows, manifestations begin spawning — randomly, not continuously.

- **Accumulation** — 1 unit of energy generated = 1 unit added to the meter. No modifier on the input side; balance is done on the spend/drain side.
- **Spawn chance** — random % roll per tick/interval. Higher meter = worse manifestation tier available, not just more frequent. The higher you push it, the more dangerous what emerges.
- **Drain** — each manifestation that spawns lowers the meter. Side effects are self-limiting if left alone, but the manifestations themselves are the problem.
- **No suppression** — once active, manifestations cannot be cancelled. They drain naturally.


## Side Effects

| Manifestation              | Meter          |
|----------------------------|----------------|
| Gremlins (machine halting) | **Tangle**     |
| Gremlins (machine halting) | **Corruption** |
| Gremlins (machine halting) | **Wyrd**       |
| Mana burn                  | **Rational**   |
| Nox burn                   | **Rational**   |
| Spawns monster             | **Warp**       |
| Metroit crashes down       | **Wyrd**       |
| Metroit crashes down       | **Warp**       |

## Gremlins (**Tangle** | **Corruption** | **Wyrd** ) 

Gremlins are a MAM-native mob — a twisted variant of the vanilla Silverfish. Where Silverfish are an annoyance that hides in stone, Gremlins are the physical manifestation of Verdant side effect overflow that lived inside a machine.

| Property       | Value                                                                |
|----------------|----------------------------------------------------------------------|
| Base mob       | Silverfish (reskin / subclass)                                       |
| Spawn trigger  | Player breaks a machine block halted by Verdant side effect overflow |
| Target         | Players                                                              |
| Behaviour      | Attack players; drain the Verdant Side Effect Pool as they exist     |
| Count on spawn | 1-3 inside machine , halts the machine                               |
| Despawn        |                                                                      |

Breaking the block is the player's choice, not automatic.

---

## Mana Burn ( **Rational** )

When rational goes to high % chance that mana in a mana pool burns ... remove x from the pool and x from the meter **Rational**

## Splintered focus ( **Rational**)

Mana spread reduced area / range 

## Spawns monster (  **Warp**  )

When summoning daemons sometime other things sneak through into our realm ...

## Metroit crashes down ( **Warp** | **Wyrd** )

The sky is falling, literally ... quite destructive !


... upside is you can mine the meteorite for ores.


---

## Meter Management Tools

Four items/structures for managing side effect meters — defensive and offensive uses.

### Ward
Pushes side effect meter value into adjacent chunks. Does not destroy it — displaces it. Good for protecting your base at the cost of your neighbors.

### Anchor (Lightning Rod)
Opposite of the Ward — pulls side effect meter from adjacent chunks into this one. Concentrates side effects deliberately. Use case: drain your neighbors, or centralize side effects somewhere you can manage them.

### Bottle (Tank)
A **placeable structure block** — not a handheld item. Place it in the world to capture side effect meter from the chunk.

**Filling:** requires magic energy + Create pump mechanic to draw side effect into the tank. Cannot be filled by hand alone.

**Moving:** use Silk Touch to pick up a filled tank and relocate it. The contents are preserved.

**Release:** break the tank without Silk Touch — releases all stored side effects into the current chunk, triggering manifestations immediately.

Useful for controlled testing: fill a tank, carry it somewhere, break it, observe.

### Pandora's Box
A trigger item — no loading required. Opening it immediately triggers the **worst possible manifestation** for the current chunk's meter level. High cost, high consequence. The box is consumed on use.

Not a storage device — a detonator. Use it when you want maximum chaos now, regardless of the meter state.

---

## Open Questions

- [ ] **Meter capacity** — TBD, balancing pass (accumulation rate is 1:1 with energy generated)
- [ ] **Spawn % curve** — how does manifestation tier scale with meter level? TBD
- [ ] **Gremlin count on spawn** — fixed, or scales with meter level?
- [ ] **Gremlin despawn** — when meter drains to zero? Timer? Killed only?
- [ ] **Affected machine block list** — Create machines confirmed; other mods TBD / config-gated
- [ ] **Meter visualisation** — how does the player know the meter is near overflow?
- [ ] **Ward/Anchor range** — how many chunks does displacement reach? TBD
- [ ] **Bottle capacity** — how much meter does one bottle hold? Stack behaviour? TBD
- [ ] **Pandora's Box variants** — different box types per meter type? Per manifestation tier? TBD
- [ ] **Other school manifestations** — Corruption / Rational / Warp / Wyrd manifestation types TBD with per-school design

---

## Status

| Item | Status |
|------|--------|
| Core concept (Side Effect Meter per energy type, chunk-scoped → overflow → manifestation → drains meter) | ✅ decided |
| Meter names: Mana→Tangle, Nox→Corruption, Kinetic→Rational, Summoning→Warp, Ritual→Wyrd | ✅ decided |
| No suppression — prevention only | ✅ decided |
| Machine halting (not breaking) on Verdant overflow | ✅ decided |
| Player breaks halted machine → Gremlins spawn | ✅ decided |
| Gremlins as twisted Silverfish, target players | ✅ decided |
| Gremlins drain Verdant Side Effect Pool | ✅ decided |
| Applies to Create machines | ✅ decided |
| Pool accumulation rate / capacity | ⬜ TBD |
| Gremlin despawn condition | ⬜ TBD |
| Ward (push meter to adjacent chunks) | ✅ decided |
| Anchor (pull meter from adjacent chunks) | ✅ decided |
| Bottle (capture meter as item) | ✅ decided |
| Pandora's Box (trigger side effects on demand) | ✅ decided |
| Ward/Anchor/Bottle/Box details | ⬜ TBD |
| Other school manifestations | ⬜ TBD — per-school design |
| Mob / meter registration | ⬜ not started |
