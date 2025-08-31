package ru.calculator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    // !!! эта коллекция должна остаться. Заменять ее на счетчик нельзя.
    private final List<Data> listValues = new ArrayList<>();


    // !!! сигнатуру метода менять нельзя
    public void calc(Data data) {

        int dataValue = data.value();
        listValues.add(data);
        if (listValues.size() == 100_000) {
            listValues.clear();
        }
        sum += dataValue + ThreadLocalRandom.current().nextInt();

        sumLastThreeValues = dataValue + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = dataValue;
        int denominator = dataValue + 1;
        int sumSq = sumLastThreeValues * sumLastThreeValues;
        int temp = sumSq / denominator - sum;
        int size = listValues.size();
        for (var idx = 0; idx < 3; idx++) {
            someValue += temp;
            someValue = Math.abs(someValue) + size;
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
        return someValue;
    }
}
