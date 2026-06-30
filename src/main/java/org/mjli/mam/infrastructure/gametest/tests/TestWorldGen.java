package org.mjli.mam.infrastructure.gametest.tests;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block.MysticalMushroomBlock;
import org.mjli.mam.block.flower.MysticalFlowerBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * Worldgen smoke tests — verifies feature JSON is loaded and actually places blocks.
 * Shared structure: worldgen/large_platform (40×5×40 dirt floor).
 * Center of platform in helper coords: (19, 2, 19) — 19-block margin on all sides,
 * enough to contain xz_spread=16 (mushrooms) entirely within the template.
 */
@GameTestHolder(MightAndMagic.MODID)
@PrefixGameTestTemplate(false)
public class TestWorldGen {

    private static final String LARGE_PLATFORM = "worldgen/large_platform";
    // First air block above the dirt floor at the center of the 40×40 platform.
    private static final BlockPos CENTER = new BlockPos(19, 2, 19);

    // ── WG-1: registry presence ───────────────────────────────────────────

    /**
     * WG-1: all worldgen features resolve in their respective registries.
     * Color patches → PLACED_FEATURE; mystical_flowers + mystical_mushrooms → CONFIGURED_FEATURE.
     */
    @GameTest(template = LARGE_PLATFORM)
    public static void allWorldgenFeaturesRegistered(GameTestHelper helper) {
        Registry<PlacedFeature> pfRegistry = helper.getLevel().registryAccess()
                .registryOrThrow(Registries.PLACED_FEATURE);
        Registry<ConfiguredFeature<?, ?>> cfRegistry = helper.getLevel().registryAccess()
                .registryOrThrow(Registries.CONFIGURED_FEATURE);

        List<String> missing = new ArrayList<>();

        for (DyeColor color : DyeColor.values()) {
            String id = color.getSerializedName() + "_mystical_flower_patch";
            if (pfRegistry.getHolder(ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, id)).isEmpty()) {
                missing.add("placed_feature:mam:" + id);
            }
        }

        for (String id : new String[]{"mystical_flowers", "mystical_mushrooms"}) {
            if (cfRegistry.getHolder(ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, id)).isEmpty()) {
                missing.add("configured_feature:mam:" + id);
            }
        }

        if (!missing.isEmpty()) {
            helper.fail("Missing features: " + String.join(", ", missing));
            return;
        }
        helper.succeed();
    }

    // ── WG-2: flower placement ────────────────────────────────────────────

    /**
     * WG-2: invoking white_mystical_flower_patch (a PlacedFeature) places at least one MysticalFlowerBlock.
     * Empty placement list means place() delegates directly to the inner random_patch configured_feature.
     */
    @GameTest(template = LARGE_PLATFORM)
    public static void mysticalFlowerPatchPlacesBlock(GameTestHelper helper) {
        Registry<PlacedFeature> registry = helper.getLevel().registryAccess()
                .registryOrThrow(Registries.PLACED_FEATURE);

        var holder = registry.getHolder(
                ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, "white_mystical_flower_patch"));
        if (holder.isEmpty()) {
            helper.fail("mam:white_mystical_flower_patch not registered as placed_feature");
            return;
        }

        BlockPos origin = helper.absolutePos(CENTER);
        PlacedFeature feature = holder.get().value();
        for (int i = 0; i < 5; i++) {
            feature.place(helper.getLevel(),
                    helper.getLevel().getChunkSource().getGenerator(),
                    helper.getLevel().random, origin);
        }

        for (int x = 0; x < 40; x++) {
            for (int z = 0; z < 40; z++) {
                if (helper.getBlockState(new BlockPos(x, 2, z)).getBlock() instanceof MysticalFlowerBlock) {
                    helper.succeed();
                    return;
                }
            }
        }
        helper.fail("mam:white_mystical_flower_patch placed no blocks after 5 invocations");
    }

    // ── WG-3: mushroom placement ──────────────────────────────────────────

    /**
     * WG-3: invoking mystical_mushrooms configured_feature places at least one MysticalMushroomBlock.
     * Uses all 16 colors via weighted_state_provider. 5 invocations with tries=40 on a 40×40 dirt floor.
     */
    @GameTest(template = LARGE_PLATFORM)
    public static void mysticalMushroomFeaturePlacesBlock(GameTestHelper helper) {
        Registry<ConfiguredFeature<?, ?>> registry = helper.getLevel().registryAccess()
                .registryOrThrow(Registries.CONFIGURED_FEATURE);

        var holder = registry.getHolder(
                ResourceLocation.fromNamespaceAndPath(MightAndMagic.MODID, "mystical_mushrooms"));
        if (holder.isEmpty()) {
            helper.fail("mam:mystical_mushrooms not registered as configured_feature");
            return;
        }

        BlockPos origin = helper.absolutePos(CENTER);
        ConfiguredFeature<?, ?> feature = holder.get().value();
        for (int i = 0; i < 5; i++) {
            feature.place(helper.getLevel(),
                    helper.getLevel().getChunkSource().getGenerator(),
                    helper.getLevel().random, origin);
        }

        for (int x = 0; x < 40; x++) {
            for (int z = 0; z < 40; z++) {
                if (helper.getBlockState(new BlockPos(x, 2, z)).getBlock() instanceof MysticalMushroomBlock) {
                    helper.succeed();
                    return;
                }
            }
        }
        helper.fail("mam:mystical_mushrooms placed no blocks after 5 invocations");
    }
}
