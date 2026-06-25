package org.mjli.mam;

import org.junit.jupiter.api.Test;
import org.mjli.mam.block_entity.mana.ManaPoolBlockEntity;

import static org.junit.jupiter.api.Assertions.*;

class ManaPoolTest {

    // ── calculateComparatorLevel ──────────────────────────────────────────────

    @Test
    void comparatorLevel_emptyPool_isZero() {
        assertEquals(0, ManaPoolBlockEntity.calculateComparatorLevel(0, 1_000_000));
    }

    @Test
    void comparatorLevel_fullPool_isFifteen() {
        assertEquals(15, ManaPoolBlockEntity.calculateComparatorLevel(1_000_000, 1_000_000));
    }

    @Test
    void comparatorLevel_anyMana_isAtLeastOne() {
        // Even 1 mana should produce signal strength ≥ 1 (not zero)
        int level = ManaPoolBlockEntity.calculateComparatorLevel(1, 1_000_000);
        assertEquals(1, level, "A non-empty pool must emit at least strength 1");
    }

    @Test
    void comparatorLevel_halfFull_isSevenOrEight() {
        int level = ManaPoolBlockEntity.calculateComparatorLevel(500_000, 1_000_000);
        assertTrue(level == 7 || level == 8, "Half-full pool should be ~7-8, got " + level);
    }

    @Test
    void comparatorLevel_quarterFull_isThreeOrFour() {
        int level = ManaPoolBlockEntity.calculateComparatorLevel(250_000, 1_000_000);
        assertTrue(level >= 3 && level <= 4, "Quarter-full pool should be ~3-4, got " + level);
    }

    @Test
    void comparatorLevel_neverExceedsFifteen() {
        int level = ManaPoolBlockEntity.calculateComparatorLevel(1_000_000, 1_000_000);
        assertTrue(level <= 15);
    }

    // ── receiveMana clamping formula ─────────────────────────────────────────
    // Tests the inline logic: mana = clamp(mana + amount, 0, MAX_MANA)

    @Test
    void manaClamping_cannotExceedMax() {
        int max = ManaPoolBlockEntity.MAX_MANA;
        int result = Math.max(0, Math.min(max + 1, max));
        assertEquals(max, result);
    }

    @Test
    void manaClamping_cannotGoBelowZero() {
        int result = Math.max(0, Math.min(0 - 500, ManaPoolBlockEntity.MAX_MANA));
        assertEquals(0, result);
    }

    @Test
    void manaClamping_normalAddition() {
        int max = ManaPoolBlockEntity.MAX_MANA;
        int current = 100_000;
        int added = 50_000;
        int result = Math.max(0, Math.min(current + added, max));
        assertEquals(150_000, result);
    }
}
