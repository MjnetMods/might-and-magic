package org.mjli.mam;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.mjli.mam.block_entity.ApothecaryBlockEntity;

public class MamCapabilities {
    public static void register(IEventBus bus) {
        bus.addListener(MamCapabilities::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                MamBlockEntities.APOTHECARY.get(),
                (be, side) -> be.getFluidTank());
    }
}
