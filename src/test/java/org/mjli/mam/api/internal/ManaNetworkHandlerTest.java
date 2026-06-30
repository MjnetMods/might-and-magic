package org.mjli.mam.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import org.junit.jupiter.api.Test;
import org.mjli.mam.api.mana.ManaEnergyType;
import org.mjli.mam.api.mana.ManaPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ManaNetworkHandlerTest {

    private static ManaPool stub() {
        return new ManaPool() {
            @Override public int getCurrentMana() { return 0; }
            @Override public int getMaxMana() { return 1_000_000; }
            @Override public boolean isFull() { return false; }
            @Override public void receiveMana(int m) {}
            @Override public boolean canReceiveManaFromBursts() { return true; }
            @Override public boolean isOutputtingPower() { return false; }
            @Override public Optional<DyeColor> getColor() { return Optional.empty(); }
            @Override public void setColor(Optional<DyeColor> c) {}
            @Override public ManaEnergyType getEnergyType() { return ManaEnergyType.MANA; }
        };
    }

    // ── GF-2: no pool in range ─────────────────────────────────────────────────

    @Test
    void queryClosest_returnsNull_whenMapEmpty() {
        assertNull(ManaNetworkHandler.queryClosest(Map.of(), BlockPos.ZERO, 6));
    }

    @Test
    void queryClosest_returnsNull_whenOnlyPoolIsOutOfRadius() {
        Map<BlockPos, ManaPool> byPos = Map.of(new BlockPos(100, 0, 0), stub());
        assertNull(ManaNetworkHandler.queryClosest(byPos, BlockPos.ZERO, 6));
    }

    // ── GF-3: prefers closer pool ──────────────────────────────────────────────

    @Test
    void queryClosest_prefersNearer_whenMultiplePools() {
        ManaPool near = stub();
        ManaPool far = stub();
        Map<BlockPos, ManaPool> byPos = new HashMap<>();
        byPos.put(new BlockPos(2, 0, 0), near);   // dist² = 4  — within radius 6
        byPos.put(new BlockPos(5, 0, 0), far);    // dist² = 25 — within radius 6
        assertSame(near, ManaNetworkHandler.queryClosest(byPos, BlockPos.ZERO, 6));
    }

    @Test
    void queryClosest_excludesFarPool_whenOnlyNearIsInRadius() {
        ManaPool near = stub();
        Map<BlockPos, ManaPool> byPos = new HashMap<>();
        byPos.put(new BlockPos(2, 0, 0), near);
        byPos.put(new BlockPos(10, 0, 0), stub());  // outside radius 6
        assertSame(near, ManaNetworkHandler.queryClosest(byPos, BlockPos.ZERO, 6));
    }
}
