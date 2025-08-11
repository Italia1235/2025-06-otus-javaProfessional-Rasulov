package ru.rasulov;

public class App {
    public static void main(String[] args) throws Exception {
        new TestRunner().runTests(FakeTest.class.getName());
    }
}