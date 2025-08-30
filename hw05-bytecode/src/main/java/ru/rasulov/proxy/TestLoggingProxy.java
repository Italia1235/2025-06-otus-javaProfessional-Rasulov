package ru.rasulov.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rasulov.proxy.annotations.Log;
import ru.rasulov.serivce.TestLogging;
import ru.rasulov.serivce.TestLoggingInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TestLoggingProxy {
    private static final Logger logger = LoggerFactory.getLogger(TestLoggingProxy.class);

    private TestLoggingProxy() {
    }

    static public TestLoggingInterface createMyClass() {
        InvocationHandler handler = new TestLoggingHandler(new TestLogging());
        return (TestLoggingInterface)
                Proxy.newProxyInstance(TestLoggingProxy.class.getClassLoader(), new Class<?>[]{TestLoggingInterface.class}, handler);
    }


    static class TestLoggingHandler implements InvocationHandler {

        private final TestLoggingInterface myClass;
        private final Map<String, Boolean> methodLoggingFlags;

        TestLoggingHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
            this.methodLoggingFlags = new HashMap<>();
            initializeLoggingFlags();
        }

        private void initializeLoggingFlags() {
            Class<?> clazz = myClass.getClass();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Log.class)) {
                    String methodSignature = getMethodSignature(method);
                    methodLoggingFlags.put(methodSignature, true);
                }
            }
        }

        private String getMethodSignature(Method method) {
            return method.getName() + Arrays.toString(method.getParameterTypes());
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodSignature = getMethodSignature(method);
            if (methodLoggingFlags.containsKey(methodSignature)) {
                String argsString = args != null ?
                        Arrays.stream(args)
                                .map(arg -> arg != null ? arg.toString() : "null")
                                .collect(Collectors.joining(", ")) : "";

                logger.info("executed method: {}, args: [{}]", method.getName(), argsString);
            }

            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "TestLoggingHandler{" + "myClass=" + myClass + '}';
        }
    }
}
