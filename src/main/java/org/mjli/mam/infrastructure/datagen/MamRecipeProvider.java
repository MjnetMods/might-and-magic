package org.mjli.mam.infrastructure.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.verdant.VerdantFlowers;

import java.util.concurrent.CompletableFuture;

public class MamRecipeProvider extends RecipeProvider {

    public MamRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // 1 petal → 1 dye (16 recipes)
        for (DyeColor color : DyeColor.values()) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DyeItem.byColor(color))
                .requires(VerdantFlowers.PETALS.get(color).get())
                .unlockedBy("has_petal", has(VerdantFlowers.PETALS.get(color).get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID,
                    color.getSerializedName() + "_petal_to_dye"));
        }

        // any 3 dye + bonemeal → floral powder (1 recipe using dye tag)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, VerdantFlowers.FLORAL_POWDER.get())
            .requires(Tags.Items.DYES)
            .requires(Tags.Items.DYES)
            .requires(Tags.Items.DYES)
            .requires(Items.BONE_MEAL)
            .unlockedBy("has_dye", has(Tags.Items.DYES))
            .save(output, ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, "floral_powder"));
    }
}
