package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantRock;
import org.mjli.mam.verdant.VerdantWood;

/**
 * Loot-table and block-property tests for living rock, mushrooms, and living wood.
 * All tests share the same 7×5×7 dirt-floor platform used by TestVerdantFlowers.
 */
@GameTestHolder(MightAndMagic.MODID)
@PrefixGameTestTemplate(false)
public class TestVerdantBlocks {

    private static final String PLATFORM = "verdant_flowers/small_platform";
    private static final BlockPos CENTER = new BlockPos(3, 2, 3);

    // ── Living Rock ───────────────────────────────────────────────────────

    /** LT-1: living_rock loot table (dropSelf) is wired — breaking the block yields the item. */
    @GameTest(template = PLATFORM)
    public static void livingRockDropsSelf(GameTestHelper helper) {
        Block rock = VerdantRock.LIVING_ROCK.get();
        helper.setBlock(CENTER, rock.defaultBlockState());
        helper.getLevel().destroyBlock(helper.absolutePos(CENTER), true);
        helper.assertItemEntityPresent(rock.asItem(), CENTER, 2.0);
        helper.succeed();
    }

    /** LT-2: living_rock requires a stone pickaxe (T1) — bare hand and wood both fail, stone succeeds. */
    @GameTest(template = PLATFORM)
    public static void livingRockRequiresPickaxe(GameTestHelper helper) {
        var state = VerdantRock.LIVING_ROCK.get().defaultBlockState();
        var player = helper.makeMockPlayer(GameType.SURVIVAL);
        if (player.hasCorrectToolForDrops(state)) {
            helper.fail("living_rock should not be harvestable with empty hand");
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WOODEN_PICKAXE));
        if (player.hasCorrectToolForDrops(state)) {
            helper.fail("living_rock should not be harvestable with a wooden pickaxe (below T1)");
        }
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_PICKAXE));
        if (!player.hasCorrectToolForDrops(state)) {
            helper.fail("living_rock should be harvestable with a stone pickaxe (T1)");
        }
        helper.succeed();
    }

    // ── Mystical Mushroom ─────────────────────────────────────────────────

    /** LT-3: mystical_mushroom loot table (dropSelf) is wired — breaking yields the mushroom item. */
    @GameTest(template = PLATFORM)
    public static void mushroomDropsSelf(GameTestHelper helper) {
        Block mushroom = VerdantFlowers.MUSHROOMS.get(DyeColor.WHITE).get();
        helper.setBlock(CENTER, mushroom.defaultBlockState());
        helper.getLevel().destroyBlock(helper.absolutePos(CENTER), true);
        helper.assertItemEntityPresent(mushroom.asItem(), CENTER, 2.0);
        helper.succeed();
    }

    // ── Living Wood ───────────────────────────────────────────────────────

    /** LT-4: livingwood_log loot table (dropSelf) is wired — breaking yields the log item. */
    @GameTest(template = PLATFORM)
    public static void livingwoodLogDropsSelf(GameTestHelper helper) {
        Block log = VerdantWood.LIVINGWOOD_LOG.get();
        helper.setBlock(CENTER, log.defaultBlockState());
        helper.getLevel().destroyBlock(helper.absolutePos(CENTER), true);
        helper.assertItemEntityPresent(log.asItem(), CENTER, 2.0);
        helper.succeed();
    }
}
