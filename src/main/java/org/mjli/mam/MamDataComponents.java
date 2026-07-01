package org.mjli.mam;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mjli.mam.api.energy.EnergyContainer;

public class MamDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MightAndMagic.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnergyContainer>> ENERGY_CONTAINER =
            DATA_COMPONENT_TYPES.register("energy_container", () -> DataComponentType.<EnergyContainer>builder()
                    .persistent(EnergyContainer.CODEC)
                    .networkSynchronized(EnergyContainer.STREAM_CODEC)
                    .build());

    public static void register(IEventBus bus) {
        DATA_COMPONENT_TYPES.register(bus);
    }
}
