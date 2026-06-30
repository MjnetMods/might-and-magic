package org.mjli.mam.api.mana;

import java.util.Optional;
import net.minecraft.world.item.DyeColor;

public interface ManaPool extends ManaReceiver {
    boolean isOutputtingPower();
    Optional<DyeColor> getColor();
    void setColor(Optional<DyeColor> color);
    ManaEnergyType getEnergyType();
}
