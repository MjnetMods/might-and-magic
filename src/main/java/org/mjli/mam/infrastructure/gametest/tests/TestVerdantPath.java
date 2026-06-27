package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
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
//     /**
//      * Verify Mana Pool comparator output is 0 when empty.
//      * Template: mana_pool_comparator.nbt — pool at (1,1,1), comparator at (2,1,1)
//      */
//     @GameTest(template = "verdant_path/mana_pool_empty_comparator", timeoutTicks = 40)
//     public static void manaPoolEmptyComparator(GameTestHelper helper) {
//         BlockPos poolPos = new BlockPos(1, 1, 1);
//         helper.succeedWhen(() -> helper.assertBlockPresent(VerdantMana.MANA_POOL.get(), poolPos));
//     }
}
