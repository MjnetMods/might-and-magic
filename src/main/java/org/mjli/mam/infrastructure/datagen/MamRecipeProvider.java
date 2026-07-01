package org.mjli.mam.infrastructure.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.recipe.ApothecaryRecipe;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantMana;
import org.mjli.mam.verdant.VerdantRock;
import org.mjli.mam.verdant.VerdantWood;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MamRecipeProvider extends RecipeProvider {

    public MamRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        guide(output);
        flowers(output);
        livingRock(output);
        livingWood(output);
        apothecary(output);
        apothecaryPureDaisy(output);
        manaPool(output);
    }

    private void guide(RecipeOutput output) {
        var petals = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, "mystical_petals"));
        var flowers = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, "mystical_flowers"));
        var mushrooms = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, "mystical_mushrooms"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VerdantFlowers.GUIDE.get())
            .requires(petals)
            .requires(flowers)
            .requires(mushrooms)
            .requires(Items.BOOK)
            .unlockedBy("has_petal", has(petals))
            .save(output, id("guide"));
    }

    private void flowers(RecipeOutput output) {
        for (DyeColor color : DyeColor.values()) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VerdantFlowers.PETALS.get(color).get(), 4)
                .requires(VerdantFlowers.FLOWERS.get(color).get())
                .unlockedBy("has_flower", has(VerdantFlowers.FLOWERS.get(color).get()))
                .save(output, id(color.getSerializedName() + "_mystical_flower_to_petals"));

            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DyeItem.byColor(color))
                .requires(VerdantFlowers.PETALS.get(color).get())
                .unlockedBy("has_petal", has(VerdantFlowers.PETALS.get(color).get()))
                .save(output, id(color.getSerializedName() + "_petal_to_dye"));
        }

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VerdantFlowers.FLORAL_POWDER.get())
            .requires(Tags.Items.DYES)
            .requires(Tags.Items.DYES)
            .requires(Tags.Items.DYES)
            .requires(Items.BONE_MEAL)
            .unlockedBy("has_dye", has(Tags.Items.DYES))
            .save(output, id("floral_powder"));
    }

    private void livingRock(RecipeOutput output) {
        // ── Base variants ─────────────────────────────────────────────
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantRock.LIVING_ROCK_POLISHED.get(), 4)
            .pattern("##").pattern("##")
            .define('#', VerdantRock.LIVING_ROCK.get())
            .unlockedBy("has_living_rock", has(VerdantRock.LIVING_ROCK.get()))
            .save(output, id("living_rock_polished"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantRock.LIVING_ROCK_BRICK.get(), 4)
            .pattern("##").pattern("##")
            .define('#', VerdantRock.LIVING_ROCK_POLISHED.get())
            .unlockedBy("has_living_rock_polished", has(VerdantRock.LIVING_ROCK_POLISHED.get()))
            .save(output, id("living_rock_brick"));

        // ── Living Rock furniture ─────────────────────────────────────
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantRock.LIVING_ROCK_STAIRS.get(), 4)
            .pattern("#  ").pattern("## ").pattern("###")
            .define('#', VerdantRock.LIVING_ROCK.get())
            .unlockedBy("has_living_rock", has(VerdantRock.LIVING_ROCK.get()))
            .save(output, id("living_rock_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantRock.LIVING_ROCK_SLAB.get(), 6)
            .pattern("###")
            .define('#', VerdantRock.LIVING_ROCK.get())
            .unlockedBy("has_living_rock", has(VerdantRock.LIVING_ROCK.get()))
            .save(output, id("living_rock_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, VerdantRock.LIVING_ROCK_WALL.get(), 6)
            .pattern("###").pattern("###")
            .define('#', VerdantRock.LIVING_ROCK.get())
            .unlockedBy("has_living_rock", has(VerdantRock.LIVING_ROCK.get()))
            .save(output, id("living_rock_wall"));

        // ── Living Rock Polished furniture ────────────────────────────
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantRock.LIVING_ROCK_POLISHED_STAIRS.get(), 4)
            .pattern("#  ").pattern("## ").pattern("###")
            .define('#', VerdantRock.LIVING_ROCK_POLISHED.get())
            .unlockedBy("has_living_rock_polished", has(VerdantRock.LIVING_ROCK_POLISHED.get()))
            .save(output, id("living_rock_polished_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantRock.LIVING_ROCK_POLISHED_SLAB.get(), 6)
            .pattern("###")
            .define('#', VerdantRock.LIVING_ROCK_POLISHED.get())
            .unlockedBy("has_living_rock_polished", has(VerdantRock.LIVING_ROCK_POLISHED.get()))
            .save(output, id("living_rock_polished_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, VerdantRock.LIVING_ROCK_POLISHED_WALL.get(), 6)
            .pattern("###").pattern("###")
            .define('#', VerdantRock.LIVING_ROCK_POLISHED.get())
            .unlockedBy("has_living_rock_polished", has(VerdantRock.LIVING_ROCK_POLISHED.get()))
            .save(output, id("living_rock_polished_wall"));

        // ── Living Rock Brick furniture ───────────────────────────────
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantRock.LIVING_ROCK_BRICK_STAIRS.get(), 4)
            .pattern("#  ").pattern("## ").pattern("###")
            .define('#', VerdantRock.LIVING_ROCK_BRICK.get())
            .unlockedBy("has_living_rock_brick", has(VerdantRock.LIVING_ROCK_BRICK.get()))
            .save(output, id("living_rock_brick_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantRock.LIVING_ROCK_BRICK_SLAB.get(), 6)
            .pattern("###")
            .define('#', VerdantRock.LIVING_ROCK_BRICK.get())
            .unlockedBy("has_living_rock_brick", has(VerdantRock.LIVING_ROCK_BRICK.get()))
            .save(output, id("living_rock_brick_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, VerdantRock.LIVING_ROCK_BRICK_WALL.get(), 6)
            .pattern("###").pattern("###")
            .define('#', VerdantRock.LIVING_ROCK_BRICK.get())
            .unlockedBy("has_living_rock_brick", has(VerdantRock.LIVING_ROCK_BRICK.get()))
            .save(output, id("living_rock_brick_wall"));
    }

    private void livingWood(RecipeOutput output) {
        // ── Planks from logs ──────────────────────────────────────────
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, VerdantWood.LIVINGWOOD_PLANKS.get(), 4)
            .requires(VerdantWood.LIVINGWOOD_LOG.get())
            .unlockedBy("has_livingwood_log", has(VerdantWood.LIVINGWOOD_LOG.get()))
            .save(output, id("livingwood_planks_from_log"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, VerdantWood.LIVINGWOOD_PLANKS.get(), 4)
            .requires(VerdantWood.LIVINGWOOD_LOG_STRIPPED.get())
            .unlockedBy("has_livingwood_log_stripped", has(VerdantWood.LIVINGWOOD_LOG_STRIPPED.get()))
            .save(output, id("livingwood_planks_from_stripped_log"));

        // ── Livingwood Planks furniture ───────────────────────────────
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantWood.LIVINGWOOD_PLANKS_STAIRS.get(), 4)
            .pattern("#  ").pattern("## ").pattern("###")
            .define('#', VerdantWood.LIVINGWOOD_PLANKS.get())
            .unlockedBy("has_livingwood_planks", has(VerdantWood.LIVINGWOOD_PLANKS.get()))
            .save(output, id("livingwood_planks_stairs"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, VerdantWood.LIVINGWOOD_PLANKS_SLAB.get(), 6)
            .pattern("###")
            .define('#', VerdantWood.LIVINGWOOD_PLANKS.get())
            .unlockedBy("has_livingwood_planks", has(VerdantWood.LIVINGWOOD_PLANKS.get()))
            .save(output, id("livingwood_planks_slab"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, VerdantWood.LIVINGWOOD_PLANKS_FENCE.get(), 3)
            .pattern("#S#").pattern("#S#")
            .define('#', VerdantWood.LIVINGWOOD_PLANKS.get())
            .define('S', Items.STICK)
            .unlockedBy("has_livingwood_planks", has(VerdantWood.LIVINGWOOD_PLANKS.get()))
            .save(output, id("livingwood_planks_fence"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, VerdantWood.LIVINGWOOD_PLANKS_FENCE_GATE.get(), 1)
            .pattern("S#S").pattern("S#S")
            .define('#', VerdantWood.LIVINGWOOD_PLANKS.get())
            .define('S', Items.STICK)
            .unlockedBy("has_livingwood_planks", has(VerdantWood.LIVINGWOOD_PLANKS.get()))
            .save(output, id("livingwood_planks_fence_gate"));
    }

    private void apothecary(RecipeOutput output) {
        var petals = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, "mystical_petals"));
        var mushrooms = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, "mystical_mushrooms"));
        Ingredient petalOrMushroom = CompoundIngredient.of(Ingredient.of(petals), Ingredient.of(mushrooms));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VerdantMana.APOTHECARY.get())
            .pattern("#P#")
            .pattern(" # ")
            .pattern("###")
            .define('#', Tags.Items.STONES)
            .define('P', petalOrMushroom)
            .unlockedBy("has_petal", has(petals))
            .unlockedBy("has_mushroom", has(mushrooms))
            .save(output, id("apothecary"));
    }

    // In-world Apothecary recipe: 4x white petal + seed reagent -> Pure Daisy. Ported from
    // Botania's data/botania/recipes/petal_apothecary/pure_daisy.json (see design/magic/10_apothecary.md).
    private void apothecaryPureDaisy(RecipeOutput output) {
        Ingredient whitePetal = Ingredient.of(VerdantFlowers.PETALS.get(DyeColor.WHITE).get());
        ApothecaryRecipe pureDaisy = new ApothecaryRecipe(
                List.of(whitePetal, whitePetal, whitePetal, whitePetal),
                Ingredient.of(Tags.Items.SEEDS),
                VerdantFlowers.PURE_DAISY.asStack());
        output.accept(id("apothecary/pure_daisy"), pureDaisy, null);
    }

    private void manaPool(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VerdantMana.MANA_POOL.get())
            .pattern("R R")
            .pattern("R R")
            .pattern("RRR")
            .define('R', VerdantRock.LIVING_ROCK.get())
            .unlockedBy("has_living_rock", has(VerdantRock.LIVING_ROCK.get()))
            .save(output, id("mana_pool"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VerdantMana.INFUSED_MANA_POOL.get())
            .pattern("R R")
            .pattern("R R")
            .pattern("RRR")
            .define('R', VerdantRock.INFUSED_LIVING_ROCK.get())
            .unlockedBy("has_infused_living_rock", has(VerdantRock.INFUSED_LIVING_ROCK.get()))
            .save(output, id("infused_mana_pool"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VerdantMana.SACRED_MANA_POOL.get())
            .pattern("R R")
            .pattern("R R")
            .pattern("RRR")
            .define('R', VerdantRock.SACRED_LIVING_ROCK.get())
            .unlockedBy("has_sacred_living_rock", has(VerdantRock.SACRED_LIVING_ROCK.get()))
            .save(output, id("sacred_mana_pool"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, VerdantMana.DESECRATED_MANA_POOL.get())
            .pattern("R R")
            .pattern("R R")
            .pattern("RRR")
            .define('R', VerdantRock.DESECRATED_LIVING_ROCK.get())
            .unlockedBy("has_desecrated_living_rock", has(VerdantRock.DESECRATED_LIVING_ROCK.get()))
            .save(output, id("desecrated_mana_pool"));
    }

    private static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, name);
    }
}
