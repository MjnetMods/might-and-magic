package org.mjli.mam.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import org.mjli.mam.verdant.VerdantFlowers;
import org.mjli.mam.verdant.VerdantRock;
import org.mjli.mam.verdant.VerdantWood;

public class MamPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        var H = helper.<ItemProviderEntry<?, ?>>withKeyFunction(RegistryEntry::getId);

        H.forComponents(VerdantFlowers.PURE_DAISY)
            .addStoryBoard("pure_daisy/converts_stone", MamPonderScenes::pureDaisyStone)
            .addStoryBoard("pure_daisy/converts_log",   MamPonderScenes::pureDaisyLog);
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
}
