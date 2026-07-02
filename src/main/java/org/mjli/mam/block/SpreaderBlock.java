package org.mjli.mam.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mjli.mam.verdant.VerdantFlowers;

public class SpreaderBlock extends Block {
    public static final MapCodec<SpreaderBlock> CODEC = simpleCodec(SpreaderBlock::new);

    // Loop Marking (design/magic/16_mana-spreader.md) — right-click with a petal to mark/re-mark,
    // no BlockEntity: plain blockstate property, so the existing dropSelf loot table already
    // resets it to unmarked on break/pickup, with no state-copying code needed.
    public static final EnumProperty<SpreaderMarkColor> MARK = EnumProperty.create("mark", SpreaderMarkColor.class);

    // Mirrors models/block/shapes/mana_spreader.json (ported from Botania's spreader.json) —
    // without this, the block falls back to a full 16x16x16 cube for collision/outline/
    // face-occlusion (design/magic/16_mana-spreader.md).
    private static final VoxelShape SHAPE = Shapes.or(
        Block.box(2, 2, 2, 3, 14, 14),
        Block.box(13, 2, 2, 14, 14, 14),
        Block.box(3, 13, 2, 13, 14, 14),
        Block.box(3, 2, 2, 13, 3, 14),
        Block.box(3, 3, 13, 13, 13, 14),
        Block.box(6, 10, 2, 13, 13, 3),
        Block.box(3, 3, 2, 10, 6, 3),
        Block.box(3, 6, 2, 6, 13, 3),
        Block.box(10, 3, 2, 13, 10, 3)
    );

    public SpreaderBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(MARK, SpreaderMarkColor.NONE));
    }

    @Override
    public MapCodec<? extends Block> codec() { return CODEC; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(MARK);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                               Player player, InteractionHand hand, BlockHitResult hit) {
        DyeColor petalColor = petalColorOf(stack);
        if (petalColor == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        SpreaderMarkColor mark = SpreaderMarkColor.fromDyeColor(petalColor);
        if (state.getValue(MARK) == mark) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(MARK, mark), 3);
            if (!player.getAbilities().instabuild) stack.shrink(1);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    private static DyeColor petalColorOf(ItemStack stack) {
        for (DyeColor color : DyeColor.values()) {
            if (VerdantFlowers.PETALS.get(color).get() == stack.getItem()) return color;
        }
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
