package ru.rasulov;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class TestRunner {

    public void runTests(String className) throws Exception {
        TestService testService = new TestService();
        Map<TestService.MethodType, List<Method>> methods = testService.handleTests(className);

        TestResults results = executeAllTests(className, methods);
        printReport(results);
    }

    private TestResults executeAllTests(String className,
                                        Map<TestService.MethodType, List<Method>> methods) throws Exception {
        List<Method> tests = methods.get(TestService.MethodType.TEST);
        int total = tests.size();
        int failed = 0;

        for (Method test : tests) {
            if (runSingleTest(className, methods, test)) {
                failed++;
            }
        }

        return new TestResults(total, failed);
    }

    private boolean runSingleTest(String className,
                                  Map<TestService.MethodType, List<Method>> methods,
                                  Method test) throws Exception {
        Object testInstance = createTestInstance(className);

        try {
            runMethods(methods.get(TestService.MethodType.BEFORE), testInstance);
            runTestMethod(test, testInstance);
            return false;
        } catch (Exception e) {
            return true;
        } finally {
            runMethods(methods.get(TestService.MethodType.AFTER), testInstance);
        }
    }

    private Object createTestInstance(String className) throws Exception {
        return Class.forName(className).getDeclaredConstructor().newInstance();
    }

    private void runMethods(List<Method> methods, Object instance) throws Exception {
        if (methods == null) return;

        for (Method method : methods) {
            method.invoke(instance);
        }
    }

    private void runTestMethod(Method test, Object instance) throws Exception {
        try {
            test.invoke(instance);
        } catch (Exception e) {
            throw new TestFailureException(e);
        }
    }

    private void printReport(TestResults results) {
        System.out.println("-----------------------");
        System.out.println("Total tests: " + results.total());
        System.out.println("Failed tests: " + results.failed());
        System.out.println("Successful tests: " + (results.total() - results.failed()));
    }


    private record TestResults(int total, int failed) {}
    private static class TestFailureException extends Exception {
        public TestFailureException(Throwable cause) {
            super(cause);
        }
    }
}