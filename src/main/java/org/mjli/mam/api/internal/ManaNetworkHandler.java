package org.mjli.mam.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.mjli.mam.api.mana.ManaCollector;
import org.mjli.mam.api.mana.ManaPool;

import javax.annotation.Nullable;
import java.util.*;

public class ManaNetworkHandler implements ManaNetwork {
    public static final ManaNetworkHandler INSTANCE = new ManaNetworkHandler();

    private final Map<Level, Map<BlockPos, ManaCollector>> collectors = new WeakHashMap<>();
    private final Map<Level, Map<BlockPos, ManaPool>> pools = new WeakHashMap<>();

    @Override
    public @Nullable ManaCollector getClosestCollector(BlockPos pos, Level level, int radius) {
        return queryClosest(collectors.getOrDefault(level, Collections.emptyMap()), pos, radius);
    }

    @Override
    public @Nullable ManaPool getClosestPool(BlockPos pos, Level level, int radius) {
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
    public Set<ManaCollector> getAllCollectorsInWorld(Level level) {
        return Collections.unmodifiableSet(new HashSet<>(collectors.getOrDefault(level, Collections.emptyMap()).values()));
    }

    @Override
    public Set<ManaPool> getAllPoolsInWorld(Level level) {
        return Collections.unmodifiableSet(new HashSet<>(pools.getOrDefault(level, Collections.emptyMap()).values()));
    }

    @Override
    public void fireManaNetworkEvent(Object thing, ManaBlockType type, ManaNetworkAction action) {
        if (!(thing instanceof BlockEntity be)) return;
        Level level = be.getLevel();
        if (level == null) return;
        BlockPos pos = be.getBlockPos();
        if (type == ManaBlockType.COLLECTOR && thing instanceof ManaCollector c) {
            Map<BlockPos, ManaCollector> map = collectors.computeIfAbsent(level, k -> new HashMap<>());
            if (action == ManaNetworkAction.ADD) map.put(pos, c); else map.remove(pos);
        } else if (type == ManaBlockType.POOL && thing instanceof ManaPool p) {
            Map<BlockPos, ManaPool> map = pools.computeIfAbsent(level, k -> new HashMap<>());
            if (action == ManaNetworkAction.ADD) map.put(pos, p); else map.remove(pos);
        }
    }
}
