package ru.rasulov.service;

import ru.rasulov.exception.ATMException;
import ru.rasulov.helper.ATMHelper;
import ru.rasulov.interfaces.ATM;
import ru.rasulov.interfaces.Cell;
import ru.rasulov.model.Banknote;
import ru.rasulov.model.Nominal;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DollarATM implements ATM {

    List<Cell> cells;

    public List<Cell> getCells() {
        return cells;
    }

    public DollarATM(List<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public List<Banknote> withDrawMoney(int amount) throws ATMException {

        ATMHelper.validateCellsAndAmountMoney(cells, amount);
        List<Cell> sortedCells = cells.stream()
                .sorted(Comparator.comparingInt((Cell c) -> c.getNominal().getValue()).reversed())
                .toList();
        List<Banknote> result = new ArrayList<>();
        int remainingAmount = amount;
        for (Cell cell : sortedCells) {
            int nominalValue = cell.getNominal().getValue();


            if (nominalValue > remainingAmount) {
                continue;
            }


            int maxBanknotes = Math.min(remainingAmount / nominalValue, cell.getCount());
            if (maxBanknotes > 0) {

                List<Banknote> banknotes = cell.getBanknotes(maxBanknotes);
                result.addAll(banknotes);


                remainingAmount -= maxBanknotes * nominalValue;


                if (remainingAmount == 0) {
                    break;
                }
            }
        }


        if (remainingAmount > 0) {
            throw new ATMException("Невозможно выдать запрошенную сумму");
        }

        return result;


    }

    @Override
    public long getBalance() {
        return cells.stream()
                .mapToLong(Cell::calculateSum)
                .sum();
    }

    public void downloadMoney(List<Banknote> banknotes) throws ATMException {
        if(banknotes.isEmpty()){
            throw new ATMException("Вы ничего не внесли");
        }
        validateBanknotes(banknotes);
        handleBanknotes(banknotes);

    }

    private void validateBanknotes(List<Banknote> banknotes) throws ATMException {
        Map<Nominal, Cell> cellMap = mappingCellToMapNominalCell();

        Set<Nominal> missingNominals = banknotes.stream()
                .map(Banknote::nominal)
                .filter(nominal -> !cellMap.containsKey(nominal))
                .collect(Collectors.toSet());

        if (!missingNominals.isEmpty()) {
            throw new ATMException("Отсутствуют ячейки для номиналов: " + missingNominals);
        }
    }

    private void handleBanknotes(List<Banknote> banknotes) throws ATMException {
        Map<Nominal, Cell> cellMap = mappingCellToMapNominalCell();

        Map<Nominal, List<Banknote>> groupedBanknotes = banknotes.stream()
                .collect(Collectors.groupingBy(Banknote::nominal));

        System.out.println("Banknotes to process: " + banknotes.size());
        System.out.println("Grouped banknotes keys: " + groupedBanknotes.keySet());
        System.out.println("Cell map keys: " + cellMap.keySet());

        for (Map.Entry<Nominal, List<Banknote>> entry : groupedBanknotes.entrySet()) {
            Nominal nominal = entry.getKey();
            List<Banknote> nominalBanknotes = entry.getValue();
            Cell cell = cellMap.get(nominal);

            try {
                for (Banknote banknote : nominalBanknotes) {
                    cell.addBanknote(banknote);
                }
            } catch (Exception e) {
                throw new ATMException("Ошибка при добавлении банкнот номинала " + nominal, e);
            }
        }
    }

    private Map<Nominal, Cell> mappingCellToMapNominalCell() {

        return this.cells.stream()
                .collect(Collectors.toMap(Cell::getNominal, Function.identity()));
    }


}

