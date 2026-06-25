package org.mjli.mam;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mjli.mam.recipe.PetalApothecaryRecipe;
import org.mjli.mam.recipe.PureDaisyRecipe;

public class MamRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, MightAndMagic.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MightAndMagic.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<PureDaisyRecipe>> PURE_DAISY_TYPE =
            RECIPE_TYPES.register("pure_daisy", () -> RecipeType.simple(MightAndMagic.modLoc("pure_daisy")));
    public static final DeferredHolder<RecipeType<?>, RecipeType<PetalApothecaryRecipe>> PETAL_APOTHECARY_TYPE =
            RECIPE_TYPES.register("petal_apothecary", () -> RecipeType.simple(MightAndMagic.modLoc("petal_apothecary")));

    public static final DeferredHolder<RecipeSerializer<?>, PureDaisyRecipe.Serializer> PURE_DAISY_SERIALIZER =
            RECIPE_SERIALIZERS.register("pure_daisy", PureDaisyRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, PetalApothecaryRecipe.Serializer> PETAL_APOTHECARY_SERIALIZER =
            RECIPE_SERIALIZERS.register("petal_apothecary", PetalApothecaryRecipe.Serializer::new);

    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }
}
