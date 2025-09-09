package ru.rasulov.model;

public enum Nominal {
    ONE(1), TWO(2), FIVE(5), TEN(10), TWENTY(20), FIFTY(50), HUNDRED(100);

    final int nominal;

    Nominal(int nominal) {
        this.nominal = nominal;
    }

    public int getValue() {
        return nominal;
    }
}
