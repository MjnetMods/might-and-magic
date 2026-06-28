# NeoForge GameTest Reference — MAM Project

Everything learned the hard way writing the Pure Daisy tests.  
Covers: test class setup, SNBT structure format, coordinate system, framework lifecycle, failure diagnostics.

---

## 1. Test class setup

```java
@GameTestHolder(MightAndMagic.MODID)   // registers all tests in this class under the mod id
@PrefixGameTestTemplate(false)          // template name is used verbatim (not prefixed with class name)
public class TestVerdantPath {

    @GameTest(template = "verdant_path/pure_daisy_converts_log", timeoutTicks = 16000)
    public static void pureDaisyConvertsLog(GameTestHelper helper) {
        BlockPos logPos = new BlockPos(3, 2, 4);
        helper.succeedWhen(() -> helper.assertBlockPresent(VerdantWood.LIVINGWOOD_LOG.get(), logPos));
    }
}
```

**`@PrefixGameTestTemplate(false)`** — without this, NeoForge prepends the class name to the template
string, producing `testverdantpath.verdant_path/...` which won't match your file.

**Template string** → resolves to a ResourceLocation: `mam:verdant_path/pure_daisy_converts_log`

**`timeoutTicks`** — at max tick rate (~16 000 ticks/s in gameTestServer), 16 000 ticks ≈ 1 second wall
clock. Size it to your conversion time + headroom.

---

## 2. SNBT structure file format

### Where files live

```
src/gametest/structure/
└── data/mam/structure/
    └── verdant_path/
        ├── pure_daisy_converts_log.snbt
        └── pure_daisy_converts_stone.snbt
```

`runData` converts them to NBT into `src/generated/resources/data/mam/structure/verdant_path/*.nbt`.  
The `StructureTemplateManager` loads from `data/<ns>/structure/` (singular, not `structures/`).

### The packed SNBT format

`SnbtToNbt` calls `NbtUtils.unpackStructureTemplate`, which expects a **packed** format —
**not** the on-disk NBT compound format. The two are different in three ways:

| Field | Packed SNBT (source file) | Unpacked NBT (on-disk result) |
|---|---|---|
| `palette` | list of **strings** | list of compound tags |
| blocks key | `data` | `blocks` |
| block state ref | string from palette | integer index into palette |

**Palette string format:** plain name for stateless blocks; `"name{key:value,key2:value2}"` for
blocks with properties. Note curly braces and **colon** separator — not the `[key=value]` blockstate
notation.

```snbt
{
  DataVersion: 3955,
  size: [9, 2, 9],
  palette: [
    "minecraft:air",
    "minecraft:dirt",
    "minecraft:oak_log{axis:y}",
    "mam:pure_daisy"
  ],
  data: [
    {pos: [0, 0, 0], state: "minecraft:dirt"},
    ...
    {pos: [3, 1, 4], state: "minecraft:oak_log{axis:y}"},
    {pos: [4, 1, 4], state: "mam:pure_daisy"}
  ],
  entities: []
}
```

**`pos`** — list of three TAG_Int values `[x, y, z]`, not an int array.

**`packBlockState` vs `unpackBlockState` are NOT inverses.** Pack produces `[...]`, unpack reads `{...}`.
Do not use a structure saved in-game and copy its SNBT literally — it will be in a different format.

### Verifying the generated NBT

Quick sanity check with Python:

```python
import gzip
with open('src/generated/resources/data/mam/structure/verdant_path/foo.nbt', 'rb') as f:
    data = gzip.decompress(f.read())
# If palette is empty → packed format was wrong
# Expected: "minecraft:dirt", "mam:pure_daisy" etc. appear as byte strings
print(b'minecraft:dirt' in data)   # should be True
```

---

## 3. Coordinate system

### Three coordinate spaces

| Space | Description |
|---|---|
| **Structure template** | Coordinates as authored in the SNBT (0-indexed from northwest/top corner) |
| **Helper** | `GameTestHelper` relative coords — used in test assertions |
| **World** | Absolute world coords — what the logs report on failure |

### Mapping: structure → helper

```
helper.absolutePos(relativePos) = structureBlockPos.offset(relativePos)
```

The **structure block entity** sits at `northwestCorner.below()` (one block below the test origin).
The structure block entity's default `structurePos` offset is `(0, 1, 0)`, so structure content
starts one block above the block entity.

```
structureBlockPos.Y = northwestCorner.Y - 1
structure Y=0       → world Y = structureBlockPos.Y + 1 = northwestCorner.Y
structure Y=1       → world Y = northwestCorner.Y + 1
```

Therefore:

```
helper Y = structure Y + 1
```

If your target block is at structure `(3, 1, 4)` (Y=1), the helper position is `new BlockPos(3, 2, 4)`.

### Failure log cross-reference

```
puredaisyconvertslog failed at 9126923, -60, 12398881!
  Expected Livingwood Log, got Air at 9126926,-58,12398885 (relative: 3,2,4) (t=16001)
```

- `failed at X,-60,Z` → `structureBlockPos` = structure block entity world position
- `relative: 3,2,4` → helper coords passed to `assertBlockPresent`
- `got Air at X,-58,Z` → absolute world position of the assertion

**"got Air" at correct coordinates** almost always means the structure was not placed (empty palette,
wrong format) — not a coordinate error. Check the generated NBT first.

---

## 4. Framework lifecycle

```
GameTestServer.startTests()
  └─ StructureGridSpawner.spawnStructure(info)
       ├─ GameTestInfo.prepareTestStructure()
       │    └─ StructureUtils.prepareTestStructure(info, northwestCorner, rotation, level)
       │         ├─ level.getStructureManager().get(name).orElseThrow()  ← "Missing test structure" if absent
       │         ├─ clearSpaceForStructure(boundingBox, level)           ← clears the area to AIR / STONE floor
       │         └─ createStructureBlock(info, nwCorner.below(), ...)
       │              └─ sbe.loadStructureInfo(level)                    ← loads template, sets structureSize
       └─ forceLoadChunks()

  Per tick: GameTestInfo.tick(runner)
       ├─ check chunksLoaded (all chunks in bounding box must be loaded)
       ├─ ensureStructureIsPlaced()
       │    ├─ if ticksToWaitForChunkLoading > 0 → decrement, return false (skip tick)
       │    └─ placeStructure() → structureBlockEntity.placeStructure(level)   ← ACTUAL block placement
       │         └─ StructureTemplate.placeInWorld(level, sbePos + structurePos, ...)
       └─ tickInternal()  ← runs succeedWhen checks, increments tick counter
```

### Key points

- **`loadStructureInfo` ≠ `placeStructure`**. The structure is NOT placed during `prepareTestStructure`.
  Blocks appear in the world on the first tick after chunks load.

- **`ensureStructureIsPlaced` returns false** while waiting for chunks — `tickInternal` is not called,
  but the tick counter still advances toward timeout. The full 16 000 ticks burn before failure.

- **`clearSpaceForStructure`** sets `groundY = boundingBox.minY() - 1`.
  Blocks at `Y < groundY` → STONE (the floor foundation).
  Blocks at `Y >= groundY` → AIR (the working space).
  The structure block entity itself is at `groundY`, placed by `createStructureBlock` after the clear.

- **`StructureTemplate.placeInWorld`** only places blocks that are explicitly listed in the template.
  Positions not in the `blocks` list are left as-is (AIR from the clear pass).

---

## 5. Data pipeline

```
src/gametest/structure/          ← SNBT source (hand-authored, packed format)
   │  runData  (SnbtToNbt)
   ▼
src/generated/resources/         ← compiled NBT (gitignored, must run runData before build)
   data/mam/structure/**.nbt

build.gradle: runGameTestServer { dependsOn('runData') }
```

`runData` must run before `runGameTestServer`. If SNBT changed but Gradle thinks `runData` is
up-to-date, force it:

```bash
./gradlew runData --no-configuration-cache
./gradlew runGameTestServer --no-configuration-cache
```

**`MamDataGen`** registers `SnbtToNbt` with input folder `src/gametest/structure/`.
The relative path from that root determines the output resource path:
`data/mam/structure/verdant_path/foo.snbt` → `data/mam/structure/verdant_path/foo.nbt`
→ ResourceLocation `mam:verdant_path/foo`.

---

## 6. Double-plant block drop mechanics

`DoublePlantBlock` (parent of `TallMysticalFlowerBlock`) has **no `onRemove`**. Instead it relies on
`updateShape` returning `Blocks.AIR` when the other half is missing. `Block.updateOrDestroy` then
applies that change via:

```java
level.destroyBlock(otherHalfPos, (flags & Block.UPDATE_SUPPRESS_DROPS) == 0, null);
```

Consequence: **destroying UPPER with dropsItems=true causes LOWER to also be destroyed with drops**.
Since LOWER's loot table condition is `HALF=lower`, LOWER's loot fires → petals drop.

| Action | Result |
|--------|--------|
| `destroyBlock(LOWER, true)` | LOWER loot fires (HALF=lower ✓) → 2 petals; UPPER removed with drops=true but UPPER loot → 0 |
| `destroyBlock(UPPER, true)` | UPPER loot fires → 0; neighbor update: LOWER `destroyBlock(true)` → LOWER loot → 2 petals |
| `destroyBlock(LOWER, false)` | LOWER: no drops; neighbor update: UPPER `destroyBlock(true)` → 0 (HALF=upper fails) |
| `destroyBlock(UPPER, false)` | UPPER: no drops; neighbor update: LOWER `destroyBlock(true)` → 2 petals |

The creative-player path (`DoublePlantBlock.playerWillDestroy → preventDropFromBottomPart`) sets LOWER to AIR
with flag `35` (`3 | UPDATE_SUPPRESS_DROPS`), which propagates `destroyBlock(UPPER, false)` → 0 from either half.

---

## 7. Common failure modes

| Error | Cause | Fix |
|---|---|---|
| `Missing test structure: mam:foo` | NBT file not found in resource pack | Check path: must be `data/<ns>/structure/` (singular). Run `runData`. |
| `Couldn't load structure mam:foo` + `ResourceLocationException: Non [a-z0-9/._-] character` | Palette `Name` contains `[axis=y]` — wrong SNBT format | Use packed format: `"minecraft:oak_log{axis:y}"` not `"minecraft:oak_log[axis=y]"` |
| `got Air at (3,2,4)` | Empty palette in NBT — structure placed but no blocks match | `palette` in SNBT was list of compounds (unpacked format) instead of list of strings |
| `got Dirt at (3,2,4)` | Helper Y off by 1 | Remember: helper Y = structure Y + 1 |
| `got Oak Log` (never converts) | Pure Daisy block entity not ticking, or recipe not loaded | Check `getTicker` returns non-null; verify recipe JSON; check block registration |
| Test times out at t=16001 | `ensureStructureIsPlaced` was returning false every tick | Usually: structure template not found (caught silently) — see "Missing test structure" |
| `BuiltInRegistries.BLOCK.getTag(mam:foo)` returns empty | Tag files at wrong path — MC 1.21 uses `tags/block/` (singular), not `tags/blocks/` (plural) | Rename `data/<ns>/tags/blocks/` → `data/<ns>/tags/block/`; same for `items/` → `item/` |

---

## 8. Running tests

```bash
# Full pipeline
./gradlew runGameTestServer

# Force data regen if SNBT changed
./gradlew runData --no-configuration-cache && ./gradlew runGameTestServer --no-configuration-cache

# In-game (runServer or runClient)
/test runall
/test run mam:verdant_path.pureDaisyConvertsLog
```

Log output: `run/gameTestServer/logs/debug.log`

Result markers in log: `[++]` = pass, `[XX]` = fail, `[__]` = not yet run.
