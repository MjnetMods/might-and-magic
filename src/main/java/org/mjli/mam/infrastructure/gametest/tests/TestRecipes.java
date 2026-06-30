package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantMana;
import org.mjli.mam.verdant.VerdantRock;
import org.mjli.mam.verdant.VerdantWood;

import java.util.List;

@GameTestHolder(MightAndMagic.MODID)
@PrefixGameTestTemplate(false)
public class TestRecipes {

    private static final String PLATFORM = "verdant_flowers/small_platform";

    /** RC-1: spot-check white/red/blue — each mystical flower → 4 matching petals. */
    @GameTest(template = PLATFORM)
    public static void flowerToPetalsYields4(GameTestHelper helper) {
        var rm = helper.getLevel().getServer().getRecipeManager();
        var allCrafting = rm.getAllRecipesFor(RecipeType.CRAFTING);
        var registries = helper.getLevel().registryAccess();

        for (DyeColor color : new DyeColor[]{DyeColor.WHITE, DyeColor.RED, DyeColor.BLUE}) {
            ItemStack flowerStack = new ItemStack(VerdantFlowers.FLOWERS.get(color).get());
            var petalItem = VerdantFlowers.PETALS.get(color).get();

            var match = allCrafting.stream()
                .filter(r -> r.value().getResultItem(registries).is(petalItem))
                .filter(r -> r.value().getIngredients().stream().anyMatch(i -> i.test(flowerStack)))
                .findFirst();
            if (match.isEmpty()) {
                helper.fail("No recipe: " + color.getSerializedName() + "_mystical_flower_to_petals");
                return;
            }
            int count = match.get().value().getResultItem(registries).getCount();
            if (count != 4) {
                helper.fail("Expected 4 petals for " + color.getSerializedName() + ", got " + count);
                return;
            }
        }
        helper.succeed();
    }

    /** RC-2: spot-check white/red/blue petals — each petal → 1 dye of matching color. */
    @GameTest(template = PLATFORM)
    public static void petalToDyeYields1(GameTestHelper helper) {
        var rm = helper.getLevel().getServer().getRecipeManager();
        var allCrafting = rm.getAllRecipesFor(RecipeType.CRAFTING);
        var registries = helper.getLevel().registryAccess();

        for (DyeColor color : new DyeColor[]{DyeColor.WHITE, DyeColor.RED, DyeColor.BLUE}) {
            ItemStack petalStack = new ItemStack(VerdantFlowers.PETALS.get(color).get());
            var dyeItem = DyeItem.byColor(color);

            var match = allCrafting.stream()
                .filter(r -> r.value().getResultItem(registries).is(dyeItem))
                .filter(r -> r.value().getIngredients().stream().anyMatch(i -> i.test(petalStack)))
                .findFirst();
            if (match.isEmpty()) {
                helper.fail("No petal→dye recipe for " + color.getSerializedName());
                return;
            }
            int count = match.get().value().getResultItem(registries).getCount();
            if (count != 1) {
                helper.fail("Expected 1 dye for " + color.getSerializedName() + ", got " + count);
                return;
            }
        }
        helper.succeed();
    }

    /** RC-4: living rock crafting chain — polished and brick both exist and yield 4. */
    @GameTest(template = PLATFORM)
    public static void livingRockCraftingChain(GameTestHelper helper) {
        assertRecipeYields(helper, VerdantRock.LIVING_ROCK_POLISHED.asItem(), 4, "living_rock_polished");
        assertRecipeYields(helper, VerdantRock.LIVING_ROCK_BRICK.asItem(), 4, "living_rock_brick");
        helper.succeed();
    }

    /** RC-5: livingwood planks from log and stripped log both yield 4. */
    @GameTest(template = PLATFORM)
    public static void livingwoodPlanksFromLog(GameTestHelper helper) {
        assertRecipeYields(helper, VerdantWood.LIVINGWOOD_PLANKS.asItem(), 4, "livingwood_planks_from_log");
        helper.succeed();
    }

    /** RC-6: livingwood furniture recipes exist and yield correct counts (stairs=4, slab=6, fence=3, gate=1). */
    @GameTest(template = PLATFORM)
    public static void livingwoodFurnitureRecipes(GameTestHelper helper) {
        record Check(Item result, int count, String label) {}
        for (var c : List.of(
            new Check(VerdantWood.LIVINGWOOD_PLANKS_STAIRS.asItem(), 4, "planks_stairs"),
            new Check(VerdantWood.LIVINGWOOD_PLANKS_SLAB.asItem(),   6, "planks_slab"),
            new Check(VerdantWood.LIVINGWOOD_PLANKS_FENCE.asItem(),  3, "planks_fence"),
            new Check(VerdantWood.LIVINGWOOD_PLANKS_FENCE_GATE.asItem(), 1, "planks_fence_gate")
        )) {
            assertRecipeYields(helper, c.result(), c.count(), "livingwood_" + c.label());
        }
        helper.succeed();
    }

    /** RC-7: living rock furniture — spot-check one of stairs/slab/wall per variant (all three variants). */
    @GameTest(template = PLATFORM)
    public static void livingRockFurnitureRecipes(GameTestHelper helper) {
        record Check(Item result, int count, String label) {}
        for (var c : List.of(
            new Check(VerdantRock.LIVING_ROCK_STAIRS.asItem(),          4, "living_rock_stairs"),
            new Check(VerdantRock.LIVING_ROCK_SLAB.asItem(),            6, "living_rock_slab"),
            new Check(VerdantRock.LIVING_ROCK_WALL.asItem(),            6, "living_rock_wall"),
            new Check(VerdantRock.LIVING_ROCK_POLISHED_STAIRS.asItem(), 4, "living_rock_polished_stairs"),
            new Check(VerdantRock.LIVING_ROCK_POLISHED_SLAB.asItem(),   6, "living_rock_polished_slab"),
            new Check(VerdantRock.LIVING_ROCK_POLISHED_WALL.asItem(),   6, "living_rock_polished_wall"),
            new Check(VerdantRock.LIVING_ROCK_BRICK_STAIRS.asItem(),    4, "living_rock_brick_stairs"),
            new Check(VerdantRock.LIVING_ROCK_BRICK_SLAB.asItem(),      6, "living_rock_brick_slab"),
            new Check(VerdantRock.LIVING_ROCK_BRICK_WALL.asItem(),      6, "living_rock_brick_wall")
        )) {
            assertRecipeYields(helper, c.result(), c.count(), c.label());
        }
        helper.succeed();
    }

    /** RC-3: floral powder recipe exists, yields 1, and requires bonemeal as one ingredient. */
    @GameTest(template = PLATFORM)
    public static void floralPowderRecipeCorrect(GameTestHelper helper) {
        var rm = helper.getLevel().getServer().getRecipeManager();
        var allCrafting = rm.getAllRecipesFor(RecipeType.CRAFTING);
        var registries = helper.getLevel().registryAccess();
        var floralPowder = VerdantFlowers.FLORAL_POWDER.get();

        var match = allCrafting.stream()
            .filter(r -> r.value().getResultItem(registries).is(floralPowder))
            .findFirst();
        if (match.isEmpty()) {
            helper.fail("No floral powder crafting recipe found");
            return;
        }

        long bonemealIngredients = match.get().value().getIngredients().stream()
            .filter(i -> i.test(new ItemStack(Items.BONE_MEAL)))
            .count();
        if (bonemealIngredients != 1) {
            helper.fail("Expected 1 bonemeal ingredient, got " + bonemealIngredients);
            return;
        }

        int count = match.get().value().getResultItem(registries).getCount();
        if (count != 1) {
            helper.fail("Expected recipe to yield 1 floral powder, got " + count);
            return;
        }
        helper.succeed();
    }

    /** RC-8: mana pool U-shape recipes — all four tiers craft and yield 1. */
    @GameTest(template = PLATFORM)
    public static void manaPoolCraftingRecipes(GameTestHelper helper) {
        record Check(Item result, int count, String label) {}
        for (var c : List.of(
            new Check(VerdantMana.MANA_POOL.asItem(), 1, "mana_pool"),
            new Check(VerdantMana.INFUSED_MANA_POOL.asItem(), 1, "infused_mana_pool"),
            new Check(VerdantMana.SACRED_MANA_POOL.asItem(), 1, "sacred_mana_pool"),
            new Check(VerdantMana.DESECRATED_MANA_POOL.asItem(), 1, "desecrated_mana_pool")
        )) {
            assertRecipeYields(helper, c.result(), c.count(), c.label());
        }
        helper.succeed();
    }

    private static void assertRecipeYields(GameTestHelper helper, Item result, int expectedCount, String label) {
        var rm = helper.getLevel().getServer().getRecipeManager();
        var registries = helper.getLevel().registryAccess();
        var match = rm.getAllRecipesFor(RecipeType.CRAFTING).stream()
            .filter(r -> r.value().getResultItem(registries).is(result))
            .findFirst();
        if (match.isEmpty()) {
            helper.fail("No recipe for " + label);
            return;
        }
        int count = match.get().value().getResultItem(registries).getCount();
        if (count != expectedCount) {
            helper.fail("Recipe " + label + ": expected " + expectedCount + ", got " + count);
        }
    }
}
