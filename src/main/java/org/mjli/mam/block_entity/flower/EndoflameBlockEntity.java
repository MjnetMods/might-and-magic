package org.mjli.mam.block_entity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.mjli.mam.MamBlockEntities;

public class EndoflameBlockEntity extends GeneratingFlowerBlockEntity {
    private static final int RANGE = 3;
    private static final int FUEL_CAP = 32000;

    private int burnTime;

    public EndoflameBlockEntity(BlockPos pos, BlockState state) {
        super(MamBlockEntities.ENDOFLAME.get(), pos, state);
    }

    @Override
    public void tickFlower() {
        Level level = getLevel();
        if (level == null || level.isClientSide) return;

        if (burnTime > 0) {
            burnTime--;
            if (burnTime % 2 == 0) addMana(3);
            return;
        }

        if (isFull()) return;

        AABB scanBox = new AABB(worldPosition).inflate(RANGE);
        for (ItemEntity itemEnt : level.getEntitiesOfClass(ItemEntity.class, scanBox)) {
            ItemStack stack = itemEnt.getItem();
            if (stack.isEmpty() || stack.hasCraftingRemainingItem()) continue;
            int bt = stack.getBurnTime(RecipeType.SMELTING);
            if (bt > 0) {
                burnTime = Math.min(FUEL_CAP, bt) / 2;
                stack.shrink(1);
                if (stack.isEmpty()) itemEnt.discard();
                setChanged();
                return;
            }
        }
    }

    @Override
    public int getMaxMana() {
        return 300;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("burnTime", burnTime);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        burnTime = tag.getInt("burnTime");
    }
}
