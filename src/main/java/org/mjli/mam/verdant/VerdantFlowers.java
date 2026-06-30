package org.mjli.mam.verdant;

import com.tterrag.registrate.util.CreativeModeTabModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.mjli.mam.MightAndMagic;
import org.mjli.mam.block.MysticalMushroomBlock;
import org.mjli.mam.block.flower.BuriedPetalBlock;
import org.mjli.mam.block.flower.MysticalFlowerBlock;
import org.mjli.mam.block.flower.PureDaisyBlock;
import org.mjli.mam.block.flower.TallMysticalFlowerBlock;
import org.mjli.mam.foundation.registration.DyedBlockList;
import org.mjli.mam.foundation.registration.DyedItemList;
import org.mjli.mam.foundation.registration.MamBlockProperties;
import org.mjli.mam.foundation.registration.MamRegistrate;
import org.mjli.mam.item.FloralPowderItem;
import org.mjli.mam.item.GuideItem;

public class VerdantFlowers {

    private static final MamRegistrate R = MightAndMagic.registrate();

    // Helper methods — methods may reference any static field regardless of declaration order
    private static ItemStack petalStack(DyeColor color) {
        return new ItemStack(PETALS.get(color).get());
    }
    private static Item petalItem(DyeColor color) {
        return PETALS.get(color).get();
    }

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

    // 1. TALL_FLOWERS — no VerdantFlowers deps at init time
    //    Petal supplier is a lazy ref: only resolved at shear-time (runtime), not during <clinit>
    public static final DyedBlockList<TallMysticalFlowerBlock> TALL_FLOWERS = new DyedBlockList<>(color ->
        R.block(color.getSerializedName() + "_tall_mystical_flower",
                p -> new TallMysticalFlowerBlock(color, () -> petalStack(color), p))
         .properties(p -> MamBlockProperties.flower())
         .blockstate((ctx, p) -> {
             String name = ctx.getName();
             var lower = p.models().cross(name, p.modLoc("block/" + name)).renderType("cutout");
             var upper = p.models().cross(name + "_top", p.modLoc("block/" + name + "_top")).renderType("cutout");
             p.getVariantBuilder(ctx.get())
                 .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).modelForState().modelFile(lower).addModel()
                 .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).modelForState().modelFile(upper).addModel();
         })
         .loot((table, block) -> table.add(block, LootTable.lootTable()
             .withPool(LootPool.lootPool()
                 .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                     .setProperties(StatePropertiesPredicate.Builder.properties()
                         .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
                 .add(LootItem.lootTableItem(petalItem(block.color))
                     .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)))))))
         .register()
    );

    // 2. FLOWERS — depends on TALL_FLOWERS (tall supplier resolved at bonemeal-time, not init)
    public static final DyedBlockList<MysticalFlowerBlock> FLOWERS = new DyedBlockList<>(color ->
        R.block(color.getSerializedName() + "_mystical_flower",
                p -> new MysticalFlowerBlock(color, EFFECTS[color.ordinal()], 240,
                    () -> TALL_FLOWERS.get(color).get(), p))
         .properties(p -> MamBlockProperties.flower())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(), p.modLoc("block/" + ctx.getName())).renderType("cutout")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register()
    );

    // 3. BURIED_PETALS — invisible internal block; flower supplier resolved at bonemeal-time
    //    No block item — accessed only via ItemNameBlockItem in PETALS
    public static final DyedBlockList<BuriedPetalBlock> BURIED_PETALS = new DyedBlockList<>(color ->
        R.block(color.getSerializedName() + "_buried_petal",
                p -> new BuriedPetalBlock(color, () -> FLOWERS.get(color).get(), p))
         .properties(p -> MamBlockProperties.buriedPetal())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(),
                 p.modLoc("item/" + color.getSerializedName() + "_petal")).renderType("cutout")))
         .loot((table, block) -> table.add(block, LootTable.lootTable()
             .withPool(LootPool.lootPool()
                 .when(ExplosionCondition.survivesExplosion())
                 .add(LootItem.lootTableItem(petalItem(block.color))))))
         .register()
    );

    // 4. PETALS — ItemNameBlockItem: right-clicking on ground places the matching BuriedPetalBlock
    public static final DyedItemList<Item> PETALS = new DyedItemList<>(color ->
        R.item(color.getSerializedName() + "_petal",
               p -> new ItemNameBlockItem(BURIED_PETALS.get(color).get(), p))
         .register()
    );

    // 5. FLORAL_POWDER — single item; scatters random flowers from mam:mystical_flowers tag
    public static final ItemEntry<FloralPowderItem> FLORAL_POWDER =
        R.item("floral_powder", p -> new FloralPowderItem(p))
         .register();

    public static final DyedBlockList<MysticalMushroomBlock> MUSHROOMS = new DyedBlockList<>(color ->
        R.block(color.getSerializedName() + "_mystical_mushroom",
                p -> new MysticalMushroomBlock(color, p))
         .properties(p -> MamBlockProperties.mushroom())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(), p.modLoc("block/" + ctx.getName())).renderType("cutout")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register()
    );

    public static final BlockEntry<PureDaisyBlock> PURE_DAISY =
        R.block("pure_daisy", PureDaisyBlock::new)
         .properties(p -> MamBlockProperties.flower())
         .blockstate((ctx, p) -> p.simpleBlock(ctx.get(),
             p.models().cross(ctx.getName(), p.modLoc("block/" + ctx.getName())).renderType("cutout")))
         .loot((t, b) -> t.dropSelf(b))
         .simpleItem()
         .register();

    public static final ItemEntry<GuideItem> GUIDE =
        R.item("guide", p -> new GuideItem(p.stacksTo(1)))
         .register();

    public static void appendToTab(CreativeModeTabModifier modifier) {
        var tab = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
        modifier.accept(new ItemStack(GUIDE.get()), tab);
        PETALS.forEach(e -> modifier.accept(new ItemStack(e.get()), tab));
        FLOWERS.forEach(e -> modifier.accept(e.asStack(), tab));
        modifier.accept(new ItemStack(FLORAL_POWDER.get()), tab);
        modifier.accept(PURE_DAISY.asStack(), tab);
        MUSHROOMS.forEach(e -> modifier.accept(e.asStack(), tab));
    }

    public static void init() {}
}
