package ru.rasulov;

import ru.rasulov.annotations.After;
import ru.rasulov.annotations.Before;
import ru.rasulov.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

class TestService {
    enum MethodType {
        BEFORE, TEST, AFTER
    }

    public Map<MethodType, List<Method>> handleTests(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        Method[] methods = clazz.getDeclaredMethods();

        Map<MethodType, List<Method>> methodMap = new EnumMap<>(MethodType.class);
        methodMap.put(MethodType.BEFORE, new ArrayList<>());
        methodMap.put(MethodType.TEST, new ArrayList<>());
        methodMap.put(MethodType.AFTER, new ArrayList<>());

        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                methodMap.get(MethodType.BEFORE).add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                methodMap.get(MethodType.TEST).add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                methodMap.get(MethodType.AFTER).add(method);
            }
        }
        return methodMap;
    }
}