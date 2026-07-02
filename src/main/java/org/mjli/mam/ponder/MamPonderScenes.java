package org.mjli.mam.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.mjli.mam.block_entity.ApothecaryBlockEntity;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantGeneratingFlowers;
import org.mjli.mam.verdant.VerdantMana;
import org.mjli.mam.verdant.VerdantRock;
import org.mjli.mam.verdant.VerdantWood;

public class MamPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        var H = helper.<ItemProviderEntry<?, ?>>withKeyFunction(RegistryEntry::getId);

        H.forComponents(VerdantFlowers.PURE_DAISY)
            .addStoryBoard("pure_daisy/converts_stone", MamPonderScenes::pureDaisyStone)
            .addStoryBoard("pure_daisy/converts_log",   MamPonderScenes::pureDaisyLog);

        H.forComponents(VerdantGeneratingFlowers.DAYBLOOM)
            .addStoryBoard("daybloom/sunlight", MamPonderScenes::daybloomSunlight);

        H.forComponents(VerdantGeneratingFlowers.ENDOFLAME)
            .addStoryBoard("endoflame/burning_fuel", MamPonderScenes::endoflameBurningFuel);

        H.forComponents(VerdantGeneratingFlowers.HYDROANGEAS)
            .addStoryBoard("hydroangeas/rain_and_water", MamPonderScenes::hydroangeasRainAndWater);

        H.forComponents(VerdantMana.APOTHECARY, VerdantMana.INFUSED_APOTHECARY,
                VerdantMana.SACRED_APOTHECARY, VerdantMana.DESECRATED_APOTHECARY)
            .addStoryBoard("apothecary/brews_pure_daisy", MamPonderScenes::apothecaryBrewsPureDaisy);
    }

    public static void pureDaisyStone(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pure_daisy.converts_stone", "Transmuting Stone");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(10);

        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(50)
            .text("Place the Pure Daisy near any Stone…")
            .pointAt(util.vector().topOf(2, 1, 2))
            .attachKeyFrame();
        scene.idle(20);

        scene.world().showSection(util.select().fromTo(0, 1, 0, 4, 1, 4), Direction.DOWN);
        scene.idle(20);

        for (BlockPos pos : new BlockPos[]{
            new BlockPos(1, 1, 2), new BlockPos(3, 1, 2),
            new BlockPos(2, 1, 1), new BlockPos(2, 1, 3)
        }) {
            scene.world().setBlock(pos, VerdantRock.LIVING_ROCK.get().defaultBlockState(), true);
            scene.idle(8);
        }
        scene.idle(10);

        scene.overlay().showText(70)
            .text("…and it slowly transmutes nearby Stone into Living Rock.")
            .pointAt(util.vector().topOf(3, 1, 2))
            .attachKeyFrame();
        scene.idle(80);
    }

    public static void pureDaisyLog(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pure_daisy.converts_log", "Transmuting Wood");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(10);

        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(50)
            .text("Oak Logs placed nearby are transmuted too…")
            .pointAt(util.vector().topOf(2, 1, 2))
            .attachKeyFrame();
        scene.idle(20);

        scene.world().showSection(util.select().fromTo(0, 1, 0, 4, 1, 4), Direction.DOWN);
        scene.idle(20);

        for (BlockPos pos : new BlockPos[]{
            new BlockPos(1, 1, 2), new BlockPos(3, 1, 2),
            new BlockPos(2, 1, 1), new BlockPos(2, 1, 3)
        }) {
            scene.world().setBlock(pos, VerdantWood.LIVINGWOOD_LOG.get().defaultBlockState(), true);
            scene.idle(8);
        }
        scene.idle(10);

        scene.overlay().showText(70)
            .text("…becoming Livingwood Logs, the foundation of verdant crafting.")
            .pointAt(util.vector().topOf(3, 1, 2))
            .attachKeyFrame();
        scene.idle(80);
    }

    public static void daybloomSunlight(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("daybloom.sunlight", "Absorbing Sunlight");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(10);

        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(60)
            .text("The Daybloom passively absorbs sunlight during the day, as long as it can see the sky…")
            .pointAt(util.vector().topOf(2, 1, 2))
            .attachKeyFrame();
        scene.idle(30);

        scene.world().showSection(util.select().position(4, 1, 2), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(70)
            .text("…slowly filling any Mana Pool within 6 blocks.")
            .pointAt(util.vector().topOf(4, 1, 2))
            .attachKeyFrame();
        scene.idle(80);
    }

    public static void endoflameBurningFuel(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("endoflame.burning_fuel", "Burning Fuel");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(10);

        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(60)
            .text("Drop any furnace fuel within 3 blocks of the Endoflame…")
            .pointAt(util.vector().topOf(2, 1, 2))
            .attachKeyFrame();
        scene.idle(30);

        scene.world().showSection(util.select().position(4, 1, 2), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(70)
            .text("…and it burns the fuel, converting it into mana for a nearby pool.")
            .pointAt(util.vector().topOf(4, 1, 2))
            .attachKeyFrame();
        scene.idle(80);
    }

    public static void hydroangeasRainAndWater(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("hydroangeas.rain_and_water", "Rain and Water");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(10);

        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(60)
            .text("The Hydroangeas generates mana in the rain — or when placed adjacent to a water source.")
            .pointAt(util.vector().topOf(2, 1, 2))
            .attachKeyFrame();
        scene.idle(30);

        scene.world().showSection(util.select().position(1, 1, 2), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().position(4, 1, 2), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(70)
            .text("A single adjacent water source provides constant generation without needing rain.")
            .pointAt(util.vector().topOf(1, 1, 2))
            .attachKeyFrame();
        scene.idle(80);
    }

    // Mirrors TestApothecary.apothecaryCraftsPureDaisyFromPetalsAndSeed (PA-3) beat-for-beat:
    // fill with water, throw 4 white petals, throw a seed catalyst last, craft.
    // Mutates the real ApothecaryBlockEntity's fluid tank / ingredient list through its existing
    // public getters (getFluidTank(), getIngredients()) so the scene drives the actual production
    // renderer (ApothecaryBlockEntityRenderer) instead of inventing a parallel visual.
    public static void apothecaryBrewsPureDaisy(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("apothecary.brews_pure_daisy", "Brewing at the Apothecary");
        scene.configureBasePlate(0, 0, 5);
        BlockPos pos = new BlockPos(2, 1, 2);

        scene.showBasePlate();
        scene.idle(10);

        scene.world().showSection(util.select().position(pos), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(60)
            .text("Fill the Apothecary with a fluid, then throw in ingredients and a catalyst to craft.")
            .pointAt(util.vector().topOf(pos))
            .attachKeyFrame();
        scene.idle(30);

        ItemStack waterBucket = new ItemStack(Items.WATER_BUCKET);
        scene.overlay().showControls(util.vector().topOf(pos), Pointing.DOWN, 30).rightClick()
            .withItem(waterBucket);
        scene.idle(7);
        scene.world().modifyBlockEntity(pos, ApothecaryBlockEntity.class,
            be -> be.getFluidTank().fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE));
        scene.idle(10);

        scene.overlay().showText(50)
            .text("Right-click with a water bucket to fill the basin.")
            .pointAt(util.vector().topOf(pos))
            .attachKeyFrame();
        scene.idle(25);

        ItemStack whitePetal = new ItemStack(VerdantFlowers.PETALS.get(DyeColor.WHITE).get());
        scene.overlay().showControls(util.vector().topOf(pos), Pointing.DOWN, 30).withItem(whitePetal);
        scene.idle(7);

        for (int i = 0; i < 4; i++) {
            ElementLink<EntityElement> petal = scene.world().createItemEntity(
                util.vector().topOf(pos).add(0, 1.2, 0), util.vector().of(0, 0.1, 0), whitePetal);
            scene.idle(10);
            scene.world().modifyEntity(petal, Entity::discard);
            scene.world().modifyBlockEntity(pos, ApothecaryBlockEntity.class,
                be -> be.getIngredients().add(whitePetal.copy()));
            scene.idle(6);
        }

        scene.overlay().showText(60)
            .text("Throw four matching petals in — they'll float above the fluid until the recipe is ready.")
            .pointAt(util.vector().topOf(pos))
            .attachKeyFrame();
        scene.idle(30);

        ItemStack seed = new ItemStack(Items.WHEAT_SEEDS);
        scene.overlay().showControls(util.vector().topOf(pos), Pointing.DOWN, 30).withItem(seed);
        scene.idle(7);

        ElementLink<EntityElement> seedEntity = scene.world().createItemEntity(
            util.vector().topOf(pos).add(0, 1.2, 0), util.vector().of(0, 0.1, 0), seed);
        scene.idle(10);
        scene.world().modifyEntity(seedEntity, Entity::discard);

        // Ponder scenes have no live RecipeManager to match against, so the craft outcome
        // (fluid drain, ingredient clear, output eject) is staged directly — same end state
        // ApothecaryBlockEntity.collideEntityItem() reaches on a real match.
        scene.world().modifyBlockEntity(pos, ApothecaryBlockEntity.class, be -> {
            be.getFluidTank().drain(be.getFluidTank().getFluidAmount(), IFluidHandler.FluidAction.EXECUTE);
            be.getIngredients().clear();
        });
        scene.world().createItemEntity(util.vector().topOf(pos).add(0, 1.5, 0), util.vector().of(0, 0.1, 0),
            VerdantFlowers.PURE_DAISY.asStack());
        scene.effects().indicateSuccess(pos);
        scene.idle(10);

        scene.overlay().showText(80)
            .text("Throw the catalyst last — a seed — to trigger the craft. The fluid drains, the ingredients clear, and the Pure Daisy pops out.")
            .pointAt(util.vector().topOf(pos))
            .attachKeyFrame();
        scene.idle(90);
    }
}
