package ru.rasulov.strategy;

import ru.rasulov.interfaces.Cell;
import ru.rasulov.interfaces.ValidationCellStrategy;
import ru.rasulov.model.Banknote;

public class DefaultValidationCellStrategy implements ValidationCellStrategy {
    @Override
    public void validate(Cell cell, Banknote banknote) throws IllegalArgumentException {
        if (!cell.getNominal().equals(banknote.nominal())) {
            throw new IllegalArgumentException("Wrong banknote nominal");
        }
        if (cell.getCount() >= cell.getCapacity()) {
            throw new IllegalStateException("Cell capacity exceeded");
        }
    }

}
