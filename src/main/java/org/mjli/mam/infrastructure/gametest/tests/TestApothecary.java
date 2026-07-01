package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block_entity.ApothecaryBlockEntity;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantMana;

@GameTestHolder(MightAndMagic.MODID)
@PrefixGameTestTemplate(false)
public class TestApothecary {

    private static final String PLATFORM = "verdant_flowers/small_platform";
    private static final BlockPos CENTER = new BlockPos(3, 2, 3);

    // ── PA-1 ─────────────────────────────────────────────────────────────────

    /** PA-1: interact() with empty hand returns PASS (does not consume the interaction). */
    @GameTest(template = PLATFORM)
    public static void apothecaryInteractEmptyHandPasses(GameTestHelper helper) {
        BlockState state = VerdantMana.APOTHECARY.get().defaultBlockState();
        helper.setBlock(CENTER, state);
        ApothecaryBlockEntity be = (ApothecaryBlockEntity)
                helper.getLevel().getBlockEntity(helper.absolutePos(CENTER));
        if (be == null) { helper.fail("No BlockEntity at CENTER"); return; }

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        // player.getMainHandItem() returns ItemStack.EMPTY by default

        var result = be.interact(player);
        if (result != net.minecraft.world.InteractionResult.PASS) {
            helper.fail("Expected PASS with empty hand, got " + result);
        }
        helper.succeed();
    }

    // ── PA-2 ─────────────────────────────────────────────────────────────────

    /**
     * PA-2: fluid state and petal list survive a saveCustomOnly / loadCustomOnly round-trip.
     * Uses two apothecary BEs: load prepared state into A, save, load into B, assert.
     */
    @GameTest(template = PLATFORM)
    public static void apothecaryNbtRoundtrip(GameTestHelper helper) {
        BlockPos posA = CENTER;
        BlockPos posB = CENTER.east(2);
        BlockState state = VerdantMana.APOTHECARY.get().defaultBlockState();
        helper.setBlock(posA, state);
        helper.setBlock(posB, state);

        HolderLookup.Provider registries = helper.getLevel().registryAccess();

        // Build state tag matching saveAdditional layout
        CompoundTag stateTag = new CompoundTag();
        stateTag.putByte("fluid", (byte) ApothecaryBlockEntity.FluidState.WATER.ordinal());
        CompoundTag petalsTag = new CompoundTag();
        ItemStack petal = new ItemStack(VerdantFlowers.PETALS.get(DyeColor.WHITE).get());
        petalsTag.put("0", petal.save(registries));
        stateTag.put("petals", petalsTag);

        ApothecaryBlockEntity beA = (ApothecaryBlockEntity)
                helper.getLevel().getBlockEntity(helper.absolutePos(posA));
        ApothecaryBlockEntity beB = (ApothecaryBlockEntity)
                helper.getLevel().getBlockEntity(helper.absolutePos(posB));
        if (beA == null || beB == null) { helper.fail("Missing BE"); return; }

        beA.loadCustomOnly(stateTag, registries);
        CompoundTag saved = beA.saveCustomOnly(registries);
        beB.loadCustomOnly(saved, registries);

        if (beB.getFluidState() != ApothecaryBlockEntity.FluidState.WATER) {
            helper.fail("Expected WATER fluid, got " + beB.getFluidState());
        }
        if (beB.getPetals().size() != 1) {
            helper.fail("Expected 1 petal, got " + beB.getPetals().size());
        }
        if (!ItemStack.isSameItemSameComponents(beB.getPetals().get(0), petal)) {
            helper.fail("Petal item did not survive round-trip");
        }
        helper.succeed();
    }

    // ── PA-3 ─────────────────────────────────────────────────────────────────

    /**
     * PA-3: filling with water, throwing 4 white petals, then throwing a seed reagent
     * crafts a Pure Daisy, clears the ingredient list, and drains the fluid.
     */
    @GameTest(template = PLATFORM)
    public static void apothecaryCraftsPureDaisyFromPetalsAndSeed(GameTestHelper helper) {
        BlockState state = VerdantMana.APOTHECARY.get().defaultBlockState();
        helper.setBlock(CENTER, state);
        BlockPos absCenter = helper.absolutePos(CENTER);
        ApothecaryBlockEntity be = (ApothecaryBlockEntity) helper.getLevel().getBlockEntity(absCenter);
        if (be == null) { helper.fail("No BlockEntity at CENTER"); return; }

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
        be.interact(player);
        if (be.getFluidState() != ApothecaryBlockEntity.FluidState.WATER) {
            helper.fail("Expected WATER after bucket fill, got " + be.getFluidState());
            return;
        }

        double x = absCenter.getX() + 0.5, y = absCenter.getY() + 1.0, z = absCenter.getZ() + 0.5;
        for (int i = 0; i < 4; i++) {
            helper.getLevel().addFreshEntity(new ItemEntity(helper.getLevel(), x, y, z,
                    new ItemStack(VerdantFlowers.PETALS.get(DyeColor.WHITE).get())));
        }

        helper.runAfterDelay(2, () -> {
            if (be.getPetals().size() != 4) {
                helper.fail("Expected 4 petals ingested, got " + be.getPetals().size());
                return;
            }

            helper.getLevel().addFreshEntity(new ItemEntity(helper.getLevel(), x, y, z,
                    new ItemStack(Items.WHEAT_SEEDS)));

            helper.runAfterDelay(2, () -> {
                if (be.getFluidState() != ApothecaryBlockEntity.FluidState.EMPTY) {
                    helper.fail("Expected fluid drained after craft, got " + be.getFluidState());
                }
                if (!be.getPetals().isEmpty()) {
                    helper.fail("Expected petals cleared after craft, got " + be.getPetals().size());
                }
                boolean foundOutput = helper.getLevel()
                        .getEntitiesOfClass(ItemEntity.class, new AABB(absCenter.above()))
                        .stream()
                        .anyMatch(e -> e.getItem().is(VerdantFlowers.PURE_DAISY.get().asItem()));
                if (!foundOutput) helper.fail("Expected Pure Daisy output item entity above the apothecary");
                helper.succeed();
            });
        });
    }
}
