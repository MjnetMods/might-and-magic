package org.mjli.mam.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import org.junit.jupiter.api.Test;
import org.mjli.mam.api.energy.EnergyPool;
import org.mjli.mam.api.energy.EnergyType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EnergyNetworkHandlerTest {

    private static EnergyPool stub() {
        return new EnergyPool() {
            @Override public int getCurrentEnergy() { return 0; }
            @Override public int getMaxEnergy() { return 1_000_000; }
            @Override public boolean isFull() { return false; }
            @Override public void receiveEnergy(int m) {}
            @Override public void receiveEnergy(int m, EnergyType t) {}
            @Override public boolean canReceiveEnergyFromBursts() { return true; }
            @Override public boolean isOutputtingPower() { return false; }
            @Override public Optional<DyeColor> getColor() { return Optional.empty(); }
            @Override public void setColor(Optional<DyeColor> c) {}
            @Override public EnergyType getEnergyType() { return EnergyType.MANA; }
        };
    }

    // ── GF-2: no pool in range ─────────────────────────────────────────────────

    @Test
    void queryClosest_returnsNull_whenMapEmpty() {
        assertNull(EnergyNetworkHandler.queryClosest(Map.of(), BlockPos.ZERO, 6));
    }

    @Test
    void queryClosest_returnsNull_whenOnlyPoolIsOutOfRadius() {
        Map<BlockPos, EnergyPool> byPos = Map.of(new BlockPos(100, 0, 0), stub());
        assertNull(EnergyNetworkHandler.queryClosest(byPos, BlockPos.ZERO, 6));
    }

    // ── GF-3: prefers closer pool ──────────────────────────────────────────────

    @Test
    void queryClosest_prefersNearer_whenMultiplePools() {
        EnergyPool near = stub();
        EnergyPool far = stub();
        Map<BlockPos, EnergyPool> byPos = new HashMap<>();
        byPos.put(new BlockPos(2, 0, 0), near);   // dist² = 4  — within radius 6
        byPos.put(new BlockPos(5, 0, 0), far);    // dist² = 25 — within radius 6
        assertSame(near, EnergyNetworkHandler.queryClosest(byPos, BlockPos.ZERO, 6));
    }

    @Test
    void queryClosest_excludesFarPool_whenOnlyNearIsInRadius() {
        EnergyPool near = stub();
        Map<BlockPos, EnergyPool> byPos = new HashMap<>();
        byPos.put(new BlockPos(2, 0, 0), near);
        byPos.put(new BlockPos(10, 0, 0), stub());  // outside radius 6
        assertSame(near, EnergyNetworkHandler.queryClosest(byPos, BlockPos.ZERO, 6));
    }
}
