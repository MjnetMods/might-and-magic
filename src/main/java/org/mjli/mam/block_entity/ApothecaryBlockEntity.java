package org.mjli.mam.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamBlockEntities;

import java.util.ArrayList;
import java.util.List;

public class ApothecaryBlockEntity extends BlockEntity {
    public enum FluidState { EMPTY, WATER, LAVA }

    private final List<ItemStack> petals = new ArrayList<>();
    private FluidState fluid = FluidState.EMPTY;

    public ApothecaryBlockEntity(BlockPos pos, BlockState state) {
        super(MamBlockEntities.APOTHECARY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ApothecaryBlockEntity self) {
        // future: recipe matching tick
    }

    public InteractionResult interact(Player player) {
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty()) return InteractionResult.PASS;

        // TODO: handle bucket fill (water), petal addition, reagent + crafting
        return InteractionResult.SUCCESS;
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
