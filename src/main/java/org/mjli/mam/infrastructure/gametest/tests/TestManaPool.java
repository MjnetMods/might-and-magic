package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.api.internal.ManaNetworkHandler;
import org.mjli.mam.api.mana.ManaPool;
import org.mjli.mam.block_entity.mana.ManaPoolBlockEntity;
import org.mjli.mam.infrastructure.gametest.MamGameTestHelper;
import org.mjli.mam.verdant.VerdantMana;

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
}
