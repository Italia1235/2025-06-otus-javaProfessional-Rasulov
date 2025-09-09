package ru.rasulov.strategy;

import ru.rasulov.interfaces.CalculationStrategy;
import ru.rasulov.interfaces.Cell;

public class NominalCalculationStrategy implements CalculationStrategy {
    @Override
    public long calculate(Cell cell) {
        return (long) cell.getCount() * cell.getNominal().getValue();
    }
}
