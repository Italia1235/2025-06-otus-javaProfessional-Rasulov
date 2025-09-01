package ru.rasulov;

import ru.rasulov.proxy.TestLoggingProxy;
import ru.rasulov.serivce.TestLoggingInterface;

public class Demo {

    public static void main(String[] args) {
        startDemo();
    }

    private static void startDemo() {
        TestLoggingInterface testLogging = TestLoggingProxy.createMyClass();
        handleTestDemo(testLogging);
    }

    private static void handleTestDemo(TestLoggingInterface testLogging) {
        testLogging.calculation(4, 5);
        testLogging.calculation(2, 4, 7);
        //without annotation
        testLogging.calculation("I love Java");
        testLogging.calculation(4);

    }
}
