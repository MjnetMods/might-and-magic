package org.mjli.mam.api.energy;

public interface EnergyCollector extends EnergyReceiver {
    float getEnergyYieldMultiplier();
    void onClientDisplayTick();
}
