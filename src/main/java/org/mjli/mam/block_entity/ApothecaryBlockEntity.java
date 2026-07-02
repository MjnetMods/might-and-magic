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
import org.mjli.mam.verdant.VerdantMana;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApothecaryBlockEntity extends BlockEntity {
    // Slot capacity per design/magic/10_apothecary.md tier table (each tier + 1 seed reagent).
    // One BlockEntityType is shared across all four tier blocks, so capacity is resolved per
    // instance from the block state rather than split into per-tier BlockEntityTypes like
    // ManaPoolBlockEntity does.
    private static final int TIER_1_INGREDIENTS = 4;
    private static final int TIER_2_INGREDIENTS = 6;
    private static final int TIER_3_INGREDIENTS = 64;

    private final int maxIngredients;

    // 20s window to recraft the last recipe, matching Botania's PetalApothecaryBlockEntity
    private static final int RECIPE_KEEP_TICKS = 400;

    // 1 bucket; no validator — accepts any bucket-compatible fluid per design/magic/10_apothecary.md
    private final FluidTank tank = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private final List<ItemStack> ingredients = new ArrayList<>();

    // Transient recraft memory — intentionally not saved to NBT, same as Botania's reference
    @Nullable
    private List<ItemStack> lastRecipe = null;
    private int recipeKeepTicks = 0;

    public ApothecaryBlockEntity(BlockPos pos, BlockState state) {
        super(MamBlockEntities.APOTHECARY.get(), pos, state);
        this.maxIngredients = capacityFor(state.getBlock());
    }

    private static int capacityFor(Block block) {
        if (block == VerdantMana.SACRED_APOTHECARY.get() || block == VerdantMana.DESECRATED_APOTHECARY.get()) {
            return TIER_3_INGREDIENTS;
        }
        if (block == VerdantMana.INFUSED_APOTHECARY.get()) {
            return TIER_2_INGREDIENTS;
        }
        return TIER_1_INGREDIENTS;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ApothecaryBlockEntity self) {
        if (level.isClientSide) return;
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos.above()));
        for (ItemEntity item : items) {
            self.collideEntityItem(item);
        }
        self.tickRecipeKeep();
    }

    private void tickRecipeKeep() {
        if (recipeKeepTicks > 0) {
            recipeKeepTicks--;
        } else {
            lastRecipe = null;
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

            lastRecipe = ingredients.stream().map(ItemStack::copy).toList();
            recipeKeepTicks = RECIPE_KEEP_TICKS;

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

        if (ingredients.size() < maxIngredients) {
            ingredients.add(stack.split(1));
            if (stack.isEmpty()) item.discard(); else item.setItem(stack);
            setChanged();
            return true;
        }

        return false;
    }

    public InteractionResult interact(Player player) {
        boolean mainHandEmpty = player.getMainHandItem().isEmpty();

        if (mainHandEmpty && canAddLastRecipe()) {
            return trySetLastRecipe(player);
        }

        if (mainHandEmpty && !ingredients.isEmpty()) {
            ItemStack retracted = ingredients.remove(ingredients.size() - 1);
            player.getInventory().placeItemBackInInventory(retracted);
            setChanged();
            return InteractionResult.SUCCESS;
        }

        boolean handled = FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, getLevel(), worldPosition, null);
        return handled ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    // Ingredients drained on craft (see collideEntityItem), so both the ingredient list
    // and the fluid must be empty/refilled before a recraft can be offered again.
    private boolean canAddLastRecipe() {
        return ingredients.isEmpty() && !tank.isEmpty() && lastRecipe != null && !lastRecipe.isEmpty();
    }

    // Pulls whatever ingredients the player happens to be carrying, one at a time — a partial
    // match still helps (matches Botania's InventoryHelper.tryToSetLastRecipe: best-effort, not all-or-nothing).
    private InteractionResult trySetLastRecipe(Player player) {
        boolean addedAny = false;
        for (ItemStack want : lastRecipe) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack held = player.getInventory().getItem(i);
                if (player.isCreative() || (!held.isEmpty() && ItemStack.isSameItemSameComponents(want, held))) {
                    ingredients.add(player.isCreative() ? want.copyWithCount(1) : held.split(1));
                    addedAny = true;
                    break;
                }
            }
        }

        if (addedAny) setChanged();
        return addedAny ? InteractionResult.SUCCESS : InteractionResult.PASS;
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
