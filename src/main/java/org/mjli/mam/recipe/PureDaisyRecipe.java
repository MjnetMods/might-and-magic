package org.mjli.mam.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamRecipes;

import java.util.List;
import java.util.Optional;

public record PureDaisyRecipe(BlockState input, BlockState output, int conversionTime)
        implements Recipe<SingleRecipeInput> {

    public static Optional<PureDaisyRecipe> findRecipe(ServerLevel level, BlockState state) {
        return level.getRecipeManager()
                .getAllRecipesFor(MamRecipes.PURE_DAISY_TYPE.get())
                .stream()
                .map(holder -> holder.value())
                .filter(r -> r.input().equals(state))
                .findFirst();
    }

    @Override public boolean matches(SingleRecipeInput input, Level level) { return false; }
    @Override public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) { return ItemStack.EMPTY; }
    @Override public boolean canCraftInDimensions(int w, int h) { return true; }
    @Override public ItemStack getResultItem(HolderLookup.Provider registries) { return ItemStack.EMPTY; }
    @Override public RecipeSerializer<?> getSerializer() { return MamRecipes.PURE_DAISY_SERIALIZER.get(); }
    @Override public RecipeType<?> getType() { return MamRecipes.PURE_DAISY_TYPE.get(); }

    public static class Serializer implements RecipeSerializer<PureDaisyRecipe> {
        public static final MapCodec<PureDaisyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        BlockState.CODEC.fieldOf("input").forGetter(PureDaisyRecipe::input),
                        BlockState.CODEC.fieldOf("output").forGetter(PureDaisyRecipe::output),
                        com.mojang.serialization.Codec.INT.optionalFieldOf("time", 200).forGetter(PureDaisyRecipe::conversionTime)
                ).apply(instance, PureDaisyRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PureDaisyRecipe> STREAM_CODEC =
                StreamCodec.of(
                        (buf, recipe) -> {
                            buf.writeNbt(net.minecraft.nbt.NbtUtils.writeBlockState(recipe.input()));
                            buf.writeNbt(net.minecraft.nbt.NbtUtils.writeBlockState(recipe.output()));
                            buf.writeInt(recipe.conversionTime());
                        },
                        buf -> {
                            BlockState in = net.minecraft.nbt.NbtUtils.readBlockState(
                                    net.minecraft.core.registries.BuiltInRegistries.BLOCK.asLookup(),
                                    buf.readNbt());
                            BlockState out = net.minecraft.nbt.NbtUtils.readBlockState(
                                    net.minecraft.core.registries.BuiltInRegistries.BLOCK.asLookup(),
                                    buf.readNbt());
                            int time = buf.readInt();
                            return new PureDaisyRecipe(in, out, time);
                        });

        @Override public MapCodec<PureDaisyRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, PureDaisyRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
