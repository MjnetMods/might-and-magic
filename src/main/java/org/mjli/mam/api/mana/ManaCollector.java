package org.mjli.mam.api.mana;

public interface ManaCollector extends ManaReceiver {
    float getManaYieldMultiplier();
    void onClientDisplayTick();
}
