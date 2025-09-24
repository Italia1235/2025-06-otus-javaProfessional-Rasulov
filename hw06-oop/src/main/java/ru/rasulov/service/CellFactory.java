package ru.rasulov.service;

import ru.rasulov.interfaces.CalculationStrategy;
import ru.rasulov.interfaces.Cell;
import ru.rasulov.interfaces.ValidationCellStrategy;
import ru.rasulov.model.BasicCell;
import ru.rasulov.model.Nominal;
import ru.rasulov.strategy.DefaultValidationCellStrategy;
import ru.rasulov.strategy.NominalCalculationStrategy;

public class CellFactory {
    public static Cell createCell(Nominal nominal, int capacity) {
        ValidationCellStrategy validation = new DefaultValidationCellStrategy();
        CalculationStrategy calculation = new NominalCalculationStrategy();

        return new BasicCell(nominal, capacity, validation, calculation);
    }
}