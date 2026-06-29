package org.mjli.mam.block.flower;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mjli.mam.MamBlockEntities;
import org.mjli.mam.block_entity.flower.EmberwortBlockEntity;

import javax.annotation.Nullable;

public class EmberwortBlock extends GeneratingFlowerBlock {
    public static final MapCodec<EmberwortBlock> CODEC = simpleCodec(EmberwortBlock::new);

    public EmberwortBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EmberwortBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : createTickerHelper(type, MamBlockEntities.EMBERWORT.get(), (l, p, s, be) -> be.serverTick());
    }
}
