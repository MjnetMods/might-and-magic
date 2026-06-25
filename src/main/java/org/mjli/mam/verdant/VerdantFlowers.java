package org.mjli.mam.verdant;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block.MysticalMushroomBlock;
import org.mjli.mam.block.flower.MysticalFlowerBlock;
import org.mjli.mam.block.flower.PureDaisyBlock;
import org.mjli.mam.foundation.registration.DyedBlockList;
import org.mjli.mam.foundation.registration.DyedItemList;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;
import org.mjli.mam.item.VerdantPathGuideItem;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;

public class VerdantFlowers {

    private static final MamRegistrate R = MightAndMagic.registrate();

    // Effect per dye color (ordinal-indexed, matching DyeColor.values() order)
    @SuppressWarnings("unchecked")
    private static final Holder<MobEffect>[] EFFECTS = new Holder[]{
        MobEffects.MOVEMENT_SPEED,    // WHITE
        MobEffects.FIRE_RESISTANCE,   // ORANGE
        MobEffects.DIG_SLOWDOWN,      // MAGENTA
        MobEffects.JUMP,              // LIGHT_BLUE
        MobEffects.ABSORPTION,        // YELLOW
        MobEffects.POISON,            // LIME
        MobEffects.REGENERATION,      // PINK
        MobEffects.DAMAGE_RESISTANCE, // GRAY
        MobEffects.WEAKNESS,          // LIGHT_GRAY
        MobEffects.WATER_BREATHING,   // CYAN
        MobEffects.CONFUSION,         // PURPLE
        MobEffects.NIGHT_VISION,      // BLUE
        MobEffects.WITHER,            // BROWN
        MobEffects.HUNGER,            // GREEN
        MobEffects.DAMAGE_BOOST,      // RED
        MobEffects.BLINDNESS          // BLACK
    };

    public static final DyedBlockList<MysticalFlowerBlock> FLOWERS = new DyedBlockList<>(color ->
        R.block(color.getSerializedName() + "_mystical_flower",
                p -> new MysticalFlowerBlock(color, EFFECTS[color.ordinal()], 240, p))
         .properties(p -> MamBlockProperties.flower())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register()
    );

    public static final DyedBlockList<MysticalMushroomBlock> MUSHROOMS = new DyedBlockList<>(color ->
        R.block(color.getSerializedName() + "_mystical_mushroom",
                p -> new MysticalMushroomBlock(color, p))
         .properties(p -> MamBlockProperties.mushroom())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register()
    );

    public static final DyedItemList<Item> PETALS = new DyedItemList<>(color ->
        R.item(color.getSerializedName() + "_petal", Item::new)
         .register()
    );

    public static final BlockEntry<PureDaisyBlock> PURE_DAISY =
        R.block("pure_daisy", PureDaisyBlock::new)
         .properties(p -> MamBlockProperties.flower())
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final ItemEntry<VerdantPathGuideItem> VERDANT_PATH_GUIDE =
        R.item("verdant_path_guide", p -> new VerdantPathGuideItem(p.stacksTo(1)))
         .register();

    public static void appendToTab(CreativeModeTab.Output output) {
        output.accept(VERDANT_PATH_GUIDE.get());
        PETALS.forEach(e -> output.accept(e.get()));
        FLOWERS.forEach(e -> output.accept(e.asStack()));
        output.accept(PURE_DAISY.asStack());
        MUSHROOMS.forEach(e -> output.accept(e.asStack()));
    }

    public static void init() {}
}
