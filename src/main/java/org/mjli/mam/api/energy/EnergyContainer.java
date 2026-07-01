package org.mjli.mam.api.energy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

// aligned = fixed to one energy type (T3 Sacred/Desecrated); unaligned = accepts either, flipping
// energyType on mismatch (T1/T2). Same tiering rule for both Mana Pools and Tablets/Rings.
public record EnergyContainer(int energy, int maxEnergy, EnergyType energyType, boolean aligned) {
    public static final Codec<EnergyContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("energy").forGetter(EnergyContainer::energy),
            Codec.INT.fieldOf("max_energy").forGetter(EnergyContainer::maxEnergy),
            EnergyType.CODEC.fieldOf("energy_type").forGetter(EnergyContainer::energyType),
            Codec.BOOL.fieldOf("aligned").forGetter(EnergyContainer::aligned)
    ).apply(instance, EnergyContainer::new));

    public static final StreamCodec<ByteBuf, EnergyContainer> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, EnergyContainer::energy,
            ByteBufCodecs.VAR_INT, EnergyContainer::maxEnergy,
            EnergyType.STREAM_CODEC, EnergyContainer::energyType,
            ByteBufCodecs.BOOL, EnergyContainer::aligned,
            EnergyContainer::new
    );

    public boolean isFull() {
        return energy >= maxEnergy;
    }

    public EnergyContainer withEnergy(int newEnergy) {
        return new EnergyContainer(Math.max(0, Math.min(newEnergy, maxEnergy)), maxEnergy, energyType, aligned);
    }

    // Matching type: adds. Aligned + mismatched: opposing energy drains instead of converting.
    // Unaligned + mismatched: flips to the incoming type (T1/T2 tablets/pools "convert").
    public EnergyContainer receive(int amount, EnergyType incomingType) {
        if (incomingType == energyType) {
            return withEnergy(energy + amount);
        } else if (aligned) {
            return withEnergy(energy - Math.min(energy, amount));
        } else {
            return new EnergyContainer(energy, maxEnergy, incomingType, aligned).withEnergy(energy + amount);
        }
    }
}
