package org.mjli.mam.infrastructure.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Utility wrapper around GameTestHelper with MAM-specific helpers.
 * Test methods receive a plain GameTestHelper; use these as static utilities.
 */
public class MamGameTestHelper {

    public static final int TICKS_PER_SECOND = 20;
    public static final int TEN_SECONDS = 200;
    public static final int THIRTY_SECONDS = 600;

    public static <T extends BlockEntity> T getBlockEntity(GameTestHelper helper, BlockPos pos, Class<T> type) {
        BlockEntity be = helper.getBlockEntity(pos);
        if (!type.isInstance(be)) {
            throw new RuntimeException("Expected " + type.getSimpleName() + " at " + pos + " but got " + be);
        }
        return type.cast(be);
    }

    public static void assertBlockStateEquals(GameTestHelper helper, BlockPos pos, BlockState expected) {
        BlockState actual = helper.getBlockState(pos);
        if (!actual.equals(expected)) {
            helper.fail("Expected " + expected + " at " + pos + " but got " + actual);
        }
    }
}
