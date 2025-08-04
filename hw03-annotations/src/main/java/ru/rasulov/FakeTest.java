package ru.rasulov;

import ru.rasulov.annotations.After;
import ru.rasulov.annotations.Before;
import ru.rasulov.annotations.Test;

public class FakeTest {

    @Before
    public void testFirstBefore() {
        System.out.println("First before");
    }


    @Before
    public void testSecondBefore() {
        System.out.println("Second before");
    }

    @Test
    public void firstTest() {

        System.out.println("First Test");
    }

    @Test
    public void secondTest() {

        System.out.println("Second Test");
    }

    @Test
    public void testWithException() {
        throw new RuntimeException("Critical Error");
    }


    @After
    public void firstAfter() {

        System.out.println("First After");
    }

    @After
    public void secondAfter() {

        System.out.println("First After");
    }
}
