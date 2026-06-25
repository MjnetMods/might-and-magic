package org.mjli.mam.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mjli.mam.api.mana.ManaCollector;
import org.mjli.mam.api.mana.ManaPool;

import javax.annotation.Nullable;
import java.util.*;

public class ManaNetworkHandler implements ManaNetwork {
    public static final ManaNetworkHandler INSTANCE = new ManaNetworkHandler();

    private final Map<Level, Set<ManaCollector>> collectors = new WeakHashMap<>();
    private final Map<Level, Set<ManaPool>> pools = new WeakHashMap<>();

    @Override
    public @Nullable ManaCollector getClosestCollector(BlockPos pos, Level level, int radius) {
        Set<ManaCollector> inWorld = collectors.getOrDefault(level, Collections.emptySet());
        ManaCollector closest = null;
        double closestDist = Double.MAX_VALUE;
        for (ManaCollector c : inWorld) {
            if (c instanceof net.minecraft.world.level.block.entity.BlockEntity be) {
                double dist = be.getBlockPos().distSqr(pos);
                if (dist <= (double) radius * radius && dist < closestDist) {
                    closestDist = dist;
                    closest = c;
                }
            }
        }
        return closest;
    }

    @Override
    public @Nullable ManaPool getClosestPool(BlockPos pos, Level level, int radius) {
        Set<ManaPool> inWorld = pools.getOrDefault(level, Collections.emptySet());
        ManaPool closest = null;
        double closestDist = Double.MAX_VALUE;
        for (ManaPool p : inWorld) {
            if (p instanceof net.minecraft.world.level.block.entity.BlockEntity be) {
                double dist = be.getBlockPos().distSqr(pos);
                if (dist <= (double) radius * radius && dist < closestDist) {
                    closestDist = dist;
                    closest = p;
                }
            }
        }
        return closest;
    }

    @Override
    public Set<ManaCollector> getAllCollectorsInWorld(Level level) {
        return Collections.unmodifiableSet(collectors.getOrDefault(level, Collections.emptySet()));
    }

    @Override
    public Set<ManaPool> getAllPoolsInWorld(Level level) {
        return Collections.unmodifiableSet(pools.getOrDefault(level, Collections.emptySet()));
    }

    @Override
    public void fireManaNetworkEvent(Object thing, ManaBlockType type, ManaNetworkAction action) {
        if (!(thing instanceof net.minecraft.world.level.block.entity.BlockEntity be)) return;
        Level level = be.getLevel();
        if (level == null) return;
        if (type == ManaBlockType.COLLECTOR && thing instanceof ManaCollector c) {
            Set<ManaCollector> set = collectors.computeIfAbsent(level, k -> new HashSet<>());
            if (action == ManaNetworkAction.ADD) set.add(c); else set.remove(c);
        } else if (type == ManaBlockType.POOL && thing instanceof ManaPool p) {
            Set<ManaPool> set = pools.computeIfAbsent(level, k -> new HashSet<>());
            if (action == ManaNetworkAction.ADD) set.add(p); else set.remove(p);
        }
    }
}
