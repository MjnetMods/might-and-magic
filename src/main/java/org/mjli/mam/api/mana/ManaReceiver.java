package org.mjli.mam.api.mana;

public interface ManaReceiver {
    int getCurrentMana();
    int getMaxMana();
    boolean isFull();
    void receiveMana(int mana);
    boolean canReceiveManaFromBursts();
}
