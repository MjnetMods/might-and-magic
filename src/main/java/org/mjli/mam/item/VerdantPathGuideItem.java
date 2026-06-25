package org.mjli.mam.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.PatchouliAPI;

public class VerdantPathGuideItem extends Item {

    private static final ResourceLocation BOOK_ID = ResourceLocation.fromNamespaceAndPath("mam", "verdant_path");

    public VerdantPathGuideItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            PatchouliAPI.get().openBookGUI(BOOK_ID);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
