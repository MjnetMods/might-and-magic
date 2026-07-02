package org.mjli.mam.block.mana;

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
import org.mjli.mam.block_entity.mana.ManaPoolBlockEntity;
import org.mjli.mam.verdant.VerdantMana;

import javax.annotation.Nullable;

public class ManaPoolBlock extends BaseEntityBlock {
    public static final MapCodec<ManaPoolBlock> CODEC = simpleCodec(ManaPoolBlock::new);

    // Mirrors the basin geometry in models/block/shapes/mana_pool.json — without this, the
    // block falls back to a full 16x16x16 cube for collision/outline/face-occlusion, wrongly
    // culling neighboring blocks' faces against it (same defect fixed for Apothecary/Altar).
    private static final VoxelShape SHAPE = Shapes.or(
        Block.box(0, 0, 0, 16, 2, 16),
        Block.box(0, 2, 14, 16, 8, 16),
        Block.box(0, 2, 0, 16, 8, 2),
        Block.box(0, 2, 2, 2, 8, 14),
        Block.box(14, 2, 2, 16, 8, 14)
    );

    public ManaPoolBlock(BlockBehaviour.Properties properties) {
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
        if (state.is(VerdantMana.INFUSED_MANA_POOL.get())) return MamBlockEntities.INFUSED_MANA_POOL.get().create(pos, state);
        if (state.is(VerdantMana.SACRED_MANA_POOL.get())) return MamBlockEntities.SACRED_MANA_POOL.get().create(pos, state);
        if (state.is(VerdantMana.DESECRATED_MANA_POOL.get())) return MamBlockEntities.DESECRATED_MANA_POOL.get().create(pos, state);
        return MamBlockEntities.MANA_POOL.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        BlockEntityTicker<T> ticker = createTickerHelper(type, MamBlockEntities.MANA_POOL.get(), ManaPoolBlockEntity::tick);
        if (ticker == null) ticker = createTickerHelper(type, MamBlockEntities.INFUSED_MANA_POOL.get(), ManaPoolBlockEntity::tick);
        if (ticker == null) ticker = createTickerHelper(type, MamBlockEntities.SACRED_MANA_POOL.get(), ManaPoolBlockEntity::tick);
        if (ticker == null) ticker = createTickerHelper(type, MamBlockEntities.DESECRATED_MANA_POOL.get(), ManaPoolBlockEntity::tick);
        return ticker;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ManaPoolBlockEntity pool) {
            return ManaPoolBlockEntity.calculateComparatorLevel(pool.getCurrentEnergy(), pool.getMaxEnergy());
        }
        return 0;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof ManaPoolBlockEntity pool) {
            return pool.interact(player);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
