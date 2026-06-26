package org.mjli.mam.block_entity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamBlockEntities;
import org.mjli.mam.recipe.PureDaisyRecipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PureDaisyBlockEntity extends BlockEntity {
    private static final BlockPos[] SURROUNDING = {
        new BlockPos(0, 0, 1), new BlockPos(1, 0, 1), new BlockPos(1, 0, 0),
        new BlockPos(1, 0, -1), new BlockPos(0, 0, -1), new BlockPos(-1, 0, -1),
        new BlockPos(-1, 0, 0), new BlockPos(-1, 0, 1)
    };

    private final Map<BlockPos, Integer> conversionTimers = new HashMap<>();
    private int tick;

    public PureDaisyBlockEntity(BlockPos pos, BlockState state) {
        super(MamBlockEntities.PURE_DAISY.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PureDaisyBlockEntity self) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        self.tick++;

        if (self.tick == 1) System.out.println("[MAM] PureDaisy serverTick IS running at " + pos);

        BlockPos relative = SURROUNDING[self.tick % SURROUNDING.length];
        BlockPos target = pos.offset(relative);
        BlockState targetState = level.getBlockState(target);

        Optional<PureDaisyRecipe> recipe = PureDaisyRecipe.findRecipe(serverLevel, targetState);
        if (self.tick <= 20) System.out.println("[MAM] tick=" + self.tick + " target=" + target + " block=" + targetState.getBlock().getDescriptionId() + " recipe=" + recipe.isPresent());
        if (recipe.isEmpty()) {
            self.conversionTimers.remove(target);
            return;
        }

        int timer = self.conversionTimers.merge(target, 1, Integer::sum);
        int required = recipe.get().conversionTime();
        if (timer >= required) {
            level.setBlockAndUpdate(target, recipe.get().output());
            self.conversionTimers.remove(target);
            self.setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag timers = new CompoundTag();
        conversionTimers.forEach((pos, t) -> timers.putInt(pos.asLong() + "", t));
        tag.put("timers", timers);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        conversionTimers.clear();
        CompoundTag timers = tag.getCompound("timers");
        for (String key : timers.getAllKeys()) {
            try {
                conversionTimers.put(BlockPos.of(Long.parseLong(key)), timers.getInt(key));
            } catch (NumberFormatException ignored) {}
        }
    }
}
