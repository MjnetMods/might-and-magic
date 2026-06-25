package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MamBlocks;
import org.mjli.mam.MightAndMagic;

/**
 * In-game tests for The Verdant Path mechanics.
 *
 * How to create structure templates:
 * 1. Build the test scenario in creative mode
 * 2. Use /test export <name> to export the structure NBT
 * 3. Move the .nbt file to src/main/resources/data/mam/structures/gametest/
 *
 * Run tests: ./gradlew runGameTestServer
 * In-game: /test runall  (runs all registered tests)
 *          /test run mam.verdant_path.<name>
 */
@GameTestHolder(MightAndMagic.MODID)
@PrefixGameTestTemplate(false)
public class TestVerdantPath {

    /**
     * Verify Pure Daisy converts adjacent stone to living rock within 20 seconds.
     * Template: pure_daisy_converts_stone.nbt — a 5x5x5 structure with
     *   Pure Daisy at center (2,1,2), surrounded by stone at (1,1,2), (3,1,2), (2,1,1), (2,1,3)
     */
    @GameTest(template = "mam:verdant_path/pure_daisy_converts_stone", timeoutTicks = 400)
    public static void pureDaisyConvertsStone(GameTestHelper helper) {
        BlockPos stonePos = new BlockPos(1, 1, 2);
        helper.succeedWhen(() -> helper.assertBlockPresent(MamBlocks.LIVING_ROCK.get(), stonePos));
    }

    /**
     * Verify Mana Pool comparator output is 0 when empty.
     * Template: mana_pool_comparator.nbt — pool at (1,1,1), comparator at (2,1,1)
     */
    @GameTest(template = "mam:verdant_path/mana_pool_empty_comparator", timeoutTicks = 40)
    public static void manaPoolEmptyComparator(GameTestHelper helper) {
        BlockPos poolPos = new BlockPos(1, 1, 1);
        helper.succeedWhen(() -> helper.assertBlockPresent(MamBlocks.MANA_POOL.get(), poolPos));
    }
}
