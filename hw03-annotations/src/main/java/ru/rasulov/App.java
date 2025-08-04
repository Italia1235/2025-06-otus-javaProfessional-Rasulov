package ru.rasulov;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) throws ClassNotFoundException {
        TestService testHandler = new TestService();
        int totalTests = 0;
        int failedTests = 0;

        Map<TestService.MethodType, List<Method>> testMethods = testHandler.handleTests(FakeTest.class.getName());
        List<Method> tests = testMethods.get(TestService.MethodType.TEST);
        totalTests = tests.size();

        for (Method testMethod : tests) {
            boolean testFailed = false;
            FakeTest testInstance = new FakeTest();

            for (Method beforeMethod : testMethods.get(TestService.MethodType.BEFORE)) {
                try {
                    beforeMethod.invoke(testInstance);
                } catch (Exception e) {
                    throw new RuntimeException("Before Block crush", e);
                }
            }

                try {
                    testMethod.invoke(testInstance);
                } catch (Exception e) {
                    testFailed = true;

                }


            for (Method afterMethod : testMethods.get(TestService.MethodType.AFTER)) {
                try {
                    afterMethod.invoke(testInstance);
                } catch (Exception e) {
                    throw new RuntimeException("After Block crush", e);
                }
            }

            if (testFailed) {
                failedTests++;
            }
        }

        int successfulTests = totalTests - failedTests;

        System.out.println("-----------------------");
        System.out.println("Total tests: " + totalTests);
        System.out.println("Failed tests: " + failedTests);
        System.out.println("Successful tests: " + successfulTests);
    }
}
