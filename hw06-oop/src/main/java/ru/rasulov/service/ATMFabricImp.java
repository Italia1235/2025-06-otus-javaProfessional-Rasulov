package ru.rasulov.service;

import ru.rasulov.interfaces.ATM;
import ru.rasulov.interfaces.ATMFabric;
import ru.rasulov.interfaces.SettingsATM;

public class ATMFabricImp implements ATMFabric {
    @Override
    public ATM createATM(SettingsATM settingsATM) {
        return new DollarATM(settingsATM.getCells());
    }
}