package ru.rasulov.helper;

import ru.rasulov.exception.ATMException;
import ru.rasulov.interfaces.Cell;
import ru.rasulov.model.Banknote;

import java.util.List;

public class ATMHelper {

    public static long countBanknotesValue(List<Banknote> banknotes) {
        return banknotes.stream()
                .mapToLong(banknote -> banknote.nominal().getValue())
                .sum();
    }

    public static void validateCellsAndAmountMoney(List<Cell> cells, int money) throws ATMException {
        if (cells == null || cells.isEmpty()) {
            throw new ATMException("Банкомат не содержит ячеек для выдачи денег");
        }

        if (money <= 0) {
            throw new ATMException("Запрошенная сумма должна быть положительной");
        }

        long balance = calculateTotalBalance(cells);
        if (money > balance) {
            throw new ATMException("Запрошенная сумма превышает баланс банкомата");
        }

        int gcd = computeGCD(cells);
        if (money % gcd != 0) {
            throw new ATMException("Невозможно выдать запрошенную сумму из-за несоответствия номиналов");
        }
    }

    public static boolean canWithdraw(List<Cell> cells, int amount) {
        if (cells == null || cells.isEmpty() || amount <= 0) {
            return false;
        }

        long balance = calculateTotalBalance(cells);
        if (amount > balance) {
            return false;
        }

        int gcd = computeGCD(cells);
        return amount % gcd == 0;
    }

    private static long calculateTotalBalance(List<Cell> cells) {
        return cells.stream()
                .mapToLong(Cell::calculateSum)
                .sum();
    }

    private static int computeGCD(List<Cell> cells) {
        int result = 0;
        for (Cell cell : cells) {
            result = gcd(result, cell.getNominal().getValue());
        }
        return result;
    }

    private static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}