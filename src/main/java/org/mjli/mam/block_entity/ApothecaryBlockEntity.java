package org.mjli.mam.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.phys.AABB;
import org.mjli.mam.MamBlockEntities;
import org.mjli.mam.MamRecipes;
import org.mjli.mam.recipe.ApothecaryInput;
import org.mjli.mam.recipe.ApothecaryRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApothecaryBlockEntity extends BlockEntity {
    public enum FluidState { EMPTY, WATER, LAVA }

    // T1 capacity per design/magic/10_apothecary.md tier table (4 + 1 seed reagent)
    private static final int MAX_INGREDIENTS = 4;

    private final List<ItemStack> petals = new ArrayList<>();
    private FluidState fluid = FluidState.EMPTY;

    public ApothecaryBlockEntity(BlockPos pos, BlockState state) {
        super(MamBlockEntities.APOTHECARY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ApothecaryBlockEntity self) {
        if (level.isClientSide) return;
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos.above()));
        for (ItemEntity item : items) {
            self.collideEntityItem(item);
        }
    }

    private boolean collideEntityItem(ItemEntity item) {
        ItemStack stack = item.getItem();
        if (stack.isEmpty() || fluid == FluidState.EMPTY) return false;

        Level level = getLevel();
        ApothecaryInput view = new ApothecaryInput(List.copyOf(petals));
        Optional<RecipeHolder<ApothecaryRecipe>> match = level.getRecipeManager()
                .getRecipeFor(MamRecipes.APOTHECARY_TYPE.get(), view, level);
        if (match.isPresent() && match.get().value().getReagent().test(stack)) {
            ApothecaryRecipe recipe = match.get().value();
            ItemStack result = recipe.assemble(view, level.registryAccess());

            petals.clear();
            stack.shrink(1);
            if (stack.isEmpty()) item.discard(); else item.setItem(stack);

            ItemEntity outputEntity = new ItemEntity(level,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, result);
            level.addFreshEntity(outputEntity);

            fluid = FluidState.EMPTY;
            setChanged();
            return true;
        }

        if (petals.size() < MAX_INGREDIENTS) {
            petals.add(stack.split(1));
            if (stack.isEmpty()) item.discard(); else item.setItem(stack);
            setChanged();
            return true;
        }

        return false;
    }

    public InteractionResult interact(Player player) {
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty()) return InteractionResult.PASS;

        if (fluid == FluidState.EMPTY) {
            FluidState fillState = held.is(Items.WATER_BUCKET) ? FluidState.WATER
                    : held.is(Items.LAVA_BUCKET) ? FluidState.LAVA
                    : null;
            if (fillState != null) {
                fluid = fillState;
                held.shrink(1);
                ItemStack emptyBucket = new ItemStack(Items.BUCKET);
                if (!player.getInventory().add(emptyBucket)) player.drop(emptyBucket, false);
                setChanged();
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public FluidState getFluidState() { return fluid; }
    public List<ItemStack> getPetals() { return petals; }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putByte("fluid", (byte) fluid.ordinal());
        CompoundTag petalTag = new CompoundTag();
        for (int i = 0; i < petals.size(); i++) {
            petalTag.put(String.valueOf(i), petals.get(i).save(registries));
        }
        tag.put("petals", petalTag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        byte fluidId = tag.getByte("fluid");
        fluid = fluidId >= 0 && fluidId < FluidState.values().length ? FluidState.values()[fluidId] : FluidState.EMPTY;
        petals.clear();
        CompoundTag petalTag = tag.getCompound("petals");
        for (String key : petalTag.getAllKeys()) {
            ItemStack.parse(registries, petalTag.getCompound(key)).ifPresent(petals::add);
        }
    }
}
