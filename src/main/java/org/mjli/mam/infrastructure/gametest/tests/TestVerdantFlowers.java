package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block.MysticalMushroomBlock;
import org.mjli.mam.block.flower.BuriedPetalBlock;
import org.mjli.mam.block.flower.MysticalFlowerBlock;
import org.mjli.mam.block.flower.TallMysticalFlowerBlock;
import org.mjli.mam.verdant.VerdantFlowers;

/**
 * Pass-1 tests: low-complexity, sync, no player required.
 * All tests share a single 7×5×7 dirt-floor platform structure.
 * Working area: y=1–4 above the dirt floor at y=0.
 */
@GameTestHolder(MightAndMagic.MODID)
@PrefixGameTestTemplate(false)
public class TestVerdantFlowers {

    private static final String PLATFORM = "verdant_flowers/small_platform";
    // First air block above the dirt floor (structure Y=1 → helper Y=2; helper Y = structure Y + 1).
    private static final BlockPos CENTER = new BlockPos(3, 2, 3);

    // ── MysticalFlowerBlock ────────────────────────────────────────────────

    /** MF-1: performBonemeal places LOWER at pos and UPPER at pos+1. */
    @GameTest(template = PLATFORM)
    public static void bonemealFlowerGrowsTall(GameTestHelper helper) {
        MysticalFlowerBlock flower = (MysticalFlowerBlock) VerdantFlowers.FLOWERS.get(DyeColor.WHITE).get();
        helper.setBlock(CENTER, flower.defaultBlockState());
        flower.performBonemeal(helper.getLevel(), helper.getLevel().random,
                helper.absolutePos(CENTER), flower.defaultBlockState());
        Block tall = VerdantFlowers.TALL_FLOWERS.get(DyeColor.WHITE).get();
        helper.assertBlockPresent(tall, CENTER);
        helper.assertBlockPresent(tall, CENTER.above());
        helper.succeed();
    }

    /** MF-2: isValidBonemealTarget returns false when block above is occupied. */
    @GameTest(template = PLATFORM)
    public static void flowerBonemealBlockedWhenObstructed(GameTestHelper helper) {
        MysticalFlowerBlock flower = (MysticalFlowerBlock) VerdantFlowers.FLOWERS.get(DyeColor.WHITE).get();
        helper.setBlock(CENTER, flower.defaultBlockState());
        helper.setBlock(CENTER.above(), Blocks.STONE.defaultBlockState());
        if (flower.isValidBonemealTarget(helper.getLevel(), helper.absolutePos(CENTER), flower.defaultBlockState())) {
            helper.fail("isValidBonemealTarget should be false when block above is occupied");
        }
        helper.succeed();
    }

    /** MF-3: breaking a single flower drops the flower item (loot table drop-self). */
    @GameTest(template = PLATFORM)
    public static void flowerDropSelfOnBreak(GameTestHelper helper) {
        MysticalFlowerBlock flower = (MysticalFlowerBlock) VerdantFlowers.FLOWERS.get(DyeColor.WHITE).get();
        helper.setBlock(CENTER, flower.defaultBlockState());
        helper.getLevel().destroyBlock(helper.absolutePos(CENTER), true);
        helper.assertItemEntityPresent(flower.asItem(), CENTER, 2.0);
        helper.succeed();
    }

    // ── TallMysticalFlowerBlock ────────────────────────────────────────────

    /** TF-4: isValidBonemealTarget always returns false for tall flowers. */
    @GameTest(template = PLATFORM)
    public static void tallFlowerNotBonemealable(GameTestHelper helper) {
        TallMysticalFlowerBlock tall = (TallMysticalFlowerBlock) VerdantFlowers.TALL_FLOWERS.get(DyeColor.WHITE).get();
        if (tall.isValidBonemealTarget(helper.getLevel(), helper.absolutePos(CENTER), tall.defaultBlockState())) {
            helper.fail("isValidBonemealTarget should always be false for TallMysticalFlowerBlock");
        }
        helper.succeed();
    }

    // ── BuriedPetalBlock ──────────────────────────────────────────────────

    /** BP-1: performBonemeal replaces buried petal with the matching-color flower. */
    @GameTest(template = PLATFORM)
    public static void bonemealBuriedPetalGrowsFlower(GameTestHelper helper) {
        BuriedPetalBlock petal = (BuriedPetalBlock) VerdantFlowers.BURIED_PETALS.get(DyeColor.WHITE).get();
        helper.setBlock(CENTER, petal.defaultBlockState());
        petal.performBonemeal(helper.getLevel(), helper.getLevel().random,
                helper.absolutePos(CENTER), petal.defaultBlockState());
        helper.assertBlockPresent(VerdantFlowers.FLOWERS.get(DyeColor.WHITE).get(), CENTER);
        helper.succeed();
    }

    /** BP-2: isValidBonemealTarget returns false when block above is occupied. */
    @GameTest(template = PLATFORM)
    public static void buriedPetalBonemealBlockedWhenObstructed(GameTestHelper helper) {
        BuriedPetalBlock petal = (BuriedPetalBlock) VerdantFlowers.BURIED_PETALS.get(DyeColor.WHITE).get();
        helper.setBlock(CENTER, petal.defaultBlockState());
        helper.setBlock(CENTER.above(), Blocks.STONE.defaultBlockState());
        if (petal.isValidBonemealTarget(helper.getLevel(), helper.absolutePos(CENTER), petal.defaultBlockState())) {
            helper.fail("isValidBonemealTarget should be false when block above is occupied");
        }
        helper.succeed();
    }

    /** BP-3: spot-check white, red, blue — each petal grows its exact matching-color flower. */
    @GameTest(template = PLATFORM)
    public static void buriedPetalColorMatchesFlower(GameTestHelper helper) {
        for (DyeColor color : new DyeColor[]{DyeColor.WHITE, DyeColor.RED, DyeColor.BLUE}) {
            BuriedPetalBlock petal = (BuriedPetalBlock) VerdantFlowers.BURIED_PETALS.get(color).get();
            helper.setBlock(CENTER, petal.defaultBlockState());
            petal.performBonemeal(helper.getLevel(), helper.getLevel().random,
                    helper.absolutePos(CENTER), petal.defaultBlockState());
            helper.assertBlockPresent(VerdantFlowers.FLOWERS.get(color).get(), CENTER);
        }
        helper.succeed();
    }

    /** BP-4: breaking a buried petal drops the petal item (not the block). */
    @GameTest(template = PLATFORM)
    public static void buriedPetalDropOnBreak(GameTestHelper helper) {
        BuriedPetalBlock petal = (BuriedPetalBlock) VerdantFlowers.BURIED_PETALS.get(DyeColor.WHITE).get();
        helper.setBlock(CENTER, petal.defaultBlockState());
        helper.getLevel().destroyBlock(helper.absolutePos(CENTER), true);
        helper.assertItemEntityPresent(VerdantFlowers.PETALS.get(DyeColor.WHITE).get(), CENTER, 2.0);
        helper.succeed();
    }

    // ── FloralPowderItem ──────────────────────────────────────────────────

    /** FP-5: canSurvive passes for a mystical flower on dirt (regression guard). */
    @GameTest(template = PLATFORM)
    public static void flowerCanSurviveOnDirt(GameTestHelper helper) {
        MysticalFlowerBlock flower = (MysticalFlowerBlock) VerdantFlowers.FLOWERS.get(DyeColor.WHITE).get();
        // CENTER.below() = (3,0,3) which is dirt in the platform structure
        if (!flower.canSurvive(flower.defaultBlockState(), helper.getLevel(), helper.absolutePos(CENTER))) {
            helper.fail("canSurvive should be true for mystical flower above dirt");
        }
        helper.succeed();
    }

    // ── MysticalMushroomBlock ─────────────────────────────────────────────

    /** MM-1: canSurvive returns true when the block below is stone (isSolidRender). */
    @GameTest(template = PLATFORM)
    public static void mushroomSurvivesOnSolidBlock(GameTestHelper helper) {
        helper.setBlock(CENTER.below(), Blocks.STONE.defaultBlockState());
        MysticalMushroomBlock mushroom = (MysticalMushroomBlock) VerdantFlowers.MUSHROOMS.get(DyeColor.WHITE).get();
        if (!mushroom.canSurvive(mushroom.defaultBlockState(), helper.getLevel(), helper.absolutePos(CENTER))) {
            helper.fail("canSurvive should be true when below is stone");
        }
        helper.succeed();
    }

    /** MM-2: canSurvive returns true when the block below is mycelium (MUSHROOM_GROW_BLOCK tag). */
    @GameTest(template = PLATFORM)
    public static void mushroomSurvivesOnMushroomGrowBlock(GameTestHelper helper) {
        helper.setBlock(CENTER.below(), Blocks.MYCELIUM.defaultBlockState());
        MysticalMushroomBlock mushroom = (MysticalMushroomBlock) VerdantFlowers.MUSHROOMS.get(DyeColor.WHITE).get();
        if (!mushroom.canSurvive(mushroom.defaultBlockState(), helper.getLevel(), helper.absolutePos(CENTER))) {
            helper.fail("canSurvive should be true when below is mycelium (MUSHROOM_GROW_BLOCK)");
        }
        helper.succeed();
    }

    /** MM-3: canSurvive returns false when the block below is air. */
    @GameTest(template = PLATFORM)
    public static void mushroomCannotSurviveOnAir(GameTestHelper helper) {
        // CENTER.above() = (3,2,3); the block at (3,1,3) is air in the platform structure
        MysticalMushroomBlock mushroom = (MysticalMushroomBlock) VerdantFlowers.MUSHROOMS.get(DyeColor.WHITE).get();
        if (mushroom.canSurvive(mushroom.defaultBlockState(), helper.getLevel(), helper.absolutePos(CENTER.above()))) {
            helper.fail("canSurvive should be false when block below is air");
        }
        helper.succeed();
    }

    // ── Tags ──────────────────────────────────────────────────────────────

    /** TAG-1: mam:mystical_flowers contains all 16 DyeColor flowers. */
    @GameTest(template = PLATFORM)
    public static void all16FlowersInMysticalFlowersTag(GameTestHelper helper) {
        TagKey<Block> tag = TagKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath("mam", "mystical_flowers"));

        // Diagnostic: check via BuiltInRegistries first (same path FloralPowderItem uses).
        // If empty here, the tag JSON is not being applied to the block registry at all.
        var builtInTagOpt = BuiltInRegistries.BLOCK.getTag(tag);
        if (builtInTagOpt.isEmpty()) {
            helper.fail("DIAG: mam:mystical_flowers absent from BuiltInRegistries — tag file not applied");
            return;
        }
        HolderSet.Named<Block> builtInTag = builtInTagOpt.get();

        for (DyeColor color : DyeColor.values()) {
            Block flower = VerdantFlowers.FLOWERS.get(color).get();
            if (builtInTag.stream().noneMatch(h -> h.value() == flower)) {
                helper.fail(color.getSerializedName() + "_mystical_flower missing (tag has " + builtInTag.size() + " entries)");
            }
        }
        helper.succeed();
    }

    /** TAG-2: all MAM mystical flowers appear in minecraft:small_flowers. */
    @GameTest(template = PLATFORM)
    public static void allFlowersInMinecraftSmallFlowersTag(GameTestHelper helper) {
        var smFlowersOpt = BuiltInRegistries.BLOCK.getTag(BlockTags.SMALL_FLOWERS);
        if (smFlowersOpt.isEmpty()) {
            helper.fail("DIAG: minecraft:small_flowers absent from BuiltInRegistries");
            return;
        }
        HolderSet.Named<Block> smFlowers = smFlowersOpt.get();

        for (DyeColor color : DyeColor.values()) {
            Block flower = VerdantFlowers.FLOWERS.get(color).get();
            if (smFlowers.stream().noneMatch(h -> h.value() == flower)) {
                helper.fail(color.getSerializedName() + "_mystical_flower missing from small_flowers (tag has " + smFlowers.size() + " entries)");
            }
        }
        helper.succeed();
    }

    // ── TallMysticalFlowerBlock — loot drops ─────────────────────────────

    /** TF-1: breaking the LOWER half fires loot → exactly 2 petals dropped. */
    @GameTest(template = PLATFORM)
    public static void tallFlowerBreakLowerDrops2Petals(GameTestHelper helper) {
        TallMysticalFlowerBlock tall = (TallMysticalFlowerBlock) VerdantFlowers.TALL_FLOWERS.get(DyeColor.WHITE).get();
        Item petal = VerdantFlowers.PETALS.get(DyeColor.WHITE).get();
        helper.setBlock(CENTER, tall.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        helper.setBlock(CENTER.above(), tall.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
        helper.getLevel().destroyBlock(helper.absolutePos(CENTER), true);
        helper.assertItemEntityCountIs(petal, CENTER, 2.0, 2);
        helper.succeed();
    }

    /**
     * TF-2: destroyBlock with dropsItems=false suppresses the loot table entirely → 0 petals.
     *
     * NOTE on double-plant mechanics: DoublePlantBlock has no onRemove. Instead, updateShape
     * returns AIR when the other half is missing, and Block.updateOrDestroy propagates that
     * as destroyBlock(otherHalf, !suppressDrops). Breaking UPPER with dropsItems=true would
     * therefore fire LOWER's loot (→ 2 petals). The dropsItems=false path tested here
     * suppresses LOWER's drops while UPPER still gets destroyBlock'd with its own drops (0,
     * since HALF=upper fails the condition). This is the correct counter-case to TF-1.
     */
    @GameTest(template = PLATFORM)
    public static void tallFlowerBreakSuppressedDropsNothing(GameTestHelper helper) {
        TallMysticalFlowerBlock tall = (TallMysticalFlowerBlock) VerdantFlowers.TALL_FLOWERS.get(DyeColor.WHITE).get();
        Item petal = VerdantFlowers.PETALS.get(DyeColor.WHITE).get();
        helper.setBlock(CENTER, tall.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        helper.setBlock(CENTER.above(), tall.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
        helper.getLevel().destroyBlock(helper.absolutePos(CENTER), false);
        helper.assertItemEntityNotPresent(petal, CENTER, 3.0);
        helper.succeed();
    }

    // ── BuriedPetalBlock — item placement ────────────────────────────────

    /** BP-5: right-clicking petal item on dirt places the matching BuriedPetalBlock. */
    @GameTest(template = PLATFORM)
    public static void petalItemPlantsBuriedPetal(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(0, new ItemStack(VerdantFlowers.PETALS.get(DyeColor.WHITE).get()));

        // Click top face of dirt so BlockPlaceContext places at dirtPos.above() = CENTER
        BlockPos dirtHelper = CENTER.below();
        BlockPos worldDirt = helper.absolutePos(dirtHelper);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(worldDirt), Direction.UP, worldDirt, false);
        helper.useBlock(dirtHelper, player, hit);

        helper.assertBlockPresent(VerdantFlowers.BURIED_PETALS.get(DyeColor.WHITE).get(), CENTER);
        helper.succeed();
    }

    // ── FloralPowderItem ──────────────────────────────────────────────────

    /** FP-1: useOn flat dirt → exactly 5–7 flowers placed in the Y=2 layer. */
    @GameTest(template = PLATFORM)
    public static void floralPowderScattersFlowersOnDirt(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(0, new ItemStack(VerdantFlowers.FLORAL_POWDER.get()));
        helper.useBlock(CENTER.below(), player);

        int count = 0;
        for (int x = 0; x < 7; x++) for (int z = 0; z < 7; z++) {
            if (!helper.getBlockState(new BlockPos(x, 2, z)).isAir()) count++;
        }
        if (count < 5 || count > 7) helper.fail("Expected 5–7 flowers on dirt, got " + count);
        helper.succeed();
    }

    /** FP-2: ItemStack shrinks by 1 after a successful scatter. */
    @GameTest(template = PLATFORM)
    public static void floralPowderConsumesItem(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(0, new ItemStack(VerdantFlowers.FLORAL_POWDER.get(), 3));
        helper.useBlock(CENTER.below(), player);
        int remaining = player.getInventory().getItem(0).getCount();
        if (remaining != 2) helper.fail("Expected 2 remaining, got " + remaining);
        helper.succeed();
    }

    /** FP-3: no flowers placed when the entire floor is stone (not in minecraft:dirt tag). */
    @GameTest(template = PLATFORM)
    public static void floralPowderBlockedOnStoneFloor(GameTestHelper helper) {
        for (int x = 0; x < 7; x++) for (int z = 0; z < 7; z++) {
            helper.setBlock(new BlockPos(x, 1, z), Blocks.STONE.defaultBlockState());
        }
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(0, new ItemStack(VerdantFlowers.FLORAL_POWDER.get()));
        helper.useBlock(new BlockPos(3, 1, 3), player);

        int count = 0;
        for (int x = 0; x < 7; x++) for (int z = 0; z < 7; z++) {
            if (!helper.getBlockState(new BlockPos(x, 2, z)).isAir()) count++;
        }
        if (count != 0) helper.fail("Expected 0 flowers on stone floor, got " + count);
        helper.succeed();
    }
}
