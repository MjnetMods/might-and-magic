package org.mjli.mam.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.mjli.mam.MamBlockEntities;
import javax.annotation.Nullable;
import org.mjli.mam.MamRecipes;
import org.mjli.mam.recipe.ApothecaryInput;
import org.mjli.mam.recipe.ApothecaryRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApothecaryBlockEntity extends BlockEntity {
    // T1 capacity per design/magic/10_apothecary.md tier table (4 + 1 seed reagent)
    private static final int MAX_INGREDIENTS = 4;

    // 1 bucket; no validator — accepts any bucket-compatible fluid per design/magic/10_apothecary.md
    private final FluidTank tank = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private final List<ItemStack> ingredients = new ArrayList<>();

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
        if (stack.isEmpty() || tank.isEmpty()) return false;

        Level level = getLevel();
        ApothecaryInput view = new ApothecaryInput(List.copyOf(ingredients));
        Optional<RecipeHolder<ApothecaryRecipe>> match = level.getRecipeManager()
                .getRecipeFor(MamRecipes.APOTHECARY_TYPE.get(), view, level);
        if (match.isPresent() && match.get().value().getReagent().test(stack)) {
            ApothecaryRecipe recipe = match.get().value();
            ItemStack result = recipe.assemble(view, level.registryAccess());

            ingredients.clear();
            stack.shrink(1);
            if (stack.isEmpty()) item.discard(); else item.setItem(stack);

            ItemEntity outputEntity = new ItemEntity(level,
                    worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, result);
            level.addFreshEntity(outputEntity);

            tank.drain(tank.getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
            setChanged();
            return true;
        }

        if (ingredients.size() < MAX_INGREDIENTS) {
            ingredients.add(stack.split(1));
            if (stack.isEmpty()) item.discard(); else item.setItem(stack);
            setChanged();
            return true;
        }

        return false;
    }

    public InteractionResult interact(Player player) {
        boolean handled = FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, getLevel(), worldPosition, null);
        return handled ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    public FluidTank getFluidTank() { return tank; }
    public List<ItemStack> getIngredients() { return ingredients; }

    @Override
    public void setChanged() {
        super.setChanged();
        // Client rendering depends on the fluid tank; setChanged() alone only marks the chunk
        // dirty for saving, it does not by itself notify watching clients.
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("fluid", tank.writeToNBT(registries, new CompoundTag()));
        CompoundTag ingredientTag = new CompoundTag();
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientTag.put(String.valueOf(i), ingredients.get(i).save(registries));
        }
        tag.put("ingredients", ingredientTag);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        tank.readFromNBT(registries, tag.getCompound("fluid"));
        ingredients.clear();
        CompoundTag ingredientTag = tag.getCompound("ingredients");
        for (String key : ingredientTag.getAllKeys()) {
            ItemStack.parse(registries, ingredientTag.getCompound(key)).ifPresent(ingredients::add);
        }
    }
}
