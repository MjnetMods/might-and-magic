package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block_entity.flower.DaybloomBlockEntity;
import org.mjli.mam.block_entity.flower.EndoflameBlockEntity;
import org.mjli.mam.block_entity.flower.HydroangeasBlockEntity;
import org.mjli.mam.infrastructure.gametest.MamGameTestHelper;
import org.mjli.mam.verdant.VerdantGeneratingFlowers;

/**
 * Tests for the three mana-generating flowers.
 * No pool is placed — flowers buffer mana in their own BE when no pool is bound.
 */
@GameTestHolder(MightAndMagic.MODID)
@PrefixGameTestTemplate(false)
public class TestGeneratingFlowers {

    private static final String PLATFORM = "verdant_flowers/small_platform";
    private static final BlockPos CENTER = new BlockPos(3, 2, 3);

    // ── Daybloom ──────────────────────────────────────────────────────────────

    /** GF-1: Daybloom accumulates mana during daytime with an open sky. */
    @GameTest(template = PLATFORM, timeoutTicks = 40)
    public static void daybloomGeneratesManaInDaylight(GameTestHelper helper) {
        // Game test world starts at time 0 (daytime); don't call setDayTime — it's global and races other tests.
        helper.setBlock(CENTER, VerdantGeneratingFlowers.DAYBLOOM.get().defaultBlockState());

        helper.runAfterDelay(20, () -> {
            DaybloomBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, DaybloomBlockEntity.class);
            if (be.getCurrentMana() <= 0) {
                helper.fail("Daybloom should have generated mana in daylight, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }

    /** GF-2: Daybloom stops generating mana once the sky is blocked above it. */
    @GameTest(template = PLATFORM, timeoutTicks = 60)
    public static void daybloomNoManaWhenSkyBlocked(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantGeneratingFlowers.DAYBLOOM.get().defaultBlockState());

        // Let it generate for 5 ticks in open sky, then block the sky.
        // Sample baseline 2 ticks AFTER stone is placed (not before) to avoid a 1-tick
        // race between the game-test callback and the BE tick ordering within the same tick.
        helper.runAfterDelay(5, () -> {
            helper.setBlock(CENTER.above(), Blocks.STONE.defaultBlockState());

            helper.runAfterDelay(2, () -> {
                DaybloomBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, DaybloomBlockEntity.class);
                int manaSnapshot = be.getCurrentMana();

                helper.runAfterDelay(15, () -> {
                    int manaFinal = be.getCurrentMana();
                    if (manaFinal > manaSnapshot) {
                        helper.fail("Daybloom should stop generating with blocked sky, but mana increased from "
                                + manaSnapshot + " to " + manaFinal);
                    }
                    helper.succeed();
                });
            });
        });
    }

    // ── Endoflame ─────────────────────────────────────────────────────────────

    /** GF-3: Endoflame picks up a coal item entity and accumulates mana. */
    @GameTest(template = PLATFORM, timeoutTicks = 60)
    public static void endoflameBurnsCoalAndGeneratesMana(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantGeneratingFlowers.ENDOFLAME.get().defaultBlockState());

        Vec3 worldPos = Vec3.atCenterOf(helper.absolutePos(CENTER));
        ItemEntity coal = new ItemEntity(helper.getLevel(), worldPos.x, worldPos.y, worldPos.z, new ItemStack(Items.COAL));
        helper.getLevel().addFreshEntity(coal);

        helper.runAfterDelay(40, () -> {
            EndoflameBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, EndoflameBlockEntity.class);
            if (be.getCurrentMana() <= 0) {
                helper.fail("Endoflame should have generated mana from coal, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }

    /** GF-4: Endoflame does NOT consume non-fuel items. */
    @GameTest(template = PLATFORM, timeoutTicks = 40)
    public static void endoflameIgnoresNonFuelItems(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantGeneratingFlowers.ENDOFLAME.get().defaultBlockState());

        Vec3 worldPos = Vec3.atCenterOf(helper.absolutePos(CENTER));
        ItemEntity dirt = new ItemEntity(helper.getLevel(), worldPos.x, worldPos.y, worldPos.z, new ItemStack(Items.DIRT));
        helper.getLevel().addFreshEntity(dirt);

        helper.runAfterDelay(20, () -> {
            EndoflameBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, EndoflameBlockEntity.class);
            if (be.getCurrentMana() > 0) {
                helper.fail("Endoflame should ignore non-fuel dirt item, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }

    // ── Hydroangeas ───────────────────────────────────────────────────────────

    /** GF-5: Hydroangeas accumulates mana when adjacent to a waterlogged block (stable water source, no flow risk). */
    @GameTest(template = PLATFORM, timeoutTicks = 40)
    public static void hydroangeasGeneratesManaAdjacentToWater(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantGeneratingFlowers.HYDROANGEAS.get().defaultBlockState());
        // Waterlogged slab: stable water fluid, won't flow and destroy the flower
        helper.setBlock(CENTER.north(), Blocks.STONE_SLAB.defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, true));

        helper.runAfterDelay(20, () -> {
            HydroangeasBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, HydroangeasBlockEntity.class);
            if (be.getCurrentMana() <= 0) {
                helper.fail("Hydroangeas should have generated mana adjacent to waterlogged block, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }

    /** GF-6: Hydroangeas produces no mana in dry air with no adjacent water. */
    @GameTest(template = PLATFORM, timeoutTicks = 40)
    public static void hydroangeasNoManaWithoutWater(GameTestHelper helper) {
        // Ensure no rain (setRaining(false) or just rely on default dry game test world)
        helper.getLevel().setRainLevel(0f);
        helper.setBlock(CENTER, VerdantGeneratingFlowers.HYDROANGEAS.get().defaultBlockState());

        helper.runAfterDelay(20, () -> {
            HydroangeasBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, HydroangeasBlockEntity.class);
            if (be.getCurrentMana() > 0) {
                helper.fail("Hydroangeas should not generate mana in dry air, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }
}
