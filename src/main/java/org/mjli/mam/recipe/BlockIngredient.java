package org.mjli.mam.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public sealed interface BlockIngredient permits BlockIngredient.OfBlock, BlockIngredient.OfTag {

    boolean test(BlockState state);

    record OfBlock(Block block) implements BlockIngredient {
        public boolean test(BlockState state) { return state.is(block); }
    }

    record OfTag(TagKey<Block> tag) implements BlockIngredient {
        public boolean test(BlockState state) { return state.is(tag); }
    }

    Codec<OfBlock> BLOCK_CODEC = BuiltInRegistries.BLOCK.byNameCodec()
            .fieldOf("block").codec()
            .xmap(OfBlock::new, OfBlock::block);

    Codec<OfTag> TAG_CODEC = ResourceLocation.CODEC
            .fieldOf("tag").codec()
            .xmap(rl -> new OfTag(TagKey.create(Registries.BLOCK, rl)), ot -> ot.tag().location());

    Codec<BlockIngredient> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<BlockIngredient, T>> decode(DynamicOps<T> ops, T input) {
            DataResult<Pair<OfTag, T>> tagResult = TAG_CODEC.decode(ops, input);
            if (tagResult.result().isPresent())
                return tagResult.map(p -> Pair.of(p.getFirst(), p.getSecond()));
            return BLOCK_CODEC.decode(ops, input).map(p -> Pair.of(p.getFirst(), p.getSecond()));
        }

        @Override
        public <T> DataResult<T> encode(BlockIngredient input, DynamicOps<T> ops, T prefix) {
            if (input instanceof OfTag ot) return TAG_CODEC.encode(ot, ops, prefix);
            return BLOCK_CODEC.encode((OfBlock) input, ops, prefix);
        }

        @Override public String toString() { return "BlockIngredient"; }
    };

    StreamCodec<RegistryFriendlyByteBuf, BlockIngredient> STREAM_CODEC = StreamCodec.of(
            (buf, ing) -> {
                if (ing instanceof OfTag ot) {
                    buf.writeBoolean(true);
                    buf.writeResourceLocation(ot.tag().location());
                } else {
                    buf.writeBoolean(false);
                    buf.writeResourceLocation(BuiltInRegistries.BLOCK.getKey(((OfBlock) ing).block()));
                }
            },
            buf -> {
                boolean isTag = buf.readBoolean();
                ResourceLocation rl = buf.readResourceLocation();
                if (isTag) return new OfTag(TagKey.create(Registries.BLOCK, rl));
                return new OfBlock(BuiltInRegistries.BLOCK.get(rl));
            }
    );
}
