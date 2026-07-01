package org.mjli.mam.api.energy;

import java.util.Optional;
import net.minecraft.world.item.DyeColor;

public interface EnergyPool extends EnergyReceiver {
    boolean isOutputtingPower();
    Optional<DyeColor> getColor();
    void setColor(Optional<DyeColor> color);
    EnergyType getEnergyType();
    void receiveEnergy(int amount, EnergyType incomingType);
}
