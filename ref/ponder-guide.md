# Ponder Reference — MAM Project

Everything learned the hard way wiring Create's Ponder library into MAM.  
Covers: dependency setup, plugin/scene registration, SNBT structure format, localization, data pipeline, failure modes.

---

## 1. Dependencies

Ponder is embedded inside Create's JAR via jarJar — it is **not** on the Maven classpath as a standalone
artifact. You need three explicit dependencies plus special handling to avoid Create's compat mod cascade.

### `build.gradle`

```groovy
// Create (optional dep — Rational Path will need it; also brings Ponder at runtime)
compileOnly("com.simibubi.create:create-${minecraft_version}:${create_version}") { transitive = false }
localRuntime("com.simibubi.create:create-${minecraft_version}:${create_version}") { transitive = false }

// Ponder — jarJar'd inside Create but needed explicitly for compile-time API
compileOnly "net.createmod.ponder:ponder-neoforge:${ponder_version}+mc${minecraft_version}"

// Flywheel — Create's rendering backend, also required by Ponder
compileOnly "dev.engine-room.flywheel:flywheel-neoforge-api-${minecraft_version}:${flywheel_version}"
localRuntime "dev.engine-room.flywheel:flywheel-neoforge-${minecraft_version}:${flywheel_version}"
```

**`transitive = false` on Create** — Create's POM lists every compat mod (JEI, FTB, JourneyMap,
Architectury…) as dependencies. Without this flag, Gradle tries to resolve all of them and fails.

**Repository** — add the CreateMod Maven, scoped to its groups to avoid polluting other lookups:

```groovy
maven {
    name = 'CreateMod'
    url = 'https://maven.createmod.net'
    content {
        includeGroup("com.simibubi.create")
        includeGroup("net.createmod.ponder")
        includeGroup("dev.engine-room.flywheel")
    }
}
```

### Version pinning — Registrate conflict

Create 6.0.x ships with a specific version of Registrate jarJar'd inside it. If your mod also
jarJars Registrate at a **different** version, the JPMS module system sees two `Registrate.MC1._21.*`
modules and crashes at load time.

**Fix:** pin your `registrate_version` in `gradle.properties` to match the version Create embeds.
For Create 6.0.7-117 → use `MC1.21-1.3.0+62`.

Check which version Create bundles by looking at a reference mod's `gradle.properties`
(e.g., `Malum-Mod`).

### `neoforge.mods.toml` — optional dependency declarations

```toml
[[dependencies.mam]]
    modId = "create"
    type = "optional"
    versionRange = "[6.0.0,)"
    ordering = "AFTER"
    side = "BOTH"

[[dependencies.mam]]
    modId = "flywheel"
    type = "optional"
    versionRange = "[1.0.0,2.0)"
    ordering = "AFTER"
    side = "CLIENT"
```

### macOS Apple Silicon — early progress window crash

NeoForge's early loading window uses raw OpenGL FBOs that crash the Metal driver (SIGSEGV in
`gldBlitFramebufferData`). Disable it:

```groovy
// build.gradle — client run config
client {
    systemProperty 'fml.earlyprogresswindow', 'false'
}
```

---

## 2. Plugin and scene registration

### Classloading guard

Ponder types must **not** be resolved unless Create is present. Use a classloading guard in the
client mod class:

```java
// MightAndMagicClient.java
@SubscribeEvent
public static void onClientSetup(FMLClientSetupEvent event) {
    if (ModList.get().isLoaded("create")) {
        initPonder();
    }
}

private static void initPonder() {
    // Fully-qualified name — do NOT use an import or the class is resolved
    // on the calling method's load, bypassing the isLoaded guard
    net.createmod.ponder.foundation.PonderIndex.addPlugin(new org.mjli.mam.ponder.MamPonderPlugin());
}
```

### PonderPlugin

```java
public class MamPonderPlugin implements PonderPlugin {
    @Override public String getModId() { return MightAndMagic.MODID; }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        MamPonderScenes.register(helper);
    }
}
```

### Scene registration

```java
public class MamPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        // withKeyFunction converts Registrate's RegistryEntry to ResourceLocation
        var H = helper.<ItemProviderEntry<?, ?>>withKeyFunction(RegistryEntry::getId);

        H.forComponents(VerdantFlowers.PURE_DAISY)
            .addStoryBoard("pure_daisy/converts_stone", MamPonderScenes::pureDaisyStone)
            .addStoryBoard("pure_daisy/converts_log",   MamPonderScenes::pureDaisyLog);
    }
}
```

The storyboard path (`"pure_daisy/converts_stone"`) determines the **structure file** path:
`assets/mam/ponder/pure_daisy/converts_stone.nbt`.

---

## 3. Storyboard API

### Method signatures

```java
public static void myScene(SceneBuilder scene, SceneBuildingUtil util) { ... }
```

**`util.select()` and `util.vector()` are methods, not fields.**

### SelectionUtil methods

| Method | Description |
|---|---|
| `position(int x, int y, int z)` | Single block |
| `fromTo(int x1, int y1, int z1, int x2, int y2, int z2)` | Rectangular volume |
| `layer(int y)` | All blocks at the given Y level |
| `layersFrom(int y)` | All blocks at Y ≥ given value |
| `layers(int y, int height)` | All blocks in a Y range |

### Typical scene skeleton

```java
scene.title("pure_daisy.converts_stone", "Transmuting Stone");  // sets sceneId
scene.configureBasePlate(0, 0, 5);   // (xOffset, zOffset, size)

scene.showBasePlate();
scene.idle(10);

// Reveal key item FIRST — small/thin blocks (flowers, cross models) are invisible
// if they appear simultaneously with surrounding full-cube blocks
scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
scene.idle(15);

scene.overlay().showText(50)
    .text("mam.ponder.pure_daisy.converts_stone.text_1")   // or use the English string directly
    .pointAt(util.vector().topOf(2, 1, 2))
    .attachKeyFrame();
scene.idle(20);

// Reveal surrounding context after the player has seen the key item
scene.world().showSection(util.select().fromTo(0, 1, 0, 4, 1, 4), Direction.DOWN);
scene.idle(20);

// Animate conversions one at a time
for (BlockPos pos : new BlockPos[]{ new BlockPos(1, 1, 2), ... }) {
    scene.world().setBlock(pos, VerdantRock.LIVING_ROCK.get().defaultBlockState(), true);
    scene.idle(8);
}
```

---

## 4. Localization

Ponder **does not** use the `defaultText` argument as a literal in production.  
`localization.getSpecific(sceneId, key)` calls `I18n.get(langKey)`. If the lang key is absent,
the raw key string is displayed — not the English default you passed to `text()`.

### Key format

```
{namespace}.ponder.{scenePath}.{subkey}
```

- `{namespace}` — your mod id (`mam`)
- `{scenePath}` — the path argument you passed to `scene.title()` (dots, not slashes)
- `{subkey}` — `header` for the title; `text_1`, `text_2`, … for overlay text

### ⚠ textIndex starts at 1, not 0

`PonderScene.textIndex` is initialized to **1**. The first `showText().text(...)` call in a
storyboard registers under `text_1`, the second under `text_2`, etc.

### Example `en_us.json` entries

```json
"mam.ponder.pure_daisy.converts_stone.header": "Transmuting Stone",
"mam.ponder.pure_daisy.converts_stone.text_1": "Place the Pure Daisy near any Stone…",
"mam.ponder.pure_daisy.converts_stone.text_2": "…and it slowly transmutes nearby Stone into Living Rock.",

"mam.ponder.pure_daisy.converts_log.header": "Transmuting Wood",
"mam.ponder.pure_daisy.converts_log.text_1": "Oak Logs placed nearby are transmuted too…",
"mam.ponder.pure_daisy.converts_log.text_2": "…becoming Livingwood Logs, the foundation of verdant crafting."
```

No datagen needed — lang entries live in `src/main/resources/assets/mam/lang/en_us.json`.

---

## 5. SNBT structure format

### Where files live

```
src/ponder/structure/
└── assets/mam/ponder/
    └── pure_daisy/
        ├── converts_stone.snbt
        └── converts_log.snbt
```

`runData` converts them to NBT into `src/generated/resources/assets/mam/ponder/**/*.nbt`.  
Ponder reads from `assets/<ns>/ponder/` (client resource pack, **not** `data/`).

The relative path from `src/ponder/structure/` determines the output resource path:
`assets/mam/ponder/pure_daisy/converts_stone.snbt` →
`assets/mam/ponder/pure_daisy/converts_stone.nbt` →
used by storyboard key `"pure_daisy/converts_stone"`.

### Format

Same **packed** SNBT format as game test structures (see `ref/gametest-guide.md` §2):

```snbt
{
  DataVersion: 3955,
  size: [5, 3, 5],
  palette: [
    "minecraft:air",
    "minecraft:dirt",
    "minecraft:stone",
    "mam:pure_daisy"
  ],
  data: [
    {pos: [0,0,0], state: "minecraft:dirt"},
    ...
    {pos: [2,1,2], state: "mam:pure_daisy"},
    {pos: [1,1,2], state: "minecraft:stone"}
  ],
  entities: []
}
```

### ⚠ Blockstate properties — palette string format

Ponder loads structures via `StructureTemplate.load()` → `NbtUtils.readBlockState()` →
`ResourceLocation.parse(tag.getString("Name"))`.

The `Name` field must be a **valid ResourceLocation** — `[`, `]`, `=` are illegal characters.

| Format | Result |
|---|---|
| `"minecraft:oak_log[axis=y]"` | **CRASH** — `ResourceLocationException: Non [a-z0-9/._-] character` |
| `"minecraft:oak_log{axis:y}"` | OK — packed format, unpacked to `{Name:"minecraft:oak_log", Properties:{axis:"y"}}` |
| `"minecraft:oak_log"` | OK — uses default block state (default for oak_log is `axis=y`) |

Use `"block_name"` (no properties) when you only need the default state — simpler and safe.  
Use `"block_name{key:value}"` (curly braces, colon) when you need a non-default property.

---

## 6. Data pipeline

```
src/ponder/structure/          ← SNBT source (packed format, hand-authored)
   │  runData  (SnbtToNbt)
   ▼
src/generated/resources/       ← compiled NBT (gitignored, must run runData before build)
   assets/mam/ponder/**/*.nbt
```

### MamDataGen — combined SnbtToNbt provider

`SnbtToNbt` uses a hardcoded provider name — only one instance can be registered per data run.
Both the gametest and ponder source folders must be combined into a single provider:

```java
List<Path> snbtFolders = new ArrayList<>();
if (Files.exists(snbtSourceFolder)) snbtFolders.add(snbtSourceFolder);   // gametest
if (Files.exists(ponderSnbtFolder)) snbtFolders.add(ponderSnbtFolder);   // ponder
if (!snbtFolders.isEmpty()) {
    generator.addProvider(
        event.includeServer() || event.includeClient(),
        new SnbtToNbt(packOutput, snbtFolders)
    );
}
```

**`event.includeClient()` is required** — ponder structures land in `assets/` (client pack),
not `data/` (server pack), so `event.includeServer()` alone would skip them.

### runClient auto-dependency

```groovy
// build.gradle
afterEvaluate {
    tasks.named('runClient') { dependsOn('runData') }
}
```

Ensures NBT files are always up-to-date before launching the client.

---

## 7. Common failure modes

| Error | Cause | Fix |
|---|---|---|
| `ResourceLocationException: Non [a-z0-9/._-] character in path: minecraft:oak_log[axis=y]` | Palette entry uses `[key=value]` blockstate notation | Use `"minecraft:oak_log"` (default state) or `"minecraft:oak_log{axis:y}"` (packed curly-brace format) |
| Ponder overlay shows raw key like `mam.ponder.foo.text_1` | Lang key absent from `en_us.json` | Add the key; note textIndex starts at 1 not 0 |
| Ponder overlay shows **wrong text** (each entry shifted by one) | Lang keys written as `text_0`/`text_1` | Correct to `text_1`/`text_2` |
| Key block (flower, cross model) invisible in scene | Appeared simultaneously with surrounding full-cube blocks | Reveal it alone first via `util.select().position(x,y,z)`, then reveal context |
| SIGSEGV / Metal crash before game loads | NeoForge early progress window + macOS Apple Silicon | Add `systemProperty 'fml.earlyprogresswindow', 'false'` to client run config |
| `JPMS module conflict: Registrate.MC1._21._67 vs _62` | Your jarJar'd Registrate version differs from Create's embedded version | Downgrade `registrate_version` in `gradle.properties` to match what Create bundles |
| `package net.createmod.ponder.api.registration does not exist` | Ponder API not on compile classpath (it's inside Create's jar) | Add explicit `compileOnly "net.createmod.ponder:ponder-neoforge:..."` dep |
| Ponder scenes never trigger / no ponder icon on item | `initPonder()` not called, or `PonderIndex.addPlugin` resolving Ponder types before guard | Wrap in `ModList.get().isLoaded("create")` guard; put `addPlugin` in a separate private method with fully-qualified names |

---

## 8. Running and verifying

```bash
# Regenerate structure NBT after editing any .snbt file
./gradlew runData

# Launch client with Create + Ponder loaded
./gradlew runClient

# In-game: hover over an item that has a registered storyboard
# A book icon appears in the item tooltip → click or press W to open Ponder
```

Ponder scenes are client-side only — `runServer` will not display them.
