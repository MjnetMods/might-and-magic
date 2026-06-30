package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.api.internal.ManaNetworkHandler;
import org.mjli.mam.api.mana.ManaEnergyType;
import org.mjli.mam.api.mana.ManaPool;
import org.mjli.mam.block_entity.mana.ManaPoolBlockEntity;
import org.mjli.mam.infrastructure.gametest.MamGameTestHelper;
import org.mjli.mam.verdant.VerdantMana;

import java.util.List;
import java.util.Set;

@GameTestHolder(MightAndMagic.MODID)
@PrefixGameTestTemplate(false)
public class TestManaPool {

    private static final String PLATFORM = "verdant_flowers/small_platform";
    private static final BlockPos CENTER = new BlockPos(3, 2, 3);

    /** MP-1: getAnalogOutputSignal returns 7 when pool holds 500k / 1M mana. */
    @GameTest(template = PLATFORM, timeoutTicks = 20)
    public static void manaPoolComparatorSignalInGame(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantMana.MANA_POOL.get().defaultBlockState());
        helper.runAfterDelay(2, () -> {
            ManaPoolBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, ManaPoolBlockEntity.class);
            be.receiveMana(500_000);
            int signal = helper.getBlockState(CENTER).getAnalogOutputSignal(helper.getLevel(), helper.absolutePos(CENTER));
            if (signal != 7) helper.fail("Expected comparator signal 7 at half-mana, got " + signal);
            else helper.succeed();
        });
    }

    /** MP-2: ManaPoolBlockEntity registers in ManaNetworkHandler after first server tick. */
    @GameTest(template = PLATFORM, timeoutTicks = 20)
    public static void manaPoolRegistersOnPlace(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantMana.MANA_POOL.get().defaultBlockState());
        helper.runAfterDelay(2, () -> {
            ManaPoolBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, ManaPoolBlockEntity.class);
            Set<ManaPool> pools = ManaNetworkHandler.INSTANCE.getAllPoolsInWorld(helper.getLevel());
            if (!pools.contains(be)) helper.fail("ManaPool not registered in ManaNetworkHandler after first tick");
            else helper.succeed();
        });
    }

    /** MP-3: removing the pool block removes it from ManaNetworkHandler (setRemoved fires synchronously). */
    @GameTest(template = PLATFORM, timeoutTicks = 20)
    public static void manaPoolDeregistersOnRemove(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantMana.MANA_POOL.get().defaultBlockState());
        helper.runAfterDelay(2, () -> {
            ManaPoolBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, ManaPoolBlockEntity.class);
            helper.setBlock(CENTER, Blocks.AIR.defaultBlockState());
            if (ManaNetworkHandler.INSTANCE.getAllPoolsInWorld(helper.getLevel()).contains(be)) {
                helper.fail("ManaPool still in ManaNetworkHandler after block removal");
            } else {
                helper.succeed();
            }
        });
    }

    /** MP-4: each pool tier reports the correct max capacity (1M/4M/16M/16M). */
    @GameTest(template = PLATFORM)
    public static void poolTierCapacityIsCorrect(GameTestHelper helper) {
        record Check(Block block, int expectedMax, String label) {}
        for (var c : List.of(
            new Check(VerdantMana.MANA_POOL.get(), ManaPoolBlockEntity.MAX_CAPACITY_TIER_1, "mana_pool"),
            new Check(VerdantMana.INFUSED_MANA_POOL.get(), ManaPoolBlockEntity.MAX_CAPACITY_TIER_2, "infused_mana_pool"),
            new Check(VerdantMana.SACRED_MANA_POOL.get(), ManaPoolBlockEntity.MAX_CAPACITY_TIER_3, "sacred_mana_pool"),
            new Check(VerdantMana.DESECRATED_MANA_POOL.get(), ManaPoolBlockEntity.MAX_CAPACITY_TIER_3, "desecrated_mana_pool")
        )) {
            helper.setBlock(CENTER, c.block().defaultBlockState());
            ManaPoolBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, ManaPoolBlockEntity.class);
            if (be.getMaxMana() != c.expectedMax()) {
                helper.fail(c.label() + ": expected max " + c.expectedMax() + ", got " + be.getMaxMana());
                return;
            }
        }
        helper.succeed();
    }

    /** MP-5: pool tiers report the correct energy type (MANA for mana_pool/infused/sacred, NOX for desecrated). */
    @GameTest(template = PLATFORM)
    public static void poolTierEnergyTypeIsCorrect(GameTestHelper helper) {
        record Check(Block block, ManaEnergyType expected, String label) {}
        for (var c : List.of(
            new Check(VerdantMana.MANA_POOL.get(), ManaEnergyType.MANA, "mana_pool"),
            new Check(VerdantMana.INFUSED_MANA_POOL.get(), ManaEnergyType.MANA, "infused_mana_pool"),
            new Check(VerdantMana.SACRED_MANA_POOL.get(), ManaEnergyType.MANA, "sacred_mana_pool"),
            new Check(VerdantMana.DESECRATED_MANA_POOL.get(), ManaEnergyType.NOX, "desecrated_mana_pool")
        )) {
            helper.setBlock(CENTER, c.block().defaultBlockState());
            ManaPoolBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, ManaPoolBlockEntity.class);
            if (be.getEnergyType() != c.expected()) {
                helper.fail(c.label() + ": expected " + c.expected() + ", got " + be.getEnergyType());
                return;
            }
        }
        helper.succeed();
    }

    /** MP-6: Nox entering a Mana Pool (T1) taints all stored Mana to Nox at 1:1, instant. */
    @GameTest(template = PLATFORM, timeoutTicks = 20)
    public static void manaPoolTaintsToNoxOnContact(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantMana.MANA_POOL.get().defaultBlockState());
        helper.runAfterDelay(2, () -> {
            ManaPoolBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, ManaPoolBlockEntity.class);
            be.receiveMana(500, ManaEnergyType.MANA);
            be.receiveMana(500, ManaEnergyType.NOX);
            if (be.getCurrentMana() != 1000 || be.getEnergyType() != ManaEnergyType.NOX) {
                helper.fail("Expected 1000 Nox after taint, got " + be.getCurrentMana() + " " + be.getEnergyType());
            } else {
                helper.succeed();
            }
        });
    }

    /** MP-7: Nox entering an Infused Mana Pool (T2) taints all stored Mana to Nox at 1:1, instant. */
    @GameTest(template = PLATFORM, timeoutTicks = 20)
    public static void infusedManaPoolTaintsToNoxOnContact(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantMana.INFUSED_MANA_POOL.get().defaultBlockState());
        helper.runAfterDelay(2, () -> {
            ManaPoolBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, ManaPoolBlockEntity.class);
            be.receiveMana(500, ManaEnergyType.MANA);
            be.receiveMana(500, ManaEnergyType.NOX);
            if (be.getCurrentMana() != 1000 || be.getEnergyType() != ManaEnergyType.NOX) {
                helper.fail("Expected 1000 Nox after taint, got " + be.getCurrentMana() + " " + be.getEnergyType());
            } else {
                helper.succeed();
            }
        });
    }

    /** MP-8: Nox entering a Sacred Mana Pool (T3, aligned) is rejected — equal Mana is destroyed, no Nox stored. */
    @GameTest(template = PLATFORM, timeoutTicks = 20)
    public static void sacredManaPoolRejectsNox(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantMana.SACRED_MANA_POOL.get().defaultBlockState());
        helper.runAfterDelay(2, () -> {
            ManaPoolBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, ManaPoolBlockEntity.class);
            be.receiveMana(500, ManaEnergyType.MANA);
            be.receiveMana(500, ManaEnergyType.NOX);
            if (be.getCurrentMana() != 0 || be.getEnergyType() != ManaEnergyType.MANA) {
                helper.fail("Expected 0 Mana after rejection, got " + be.getCurrentMana() + " " + be.getEnergyType());
            } else {
                helper.succeed();
            }
        });
    }

    /** MP-9: Mana entering a Desecrated Mana Pool (T3, aligned) is rejected — equal Nox is destroyed, no Mana stored. */
    @GameTest(template = PLATFORM, timeoutTicks = 20)
    public static void desecratedManaPoolRejectsMana(GameTestHelper helper) {
        helper.setBlock(CENTER, VerdantMana.DESECRATED_MANA_POOL.get().defaultBlockState());
        helper.runAfterDelay(2, () -> {
            ManaPoolBlockEntity be = MamGameTestHelper.getBlockEntity(helper, CENTER, ManaPoolBlockEntity.class);
            be.receiveMana(500, ManaEnergyType.NOX);
            be.receiveMana(500, ManaEnergyType.MANA);
            if (be.getCurrentMana() != 0 || be.getEnergyType() != ManaEnergyType.NOX) {
                helper.fail("Expected 0 Nox after rejection, got " + be.getCurrentMana() + " " + be.getEnergyType());
            } else {
                helper.succeed();
            }
        });
    }
}
