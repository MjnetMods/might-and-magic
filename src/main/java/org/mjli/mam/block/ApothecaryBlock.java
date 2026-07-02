package org.mjli.mam.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mjli.mam.MamBlockEntities;
import org.mjli.mam.block_entity.ApothecaryBlockEntity;

import javax.annotation.Nullable;

public class ApothecaryBlock extends BaseEntityBlock {
    public static final MapCodec<ApothecaryBlock> CODEC = simpleCodec(ApothecaryBlock::new);

    // Mirrors the pedestal+goblet geometry in models/block/shapes/apothecary.json — without this,
    // the block falls back to a full 16x16x16 cube for collision/outline/face-occlusion, which
    // both gives an oversized hitbox and wrongly culls neighboring blocks' faces against it.
    private static final VoxelShape SHAPE = Shapes.or(
        Block.box(2, 0, 2, 14, 2, 14),
        Block.box(4, 2, 4, 12, 11, 12),
        Block.box(2, 11, 2, 14, 12, 14),
        Block.box(2, 12, 13, 14, 16, 14),
        Block.box(2, 12, 2, 14, 16, 3),
        Block.box(2, 12, 3, 3, 16, 13),
        Block.box(13, 12, 3, 14, 16, 13)
    );

    public ApothecaryBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ApothecaryBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, MamBlockEntities.APOTHECARY.get(), ApothecaryBlockEntity::tick);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof ApothecaryBlockEntity apothecary) {
            return apothecary.interact(player);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
