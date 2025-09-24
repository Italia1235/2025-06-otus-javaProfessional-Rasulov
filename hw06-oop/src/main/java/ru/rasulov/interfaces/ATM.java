package ru.rasulov.interfaces;

import ru.rasulov.model.Banknote;
import ru.rasulov.exception.ATMException;

import java.util.List;

public interface ATM {
    void downloadMoney(List<Banknote> banknotes) throws ATMException;
    List<Cell> getCells();
    List<Banknote> withDrawMoney(int amount) throws ATMException;

    long getBalance();


}
