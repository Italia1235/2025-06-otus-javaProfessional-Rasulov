package ru.rasulov.model;

import ru.rasulov.interfaces.CalculationStrategy;
import ru.rasulov.interfaces.Cell;
import ru.rasulov.interfaces.ValidationCellStrategy;

import java.util.ArrayList;
import java.util.List;

public class BasicCell implements Cell {
    private final Nominal nominal;
    private final int capacity;

    private int countBanknotes;
    private final ValidationCellStrategy validationStrategy;
    private final CalculationStrategy calculationStrategy;

    @Override
    public List<Banknote> getBanknotes(int count) {
        if (count > countBanknotes) {
            throw new IllegalArgumentException("Don't have some banknotes");
        }
        List<Banknote> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(new Banknote(nominal));
        }

        // Уменьшаем счетчик банкнот в ячейке
        this.countBanknotes -= count;
        return result;

    }

    public BasicCell(Nominal nominal, int capacity, ValidationCellStrategy validationStrategy, CalculationStrategy calculationStrategy) {
        this.nominal = nominal;
        this.capacity = capacity;
        this.validationStrategy = validationStrategy;
        this.calculationStrategy = calculationStrategy;
    }

    @Override
    public void addBanknote(Banknote banknote) {
        validationStrategy.validate(this, banknote);
        countBanknotes++;
    }

    @Override
    public long calculateSum() {
        return calculationStrategy.calculate(this);
    }


    public Nominal getNominal() {
        return nominal;
    }

    @Override
    public int getCount() {
        return countBanknotes;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }
}
