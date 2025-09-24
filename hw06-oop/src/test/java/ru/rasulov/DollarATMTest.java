package ru.rasulov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rasulov.exception.ATMException;
import ru.rasulov.interfaces.ATM;
import ru.rasulov.interfaces.SettingsATM;
import ru.rasulov.model.Banknote;
import ru.rasulov.model.Nominal;
import ru.rasulov.service.ATMFabricImp;
import ru.rasulov.service.CellFactory;
import ru.rasulov.service.DollarATM;
import ru.rasulov.service.SettingsATMimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DollarATMTest {
    ATM atm;

    @BeforeEach
    void createATM() throws ATMException {
        SettingsATM settingsATM = new SettingsATMimpl(List.of(
                CellFactory.createCell(Nominal.HUNDRED, 20),
                CellFactory.createCell(Nominal.FIFTY, 50)
        ));
        atm = new ATMFabricImp().createATM(settingsATM);
        var listHudredBanknotes = createListOfBanknotes(Nominal.HUNDRED, 10);
        var listFiftyBankontes = createListOfBanknotes(Nominal.FIFTY, 20);
        List<Banknote> allList = Stream.concat(listHudredBanknotes.stream(), listFiftyBankontes.stream())
                .collect(Collectors.toList());
        atm.downloadMoney(allList);

    }


    @Test
    public void correctObjectATMTest() {
        assertInstanceOf(DollarATM.class, atm, "Объект должен быть экземпляром DollarsATM");
        assertEquals(atm.getCells().size(), 2, "У объекта должно быть 2 ячейки");
    }

    @Test
    public void correctCellsLogicTest() {
        assertEquals(atm.getCells().getFirst().getNominal(), Nominal.HUNDRED);
        assertEquals(atm.getCells().getFirst().getCapacity(), 20);
        assertEquals(atm.getCells().getFirst().getCount(), 10);
        assertEquals(atm.getCells().getFirst().calculateSum(), 1000);
    }

    private List<Banknote> createListOfBanknotes(Nominal nominal, int size) {
        var banknotes = new ArrayList<Banknote>();
        for (int i = 0; i < size; i++) {
            banknotes.add(new Banknote(nominal));
        }
        return banknotes;
    }


    @Test
    public void withDrawMoneyTestWithCorrectDataTest() throws ATMException {
        assertEquals(atm.getBalance(), 2000, "Баланс на старте равен 2000");
        var money = atm.withDrawMoney(200);
        assertEquals(money.size(), 2);
        assertTrue(money.stream().allMatch(b -> b instanceof Banknote),
                "Все элементы должны быть Banknote");
        assertTrue(money.stream().allMatch(b -> b.nominal().getValue() == 100),
                "Все элементы должны быть Banknote");

        assertEquals(atm.getBalance(), 1800, "После снятия 200 $ должно быть 1800");
    }

    @Test
    public void withDrawMoneyTestWithUncorrectDataTest() throws ATMException {
        Exception exceptionBalance = assertThrowsExactly(ATMException.class,
                () -> atm.withDrawMoney(200_000_000));
        assertEquals("Запрошенная сумма превышает баланс банкомата", exceptionBalance.getMessage());

        Exception correctAmountForBanknote = assertThrowsExactly(ATMException.class, () -> atm.withDrawMoney(101));
        assertEquals("Невозможно выдать запрошенную сумму из-за несоответствия номиналов", correctAmountForBanknote.getMessage());

        assertThrowsExactly(ATMException.class, () -> atm.withDrawMoney(125));
        assertThrowsExactly(ATMException.class, () -> atm.withDrawMoney(1003));

    }

}
