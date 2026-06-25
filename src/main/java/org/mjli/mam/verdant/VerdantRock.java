package org.mjli.mam.verdant;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;

public class VerdantRock {

    private static final MamRegistrate R = MightAndMagic.registrate();

    public static final BlockEntry<Block> LIVING_ROCK =
        R.block("living_rock", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> LIVING_ROCK_POLISHED =
        R.block("living_rock_polished", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<Block> LIVING_ROCK_BRICK =
        R.block("living_rock_brick", Block::new)
         .properties(p -> MamBlockProperties.livingRock())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static void appendToTab(CreativeModeTab.Output output) {
        output.accept(LIVING_ROCK.asStack());
        output.accept(LIVING_ROCK_POLISHED.asStack());
        output.accept(LIVING_ROCK_BRICK.asStack());
    }

    public static void init() {}
}
