package org.mjli.mam.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;

import javax.annotation.Nullable;

// Loop Marking (design/magic/16_mana-spreader.md) — NONE plus one value per DyeColor, so
// "unmarked" is a real state rather than nullable DyeColor (blockstate properties can't be null).
public enum SpreaderMarkColor implements StringRepresentable {
    NONE(null),
    WHITE(DyeColor.WHITE),
    ORANGE(DyeColor.ORANGE),
    MAGENTA(DyeColor.MAGENTA),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE),
    YELLOW(DyeColor.YELLOW),
    LIME(DyeColor.LIME),
    PINK(DyeColor.PINK),
    GRAY(DyeColor.GRAY),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY),
    CYAN(DyeColor.CYAN),
    PURPLE(DyeColor.PURPLE),
    BLUE(DyeColor.BLUE),
    BROWN(DyeColor.BROWN),
    GREEN(DyeColor.GREEN),
    RED(DyeColor.RED),
    BLACK(DyeColor.BLACK);

    @Nullable
    private final DyeColor dyeColor;

    SpreaderMarkColor(@Nullable DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    public boolean isMarked() {
        return dyeColor != null;
    }

    /** Tint RGB for this mark, unused (never sampled) when {@link #isMarked()} is false. */
    public int getTint() {
        return dyeColor != null ? dyeColor.getTextureDiffuseColor() : 0xFFFFFF;
    }

    public static SpreaderMarkColor fromDyeColor(DyeColor color) {
        for (SpreaderMarkColor mark : values()) {
            if (mark.dyeColor == color) return mark;
        }
        throw new IllegalArgumentException("No SpreaderMarkColor for " + color);
    }

    @Override
    public String getSerializedName() {
        return dyeColor != null ? dyeColor.getSerializedName() : "none";
    }
}
