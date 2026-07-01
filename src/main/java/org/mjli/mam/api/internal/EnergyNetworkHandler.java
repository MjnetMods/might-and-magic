package org.mjli.mam.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.mjli.mam.api.energy.EnergyCollector;
import org.mjli.mam.api.energy.EnergyPool;

import javax.annotation.Nullable;
import java.util.*;

public class EnergyNetworkHandler implements EnergyNetwork {
    public static final EnergyNetworkHandler INSTANCE = new EnergyNetworkHandler();

    private final Map<Level, Map<BlockPos, EnergyCollector>> collectors = new WeakHashMap<>();
    private final Map<Level, Map<BlockPos, EnergyPool>> pools = new WeakHashMap<>();

    @Override
    public @Nullable EnergyCollector getClosestCollector(BlockPos pos, Level level, int radius) {
        return queryClosest(collectors.getOrDefault(level, Collections.emptyMap()), pos, radius);
    }

    @Override
    public @Nullable EnergyPool getClosestPool(BlockPos pos, Level level, int radius) {
        return queryClosest(pools.getOrDefault(level, Collections.emptyMap()), pos, radius);
    }

    /** Returns the value whose key is closest to {@code origin} within {@code radius} blocks, or null. */
    static @Nullable <T> T queryClosest(Map<BlockPos, T> byPos, BlockPos origin, int radius) {
        T closest = null;
        double closestDist = Double.MAX_VALUE;
        for (Map.Entry<BlockPos, T> e : byPos.entrySet()) {
            double dist = e.getKey().distSqr(origin);
            if (dist <= (double) radius * radius && dist < closestDist) {
                closestDist = dist;
                closest = e.getValue();
            }
        }
        return closest;
    }

    @Override
    public Set<EnergyCollector> getAllCollectorsInWorld(Level level) {
        return Collections.unmodifiableSet(new HashSet<>(collectors.getOrDefault(level, Collections.emptyMap()).values()));
    }

    @Override
    public Set<EnergyPool> getAllPoolsInWorld(Level level) {
        return Collections.unmodifiableSet(new HashSet<>(pools.getOrDefault(level, Collections.emptyMap()).values()));
    }

    @Override
    public void fireEvent(Object thing, EnergyBlockType type, EnergyNetworkAction action) {
        if (!(thing instanceof BlockEntity be)) return;
        Level level = be.getLevel();
        if (level == null) return;
        BlockPos pos = be.getBlockPos();
        if (type == EnergyBlockType.COLLECTOR && thing instanceof EnergyCollector c) {
            Map<BlockPos, EnergyCollector> map = collectors.computeIfAbsent(level, k -> new HashMap<>());
            if (action == EnergyNetworkAction.ADD) map.put(pos, c); else map.remove(pos);
        } else if (type == EnergyBlockType.POOL && thing instanceof EnergyPool p) {
            Map<BlockPos, EnergyPool> map = pools.computeIfAbsent(level, k -> new HashMap<>());
            if (action == EnergyNetworkAction.ADD) map.put(pos, p); else map.remove(pos);
        }
    }
}
