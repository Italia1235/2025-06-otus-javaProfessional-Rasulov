package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();


    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);


        Object configInstance;
        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create config class instance", e);
        }

        List<Method> methods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method ->
                        method.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());


        checkUniqueComponentNames(methods);


        for (Method method : methods) {
            try {
                createComponent(method, configInstance);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create component: " + method.getName(), e);
            }
        }
    }

    private void checkUniqueComponentNames(List<Method> methods) {
        Set<String> componentNames = new HashSet<>();
        for (Method method : methods) {
            String componentName = method.getAnnotation(AppComponent.class).name();
            if (!componentNames.add(componentName)) {
                throw new RuntimeException("Duplicate component name found: " + componentName);
            }
        }
    }

    private void createComponent(Method method, Object configInstance)
            throws IllegalAccessException, InvocationTargetException {
        AppComponent annotation = method.getAnnotation(AppComponent.class);
        String componentName = annotation.name();


        if (appComponentsByName.containsKey(componentName)) {
            throw new RuntimeException("Component with name '" + componentName + "' already exists");
        }


        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];


        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Object dependency = getAppComponentInternal(parameterType);

            if (dependency == null) {
                throw new RuntimeException("Dependency not found: " + parameterType +
                        " for component: " + componentName);
            }
            arguments[i] = dependency;
        }


        Object componentInstance = method.invoke(configInstance, arguments);


        appComponents.add(componentInstance);
        appComponentsByName.put(componentName, componentInstance);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<C> candidates = appComponents.stream()
                .filter(componentClass::isInstance)
                .map(componentClass::cast)
                .toList();

        if (candidates.isEmpty()) {
            throw new RuntimeException("Component not found for class: " + componentClass.getName());
        }

        if (candidates.size() > 1) {
            throw new RuntimeException("Multiple components found for class: " + componentClass.getName() +
                    ". Use getAppComponent(String) to specify exact component.");
        }

        return candidates.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        if (!appComponentsByName.containsKey(componentName)) {
            throw new RuntimeException("Component not found: " + componentName);
        }
        return (C) appComponentsByName.get(componentName);
    }


    private <C> C getAppComponentInternal(Class<C> componentClass) {
        return appComponents.stream()
                .filter(componentClass::isInstance)
                .map(componentClass::cast)
                .findFirst()
                .orElse(null);
    }
}
