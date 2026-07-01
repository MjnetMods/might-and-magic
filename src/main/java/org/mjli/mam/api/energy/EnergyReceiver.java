package org.mjli.mam.api.energy;

public interface EnergyReceiver {
    int getCurrentEnergy();
    int getMaxEnergy();
    boolean isFull();
    void receiveEnergy(int amount);
    boolean canReceiveEnergyFromBursts();
}
