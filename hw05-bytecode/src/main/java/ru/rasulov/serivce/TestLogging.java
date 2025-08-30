package ru.rasulov.serivce;

import ru.rasulov.proxy.annotations.Log;

public class TestLogging implements TestLoggingInterface{
    @Override
    public void calculation(int param) {

    }
    @Log
    @Override
    public void calculation(int param, int secondParam) {

    }

    @Override
    public void calculation(String param) {

    }
    @Log
    @Override
    public void calculation(int param, int secondParam, int thirdParam) {

    }
}
