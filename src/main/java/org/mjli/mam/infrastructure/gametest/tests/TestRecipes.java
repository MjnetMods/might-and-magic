package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.verdant.VerdantFlowers;

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
}
