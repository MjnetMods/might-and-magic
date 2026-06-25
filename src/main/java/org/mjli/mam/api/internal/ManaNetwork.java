package org.mjli.mam.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mjli.mam.api.mana.ManaCollector;
import org.mjli.mam.api.mana.ManaPool;

import javax.annotation.Nullable;
import java.util.Set;

public interface ManaNetwork {
    @Nullable ManaCollector getClosestCollector(BlockPos pos, Level level, int radius);
    @Nullable ManaPool getClosestPool(BlockPos pos, Level level, int radius);
    Set<ManaCollector> getAllCollectorsInWorld(Level level);
    Set<ManaPool> getAllPoolsInWorld(Level level);
    void fireManaNetworkEvent(Object thing, ManaBlockType type, ManaNetworkAction action);
}
