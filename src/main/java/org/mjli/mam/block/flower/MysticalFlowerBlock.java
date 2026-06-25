package org.mjli.mam.block.flower;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MysticalFlowerBlock extends FlowerBlock {
    public final DyeColor color;

    public MysticalFlowerBlock(DyeColor color, Holder<MobEffect> effect, int effectDuration, BlockBehaviour.Properties properties) {
        super(effect, effectDuration, properties);
        this.color = color;
    }
}
