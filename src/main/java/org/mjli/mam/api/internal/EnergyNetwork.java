package org.mjli.mam.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mjli.mam.api.energy.EnergyCollector;
import org.mjli.mam.api.energy.EnergyPool;

import javax.annotation.Nullable;
import java.util.Set;

public interface EnergyNetwork {
    @Nullable EnergyCollector getClosestCollector(BlockPos pos, Level level, int radius);
    @Nullable EnergyPool getClosestPool(BlockPos pos, Level level, int radius);
    Set<EnergyCollector> getAllCollectorsInWorld(Level level);
    Set<EnergyPool> getAllPoolsInWorld(Level level);
    void fireEvent(Object thing, EnergyBlockType type, EnergyNetworkAction action);
}
