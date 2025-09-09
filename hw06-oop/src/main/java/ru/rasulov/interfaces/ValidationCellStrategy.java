package ru.rasulov.interfaces;

import ru.rasulov.model.Banknote;

public interface ValidationCellStrategy {
    void validate(Cell cell, Banknote banknote);

}