package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block_entity.flower.DewpetalBlockEntity;
import org.mjli.mam.block_entity.flower.EmberwortBlockEntity;
import org.mjli.mam.block_entity.flower.SolarbudBlockEntity;
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

    // ── Solarbud ─────────────────────────────────────────────────────────────

    /** GF-1: Solarbud accumulates mana during daytime with an open sky. */
    @GameTest(template = PLATFORM, timeoutTicks = 40)
    public static void solarbudGeneratesManaInDaylight(GameTestHelper helper) {
        helper.getLevel().setDayTime(6000L);
        helper.setBlock(CENTER, VerdantGeneratingFlowers.SOLARBUD.get().defaultBlockState());

        helper.runAfterDelay(20, () -> {
            SolarbudBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, SolarbudBlockEntity.class);
            if (be.getCurrentMana() <= 0) {
                helper.fail("Solarbud should have generated mana in daylight, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }

    /** GF-2: Solarbud does NOT generate mana at night. */
    @GameTest(template = PLATFORM, timeoutTicks = 40)
    public static void solarbudNoManaAtNight(GameTestHelper helper) {
        helper.getLevel().setDayTime(18000L); // midnight
        helper.setBlock(CENTER, VerdantGeneratingFlowers.SOLARBUD.get().defaultBlockState());

        helper.runAfterDelay(20, () -> {
            SolarbudBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, SolarbudBlockEntity.class);
            if (be.getCurrentMana() > 0) {
                helper.fail("Solarbud should not generate mana at night, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }

    // ── Emberwort ────────────────────────────────────────────────────────────

    /** GF-3: Emberwort picks up a coal item entity and accumulates mana. */
    @GameTest(template = PLATFORM, timeoutTicks = 60)
    public static void emberwortBurnsCoalAndGeneratesMana(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantGeneratingFlowers.EMBERWORT.get().defaultBlockState());

        Vec3 worldPos = Vec3.atCenterOf(helper.absolutePos(CENTER));
        ItemEntity coal = new ItemEntity(helper.getLevel(), worldPos.x, worldPos.y, worldPos.z, new ItemStack(Items.COAL));
        helper.getLevel().addFreshEntity(coal);

        helper.runAfterDelay(40, () -> {
            EmberwortBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, EmberwortBlockEntity.class);
            if (be.getCurrentMana() <= 0) {
                helper.fail("Emberwort should have generated mana from coal, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }

    /** GF-4: Emberwort does NOT consume non-fuel items. */
    @GameTest(template = PLATFORM, timeoutTicks = 40)
    public static void emberwortIgnoresNonFuelItems(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantGeneratingFlowers.EMBERWORT.get().defaultBlockState());

        Vec3 worldPos = Vec3.atCenterOf(helper.absolutePos(CENTER));
        ItemEntity dirt = new ItemEntity(helper.getLevel(), worldPos.x, worldPos.y, worldPos.z, new ItemStack(Items.DIRT));
        helper.getLevel().addFreshEntity(dirt);

        helper.runAfterDelay(20, () -> {
            EmberwortBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, EmberwortBlockEntity.class);
            if (be.getCurrentMana() > 0) {
                helper.fail("Emberwort should ignore non-fuel dirt item, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }

    // ── Dewpetal ─────────────────────────────────────────────────────────────

    /** GF-5: Dewpetal accumulates mana when a water source is adjacent. */
    @GameTest(template = PLATFORM, timeoutTicks = 40)
    public static void dewpetalGeneratesManaAdjacentToWater(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantGeneratingFlowers.DEWPETAL.get().defaultBlockState());
        helper.setBlock(CENTER.north(), Blocks.WATER.defaultBlockState());

        helper.runAfterDelay(20, () -> {
            DewpetalBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, DewpetalBlockEntity.class);
            if (be.getCurrentMana() <= 0) {
                helper.fail("Dewpetal should have generated mana adjacent to water, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }

    /** GF-6: Dewpetal produces no mana in dry air with no adjacent water. */
    @GameTest(template = PLATFORM, timeoutTicks = 40)
    public static void dewpetalNoManaWithoutWater(GameTestHelper helper) {
        // Ensure no rain (setRaining(false) or just rely on default dry game test world)
        helper.getLevel().setRainLevel(0f);
        helper.setBlock(CENTER, VerdantGeneratingFlowers.DEWPETAL.get().defaultBlockState());

        helper.runAfterDelay(20, () -> {
            DewpetalBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, DewpetalBlockEntity.class);
            if (be.getCurrentMana() > 0) {
                helper.fail("Dewpetal should not generate mana in dry air, but getCurrentMana() == " + be.getCurrentMana());
            }
            helper.succeed();
        });
    }
}
