package ru.rasulov;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rasulov.helper.ATMHelper;
import ru.rasulov.model.Banknote;
import ru.rasulov.model.Nominal;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankomateHelperTest {

    @Test
    @DisplayName("Корректный подсчет банктон")
    void countBanknotesValueTest() {
        var banknotesList = List.of(new Banknote(Nominal.FIFTY), new Banknote(Nominal.HUNDRED), new Banknote(Nominal.ONE));
        assertEquals(ATMHelper.countBanknotesValue(banknotesList), 151);
    }

    @Test
    @DisplayName("С пустым cписком = 0 ")
    void countBanknotesValueTestWithEmptyList() {
        assertEquals(ATMHelper.countBanknotesValue(List.of()), 0);
    }


}
