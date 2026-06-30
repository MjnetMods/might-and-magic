# Registrate Guide

NeoForge Registrate `MC1.21-1.3.0+62` usage patterns for this mod.

---

## Basic block registration

```java
R.block("my_block", Block::new)
 .properties(p -> BlockBehaviour.Properties.of().strength(2f))
 .blockstate((ctx, p) -> p.simpleBlock(ctx.get(), p.cubeAll(ctx.get())))
 .loot((t, b) -> t.dropSelf(b))
 .simpleItem()        // generates item model parenting from block/my_block
 .register();
```

`.simpleItem()` is `item().build()` with a default model that calls `blockItem(ctx)` →
`withExistingParent(name, "block/" + name)`. This works as long as the blockstate
provider generates a model file literally named `<name>` (i.e., a cube_all or
single-model block). It fails for blocks whose blockstate helpers only generate
sub-models.

---

## Block types and their item model strategy

| Block type | Blockstate helper | Models generated | Item model strategy |
|-----------|------------------|-----------------|---------------------|
| Full cube | `simpleBlock(b, cubeAll(b))` | `<name>` | `.simpleItem()` |
| Rotated pillar | `axisBlock(b, side, top)` | `<name>` | `.simpleItem()` |
| Stairs | `stairsBlock(b, texture)` | `<name>_stairs`, `_inner`, `_outer` | `.simpleItem()` — the helper also generates a plain `<name>` (stair model) that serves as item |
| Slab | `slabBlock(b, doubleslab, texture)` | `<name>`, `<name>_top`, uses `doubleslab` for double | `.simpleItem()` |
| Fence gate | `fenceGateBlock(b, texture)` | `<name>`, `<name>_open`, `<name>_wall`, `<name>_wall_open` | `.simpleItem()` — base model is `<name>` |
| **Fence** | `fenceBlock(b, texture)` | `<name>_fence_post`, `<name>_fence_side` | **Must manually generate inventory model** (see below) |
| **Wall** | `wallBlock(b, texture)` | `<name>_post`, `<name>_side`, `<name>_side_tall` | **Must manually generate inventory model** (see below) |

---

## Fence and wall: inventory item model pattern

Neither `fenceBlock()` nor `wallBlock()` generates a base model suitable for
`simpleItem()`. You must generate the `_inventory` model explicitly in the
blockstate lambda, then use `item().model(blockWithInventoryModel).build()`.

### Fence

```java
R.block("my_fence", FenceBlock::new)
 .properties(p -> ...)
 .blockstate((ctx, p) -> {
     var tex = p.modLoc("block/my_planks");
     p.fenceBlock(ctx.get(), tex);
     // generate inventory model for item rendering
     p.models().getBuilder(ctx.getName() + "_inventory")
         .parent(new ModelFile.UncheckedModelFile("minecraft:block/fence_inventory"))
         .texture("texture", tex.toString());
 })
 .loot((t, b) -> t.dropSelf(b))
 .item().model((ctx, p) -> p.blockWithInventoryModel(ctx::getEntry)).build()
 .register();
```

### Wall

```java
R.block("my_wall", WallBlock::new)
 .properties(p -> ...)
 .blockstate((ctx, p) -> {
     var tex = p.modLoc("block/my_stone");
     p.wallBlock(ctx.get(), tex);
     p.models().getBuilder(ctx.getName() + "_inventory")
         .parent(new ModelFile.UncheckedModelFile("minecraft:block/wall_inventory"))
         .texture("wall", tex.toString());
 })
 .loot((t, b) -> t.dropSelf(b))
 .item().model((ctx, p) -> p.blockWithInventoryModel(ctx::getEntry)).build()
 .register();
```

**Why `UncheckedModelFile`?** NeoForge datagen providers run concurrently. Using
`withExistingParent` (which validates existence) against a model generated in the
same run can race. `UncheckedModelFile` skips the check and just writes the JSON.
The wall `_inventory` model IS being generated in the same lambda, so it will
exist on disk after the run — the reference is always correct.

---

## Slab loot table

Breaking a double slab should drop 2 items, not 1. Use `createSlabItemTable`:

```java
.loot((t, b) -> t.add(b, t.createSlabItemTable(b)))
```

Do **not** use `.loot((t, b) -> t.dropSelf(b))` for slabs — that drops 1 for double slabs.

---

## StairBlock base state

`StairBlock` requires the parent block's default `BlockState` at construction:

```java
R.block("my_stairs", p -> new StairBlock(MY_PLANKS.get().defaultBlockState(), p))
```

This is safe because Registrate's factory lambda runs during the BLOCK registry
event — after previously-declared entries in the same class are already registered.
Declare the parent block field above the stairs field.

---

## FenceGateBlock and WoodType

`FenceGateBlock` needs a `WoodType` for door/gate sounds:

```java
R.block("my_fence_gate", p -> new FenceGateBlock(WoodType.OAK, p))
```

`WoodType.OAK` is fine as a sound placeholder until a custom wood type is defined.

---

## appendToTab

All items must be added with `PARENT_TAB_ONLY` to avoid double-registration in
the search tab (Registrate's default creative tab is SEARCH):

```java
public static void appendToTab(CreativeModeTabModifier modifier) {
    var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
    modifier.accept(MY_BLOCK.asStack(), tab);
}
```

---

## Datagen: RecipeProvider vs hand-authored JSON

| Use | Where |
|-----|-------|
| Custom recipe types (`mam:pure_daisy`, `mam:apothecary`) | Hand-authored in `src/main/resources/data/mam/recipe/` |
| Standard crafting (shaped/shapeless) | `MamRecipeProvider` (datagen → `src/generated/`) |
| Programmatic loops (16 colors, etc.) | Always in `MamRecipeProvider` |

The split exists because custom recipe types have no Java builder; hand-authoring
is the only option for them.
