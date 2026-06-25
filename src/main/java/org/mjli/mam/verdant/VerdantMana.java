package org.mjli.mam.verdant;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTab;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block.PetalApothecaryBlock;
import org.mjli.mam.block.mana.ManaPoolBlock;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;

public class VerdantMana {

    private static final MamRegistrate R = MightAndMagic.registrate();

    public static final BlockEntry<ManaPoolBlock> MANA_POOL =
        R.block("mana_pool", ManaPoolBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final BlockEntry<PetalApothecaryBlock> PETAL_APOTHECARY =
        R.block("petal_apothecary", PetalApothecaryBlock::new)
         .properties(p -> MamBlockProperties.manaPool())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static void appendToTab(CreativeModeTab.Output output) {
        output.accept(MANA_POOL.asStack());
        output.accept(PETAL_APOTHECARY.asStack());
    }

    public static void init() {}
}
