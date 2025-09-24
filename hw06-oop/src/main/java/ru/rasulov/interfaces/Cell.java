package ru.rasulov.interfaces;

import ru.rasulov.model.Banknote;
import ru.rasulov.model.Nominal;

import java.util.List;

public interface Cell {
    public List<Banknote> getBanknotes(int count);
    void addBanknote(Banknote banknote);
    long calculateSum();
    Nominal getNominal();
    int getCount();
    int getCapacity();
}
