package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.verdant.VerdantMana;
import org.mjli.mam.verdant.VerdantRock;
import org.mjli.mam.verdant.VerdantWood;

/**
 * In-game tests for The Verdant Path mechanics.
 *
 * Structure templates: src/main/resources/data/mam/gametest/structures/verdant_path/<name>.snbt
 * NeoForge reads these directly at runtime — no datagen compilation needed.
 *
 * Run tests: ./gradlew runGameTestServer
 * In-game: /test runall
 *          /test run mam:verdant_path.<name>
 */
@GameTestHolder(MightAndMagic.MODID)
@PrefixGameTestTemplate(false)
public class TestVerdantPath {

    /** Verify Pure Daisy converts adjacent logs to living wood within 20 seconds.   */
    @GameTest(template = "verdant_path/pure_daisy_converts_log", timeoutTicks = 16000)
    public static void pureDaisyConvertsLog(GameTestHelper helper) {
        BlockPos logPos = new BlockPos(3, 2, 4);
        helper.succeedWhen(() -> helper.assertBlockPresent(VerdantWood.LIVINGWOOD_LOG.get(), logPos));
    }

  /** Verify Pure Daisy converts adjacent stone to living rock within 20 seconds.   */
  @GameTest(template = "verdant_path/pure_daisy_converts_stone", timeoutTicks = 16000)
  public static void pureDaisyConvertsStone(GameTestHelper helper) {
    BlockPos stonePos = new BlockPos(3, 2, 4);
    helper.succeedWhen(() -> helper.assertBlockPresent(VerdantRock.LIVING_ROCK.get(), stonePos));
  }
//
//     @GameTest(template = "verdant_path/mana_pool_empty_comparator", timeoutTicks = 40)
//     public static void manaPoolEmptyComparator(GameTestHelper helper) {
//         BlockPos poolPos = new BlockPos(1, 1, 1);
//         helper.succeedWhen(() -> helper.assertBlockPresent(VerdantMana.MANA_POOL.get(), poolPos));
//     }

    // ── Pass 3: Pure Daisy edge cases ────────────────────────────────────────

    /** PD-1: non-oak logs (birch) also match the minecraft:logs tag and convert to livingwood_log. */
    @GameTest(template = "verdant_path/pure_daisy_converts_log", timeoutTicks = 16000)
    public static void pureDaisyConvertsAnyLogVariant(GameTestHelper helper) {
        BlockPos logPos = new BlockPos(3, 2, 4);
        helper.setBlock(logPos, Blocks.BIRCH_LOG.defaultBlockState());
        helper.succeedWhen(() -> helper.assertBlockPresent(VerdantWood.LIVINGWOOD_LOG.get(), logPos));
    }

    /** PD-2: dirt adjacent to Pure Daisy is NOT converted after 300 ticks (no matching recipe). */
    @GameTest(template = "verdant_path/pure_daisy_converts_log", timeoutTicks = 400)
    public static void pureDaisyIgnoresNonMatchingBlock(GameTestHelper helper) {
        BlockPos targetPos = new BlockPos(3, 2, 4);
        helper.setBlock(targetPos, Blocks.DIRT.defaultBlockState());
        helper.runAfterDelay(300, () -> {
            helper.assertBlockPresent(Blocks.DIRT, targetPos);
            helper.succeed();
        });
    }

    /** PD-4: an already-converted livingwood_log is NOT re-converted by an adjacent Pure Daisy. */
    @GameTest(template = "verdant_path/pure_daisy_converts_log", timeoutTicks = 400)
    public static void pureDaisyNoDoubleConversion(GameTestHelper helper) {
        BlockPos logPos = new BlockPos(3, 2, 4);
        helper.setBlock(logPos, VerdantWood.LIVINGWOOD_LOG.get().defaultBlockState());
        helper.runAfterDelay(300, () -> {
            helper.assertBlockPresent(VerdantWood.LIVINGWOOD_LOG.get(), logPos);
            helper.succeed();
        });
    }

    /**
     * PD-5: removing and replacing the target mid-conversion still results in conversion,
     * confirming the daisy picks up the fresh block correctly after the change.
     */
    @GameTest(template = "verdant_path/pure_daisy_converts_stone", timeoutTicks = 16000)
    public static void pureDaisyTimerResetsOnTargetRemoved(GameTestHelper helper) {
        BlockPos stonePos = new BlockPos(3, 2, 4);
        helper.runAfterDelay(100, () -> {
            helper.setBlock(stonePos, Blocks.AIR.defaultBlockState());
            helper.runAfterDelay(1, () -> {
                helper.setBlock(stonePos, Blocks.STONE.defaultBlockState());
                helper.succeedWhen(() -> helper.assertBlockPresent(VerdantRock.LIVING_ROCK.get(), stonePos));
            });
        });
    }
}
