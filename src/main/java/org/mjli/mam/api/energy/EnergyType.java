package org.mjli.mam.api.energy;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum EnergyType {
    MANA,
    NOX;

    public static final Codec<EnergyType> CODEC = Codec.STRING.xmap(EnergyType::valueOf, EnergyType::name);
    public static final StreamCodec<ByteBuf, EnergyType> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(i -> EnergyType.values()[i], EnergyType::ordinal);
}
