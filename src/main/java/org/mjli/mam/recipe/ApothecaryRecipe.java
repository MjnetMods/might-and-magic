package org.mjli.mam.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.mjli.mam.MamRecipes;

import java.util.ArrayList;
import java.util.List;

public record ApothecaryRecipe(List<Ingredient> petals, Ingredient reagent, ItemStack output)
        implements RecipeWithReagent {

    @Override
    public boolean matches(RecipeInput input, Level level) {
        List<Ingredient> missing = new ArrayList<>(petals);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) break;

            int matchIndex = -1;
            for (int j = 0; j < missing.size(); j++) {
                if (missing.get(j).test(stack)) { matchIndex = j; break; }
            }
            if (matchIndex == -1) return false;
            missing.remove(matchIndex);
        }
        return missing.isEmpty();
    }

    @Override public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) { return output.copy(); }
    @Override public ItemStack getResultItem(HolderLookup.Provider registries) { return output.copy(); }
    @Override public RecipeSerializer<?> getSerializer() { return MamRecipes.APOTHECARY_SERIALIZER.get(); }
    @Override public RecipeType<?> getType() { return MamRecipes.APOTHECARY_TYPE.get(); }
    @Override public Ingredient getReagent() { return reagent; }

    public static class Serializer implements RecipeSerializer<ApothecaryRecipe> {
        public static final MapCodec<ApothecaryRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC.listOf().fieldOf("petals").forGetter(ApothecaryRecipe::petals),
                        Ingredient.CODEC.fieldOf("reagent").forGetter(ApothecaryRecipe::reagent),
                        ItemStack.CODEC.fieldOf("output").forGetter(ApothecaryRecipe::output)
                ).apply(instance, ApothecaryRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ApothecaryRecipe> STREAM_CODEC =
                StreamCodec.of(
                        (buf, recipe) -> {
                            buf.writeInt(recipe.petals().size());
                            for (Ingredient ing : recipe.petals()) Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
                            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.reagent());
                            ItemStack.STREAM_CODEC.encode(buf, recipe.output());
                        },
                        buf -> {
                            int count = buf.readInt();
                            List<Ingredient> petals = new java.util.ArrayList<>(count);
                            for (int i = 0; i < count; i++) petals.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                            Ingredient reagent = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                            ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
                            return new ApothecaryRecipe(petals, reagent, output);
                        });

        @Override public MapCodec<ApothecaryRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, ApothecaryRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
