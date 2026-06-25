package org.mjli.mam.foundation.registration;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

// Same pattern as DyedBlockList but for plain items (e.g. petals)
public class DyedItemList<T extends Item> implements Iterable<ItemEntry<T>> {

    private static final int COLOR_AMOUNT = DyeColor.values().length;
    private final ItemEntry<?>[] values = new ItemEntry<?>[COLOR_AMOUNT];

    public DyedItemList(Function<DyeColor, ItemEntry<? extends T>> filler) {
        for (DyeColor color : DyeColor.values()) {
            values[color.ordinal()] = filler.apply(color);
        }
    }

    @SuppressWarnings("unchecked")
    public ItemEntry<T> get(DyeColor color) {
        return (ItemEntry<T>) values[color.ordinal()];
    }

    @SuppressWarnings("unchecked")
    public ItemEntry<T>[] toArray() {
        return (ItemEntry<T>[]) Arrays.copyOf(values, values.length);
    }

    @Override
    public Iterator<ItemEntry<T>> iterator() {
        return new Iterator<>() {
            private int index = 0;
            @Override public boolean hasNext() { return index < values.length; }
            @SuppressWarnings("unchecked")
            @Override public ItemEntry<T> next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (ItemEntry<T>) values[index++];
            }
        };
    }
}
