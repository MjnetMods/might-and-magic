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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
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
        FluidTank seedTank = new FluidTank(1000);
        seedTank.setFluid(new FluidStack(Fluids.WATER, 1000));
        stateTag.put("fluid", seedTank.writeToNBT(registries, new CompoundTag()));
        CompoundTag ingredientsTag = new CompoundTag();
        ItemStack petal = new ItemStack(VerdantFlowers.PETALS.get(DyeColor.WHITE).get());
        ingredientsTag.put("0", petal.save(registries));
        stateTag.put("ingredients", ingredientsTag);

        ApothecaryBlockEntity beA = (ApothecaryBlockEntity)
                helper.getLevel().getBlockEntity(helper.absolutePos(posA));
        ApothecaryBlockEntity beB = (ApothecaryBlockEntity)
                helper.getLevel().getBlockEntity(helper.absolutePos(posB));
        if (beA == null || beB == null) { helper.fail("Missing BE"); return; }

        beA.loadCustomOnly(stateTag, registries);
        CompoundTag saved = beA.saveCustomOnly(registries);
        beB.loadCustomOnly(saved, registries);

        if (beB.getFluidTank().isEmpty() || beB.getFluidTank().getFluid().getFluid() != Fluids.WATER) {
            helper.fail("Expected WATER fluid, got " + beB.getFluidTank().getFluid());
        }
        if (beB.getIngredients().size() != 1) {
            helper.fail("Expected 1 petal, got " + beB.getIngredients().size());
        }
        if (!ItemStack.isSameItemSameComponents(beB.getIngredients().get(0), petal)) {
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
        if (be.getFluidTank().isEmpty() || be.getFluidTank().getFluid().getFluid() != Fluids.WATER) {
            helper.fail("Expected WATER after bucket fill, got " + be.getFluidTank().getFluid());
            return;
        }

        double x = absCenter.getX() + 0.5, y = absCenter.getY() + 1.0, z = absCenter.getZ() + 0.5;
        for (int i = 0; i < 4; i++) {
            helper.getLevel().addFreshEntity(new ItemEntity(helper.getLevel(), x, y, z,
                    new ItemStack(VerdantFlowers.PETALS.get(DyeColor.WHITE).get())));
        }

        helper.runAfterDelay(2, () -> {
            if (be.getIngredients().size() != 4) {
                helper.fail("Expected 4 petals ingested, got " + be.getIngredients().size());
                return;
            }

            helper.getLevel().addFreshEntity(new ItemEntity(helper.getLevel(), x, y, z,
                    new ItemStack(Items.WHEAT_SEEDS)));

            helper.runAfterDelay(2, () -> {
                if (!be.getFluidTank().isEmpty()) {
                    helper.fail("Expected fluid drained after craft, got " + be.getFluidTank().getFluid());
                }
                if (!be.getIngredients().isEmpty()) {
                    helper.fail("Expected petals cleared after craft, got " + be.getIngredients().size());
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

    // ── PA-4 ─────────────────────────────────────────────────────────────────

    /**
     * PA-4: throwing a white then a red petal, then interacting empty-handed, retracts only
     * the most recently thrown (red) petal back into the player's inventory — LIFO, one per click.
     */
    @GameTest(template = PLATFORM)
    public static void apothecaryRetractsLastIngredientEmptyHanded(GameTestHelper helper) {
        BlockState state = VerdantMana.APOTHECARY.get().defaultBlockState();
        helper.setBlock(CENTER, state);
        BlockPos absCenter = helper.absolutePos(CENTER);
        ApothecaryBlockEntity be = (ApothecaryBlockEntity) helper.getLevel().getBlockEntity(absCenter);
        if (be == null) { helper.fail("No BlockEntity at CENTER"); return; }

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
        be.interact(player);
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

        double x = absCenter.getX() + 0.5, y = absCenter.getY() + 1.0, z = absCenter.getZ() + 0.5;
        helper.getLevel().addFreshEntity(new ItemEntity(helper.getLevel(), x, y, z,
                new ItemStack(VerdantFlowers.PETALS.get(DyeColor.WHITE).get())));

        helper.runAfterDelay(2, () -> {
            if (be.getIngredients().size() != 1) {
                helper.fail("Expected 1 ingredient after first throw, got " + be.getIngredients().size());
                return;
            }

            helper.getLevel().addFreshEntity(new ItemEntity(helper.getLevel(), x, y, z,
                    new ItemStack(VerdantFlowers.PETALS.get(DyeColor.RED).get())));

            helper.runAfterDelay(2, () -> {
                if (be.getIngredients().size() != 2) {
                    helper.fail("Expected 2 ingredients after second throw, got " + be.getIngredients().size());
                    return;
                }

                var result = be.interact(player);
                if (result != net.minecraft.world.InteractionResult.SUCCESS) {
                    helper.fail("Expected SUCCESS retracting an ingredient, got " + result);
                    return;
                }
                if (be.getIngredients().size() != 1) {
                    helper.fail("Expected 1 ingredient left after retract, got " + be.getIngredients().size());
                    return;
                }
                if (!be.getIngredients().get(0).is(VerdantFlowers.PETALS.get(DyeColor.WHITE).get())) {
                    helper.fail("Expected the white petal (thrown first) to remain — LIFO retract took the wrong one");
                    return;
                }

                boolean retractedInInventory = false;
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    if (player.getInventory().getItem(i).is(VerdantFlowers.PETALS.get(DyeColor.RED).get())) {
                        retractedInInventory = true;
                        break;
                    }
                }
                if (!retractedInInventory) {
                    helper.fail("Expected retracted red petal in player inventory");
                    return;
                }
                helper.succeed();
            });
        });
    }

    // ── PA-5 ─────────────────────────────────────────────────────────────────

    /**
     * PA-5: after a successful craft, refilling the fluid and interacting empty-handed within
     * the recraft window re-pulls the same ingredients from the player's inventory.
     */
    @GameTest(template = PLATFORM)
    public static void apothecaryRecraftsLastRecipeFromPlayerInventory(GameTestHelper helper) {
        BlockState state = VerdantMana.APOTHECARY.get().defaultBlockState();
        helper.setBlock(CENTER, state);
        BlockPos absCenter = helper.absolutePos(CENTER);
        ApothecaryBlockEntity be = (ApothecaryBlockEntity) helper.getLevel().getBlockEntity(absCenter);
        if (be == null) { helper.fail("No BlockEntity at CENTER"); return; }

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
        be.interact(player);

        double x = absCenter.getX() + 0.5, y = absCenter.getY() + 1.0, z = absCenter.getZ() + 0.5;
        for (int i = 0; i < 4; i++) {
            helper.getLevel().addFreshEntity(new ItemEntity(helper.getLevel(), x, y, z,
                    new ItemStack(VerdantFlowers.PETALS.get(DyeColor.WHITE).get())));
        }

        helper.runAfterDelay(2, () -> {
            if (be.getIngredients().size() != 4) {
                helper.fail("Expected 4 petals ingested, got " + be.getIngredients().size());
                return;
            }

            helper.getLevel().addFreshEntity(new ItemEntity(helper.getLevel(), x, y, z,
                    new ItemStack(Items.WHEAT_SEEDS)));

            helper.runAfterDelay(2, () -> {
                if (!be.getIngredients().isEmpty() || !be.getFluidTank().isEmpty()) {
                    helper.fail("Expected craft to clear ingredients and drain fluid before recraft check");
                    return;
                }

                player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
                be.interact(player);
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                if (be.getFluidTank().isEmpty()) {
                    helper.fail("Expected fluid refilled before recraft attempt");
                    return;
                }

                // Slot 1, not 0 — slot 0 is the player's selected hotbar slot, i.e. their main
                // hand; putting the petals there would defeat the "main hand empty" check below.
                player.getInventory().setItem(1, new ItemStack(VerdantFlowers.PETALS.get(DyeColor.WHITE).get(), 4));

                var result = be.interact(player);
                if (result != net.minecraft.world.InteractionResult.SUCCESS) {
                    helper.fail("Expected SUCCESS recrafting last recipe, got " + result);
                    return;
                }
                if (be.getIngredients().size() != 4) {
                    helper.fail("Expected 4 ingredients re-added from recraft, got " + be.getIngredients().size());
                    return;
                }
                if (!player.getInventory().getItem(1).isEmpty()) {
                    helper.fail("Expected player's 4 petals consumed by recraft, got "
                            + player.getInventory().getItem(1).getCount());
                    return;
                }
                helper.succeed();
            });
        });
    }

    // ── PA-6 ─────────────────────────────────────────────────────────────────

    /**
     * PA-6: an Infused Apothecary (T2) accepts up to 6 ingredients, not T1's 4 — regression
     * guard for the per-tier capacity fix (design/magic/10_apothecary.md tier table: T1 = 4,
     * T2 = 6). A 7th item must be left uningested once the T2 cap is reached.
     */
    @GameTest(template = PLATFORM)
    public static void apothecaryTier2AcceptsSixIngredients(GameTestHelper helper) {
        BlockState state = VerdantMana.INFUSED_APOTHECARY.get().defaultBlockState();
        helper.setBlock(CENTER, state);
        BlockPos absCenter = helper.absolutePos(CENTER);
        ApothecaryBlockEntity be = (ApothecaryBlockEntity) helper.getLevel().getBlockEntity(absCenter);
        if (be == null) { helper.fail("No BlockEntity at CENTER"); return; }

        // collideEntityItem() rejects every ingredient outright while the tank is empty
        // (see ApothecaryBlockEntity), so the basin must be filled before capacity can be tested.
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.WATER_BUCKET));
        be.interact(player);

        double x = absCenter.getX() + 0.5, y = absCenter.getY() + 1.0, z = absCenter.getZ() + 0.5;
        for (int i = 0; i < 7; i++) {
            helper.getLevel().addFreshEntity(new ItemEntity(helper.getLevel(), x, y, z, new ItemStack(Items.STICK)));
        }

        helper.runAfterDelay(2, () -> {
            if (be.getIngredients().size() != 6) {
                helper.fail("Expected 6 ingredients accepted (T2 cap), got " + be.getIngredients().size());
                return;
            }
            boolean leftoverStick = helper.getLevel()
                    .getEntitiesOfClass(ItemEntity.class, new AABB(absCenter.above()))
                    .stream()
                    .anyMatch(e -> e.getItem().is(Items.STICK));
            if (!leftoverStick) {
                helper.fail("Expected the 7th stick to remain uningested once the T2 cap (6) was reached");
                return;
            }
            helper.succeed();
        });
    }
}
