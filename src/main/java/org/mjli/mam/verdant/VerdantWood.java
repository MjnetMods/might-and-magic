package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;

public class VerdantWood {

    private static final MamRegistrate R = MightAndMagic.registrate();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_LOG =
        R.block("livingwood_log", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_LOG_STRIPPED =
        R.block("livingwood_log_stripped", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_LOG_GLIMMERING =
        R.block("livingwood_log_glimmering", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog().lightLevel(s -> 12))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_LOG_STRIPPED_GLIMMERING =
        R.block("livingwood_log_stripped_glimmering", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog().lightLevel(s -> 8))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD =
        R.block("livingwood", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<RotatedPillarBlock> LIVINGWOOD_STRIPPED =
        R.block("livingwood_stripped", RotatedPillarBlock::new)
         .properties(p -> MamBlockProperties.livingWoodLog())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> LIVINGWOOD_PLANKS =
        R.block("livingwood_planks", Block::new)
         .properties(p -> MamBlockProperties.livingWood())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> LIVINGWOOD_PLANKS_MOSSY =
        R.block("livingwood_planks_mossy", Block::new)
         .properties(p -> MamBlockProperties.livingWood())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(LIVINGWOOD_LOG.asStack(), tab);
        modifier.accept(LIVINGWOOD_LOG_STRIPPED.asStack(), tab);
        modifier.accept(LIVINGWOOD_LOG_GLIMMERING.asStack(), tab);
        modifier.accept(LIVINGWOOD_LOG_STRIPPED_GLIMMERING.asStack(), tab);
        modifier.accept(LIVINGWOOD.asStack(), tab);
        modifier.accept(LIVINGWOOD_STRIPPED.asStack(), tab);
        modifier.accept(LIVINGWOOD_PLANKS.asStack(), tab);
        modifier.accept(LIVINGWOOD_PLANKS_MOSSY.asStack(), tab);
    }

    public static void init() {}
}
