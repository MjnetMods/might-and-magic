package org.mjli.mam.infrastructure.datagen;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.structures.SnbtToNbt;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.mjli.mam.MightAndMagic;

@EventBusSubscriber(modid = MightAndMagic.MODID)
public class MamDataGen {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
      PackOutput packOutput = generator.getPackOutput();

// 1. Grab the output folder (usually something like build/resources/main or src/generated/resources)
      Path outputFolder = packOutput.getOutputFolder();

      // 2. Safely find the root project workspace folder by searching upward for build.gradle
      Path projectRoot = outputFolder;
      while (projectRoot != null && !Files.exists(projectRoot.resolve("build.gradle"))) {
        projectRoot = projectRoot.getParent();
      }

      // Fallback to current directory if build.gradle isn't found
      if (projectRoot == null) {
        projectRoot = Path.of(".");
      }

      // 3. Pin down the absolute path to your custom gametest files
      Path snbtSourceFolder = projectRoot.resolve("src").resolve("gametest").resolve("structure");

      // Ponder structures: src/ponder/structure/assets/mam/ponder/ → assets/mam/ponder/
      Path ponderSnbtFolder = projectRoot.resolve("src").resolve("ponder").resolve("structure");

      // SnbtToNbt uses a fixed provider name so we can only register it once; combine all source folders
      List<Path> snbtFolders = new java.util.ArrayList<>();
      if (Files.exists(snbtSourceFolder)) snbtFolders.add(snbtSourceFolder);
      if (Files.exists(ponderSnbtFolder)) snbtFolders.add(ponderSnbtFolder);
      if (!snbtFolders.isEmpty()) {
        generator.addProvider(
            event.includeServer() || event.includeClient(),
            new SnbtToNbt(packOutput, snbtFolders)
        );
      }

      generator.addProvider(event.includeServer(),
          new MamRecipeProvider(packOutput, event.getLookupProvider()));
    }
}
