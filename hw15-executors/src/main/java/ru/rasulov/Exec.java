package ru.rasulov;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Exec {
    private static final Logger logger = LoggerFactory.getLogger(Exec.class);
    private int MIN = 0;
    private int MAX = 10;
    private static boolean ascending = true;

    private long STARTER_ID = 1L;
    private long wait_ID = 2L;
    private int counter = 0;


    public static void main(String[] args) {
        Exec ex = new Exec();
        new Thread(() -> ex.action(1), "Thread-1").start();
        new Thread(() -> ex.action(2), "Thread-2").start();
    }

    private synchronized void action(int id) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (id == wait_ID) {
                    this.wait();
                }
                if (id == STARTER_ID) {
                    changeCounter();
                }
                logger.info("{}: {}", Thread.currentThread().getName(), counter);
                wait_ID = id;
                sleep();
                notifyAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void changeCounter() {
        if (ascending) {
            counter++;
            ascending = counter != MAX;
        } else {
            counter--;
            ascending = counter == MIN;
        }
    }
}
