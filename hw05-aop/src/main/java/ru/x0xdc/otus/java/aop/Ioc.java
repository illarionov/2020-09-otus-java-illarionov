package ru.x0xdc.otus.java.aop;

import ru.x0xdc.otus.java.aop.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Stream;

public class Ioc {

    private static volatile Ioc instance;

    public static Ioc getInstance() {
        if (instance == null) {
            synchronized (Ioc.class) {
                if (instance == null) {
                    instance = new Ioc();
                }
            }
        }
        return instance;
    }

    private Ioc() {
    }

    public TestLogging createTestLogging() {
        return proxy(new TestLoggingImpl());
    }

    @SuppressWarnings("unchecked")
    <T> T proxy(T implementation) {
        LoggingInvocationHandler<T> handler = new LoggingInvocationHandler<>(implementation);
        return (T) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                implementation.getClass().getInterfaces(), handler);
    }

    private static class LoggingInvocationHandler<T> implements InvocationHandler {

        private final T source;

        private final Map<String,  Set<List<Class<?>>>> methods;

        LoggingInvocationHandler(T source) {
            this.source = source;
            this.methods = getMethodsAnnotatedWithLog(source);
        }

        private static Map<String, Set<List<Class<?>>>> getMethodsAnnotatedWithLog(Object source) {
            // Логируем, если аннотация @Log проставлена на методе какого-либо из имплементируемых интерфейсов,
            // либо на методе самого объекта
            return Stream.concat(
                    Stream.of(source.getClass()),
                    Stream.of(source.getClass().getInterfaces()))
                    .flatMap(intf -> Arrays.stream(intf.getDeclaredMethods()))
                    .filter(m -> m.isAnnotationPresent(Log.class))
                    .collect(HashMap::new, (map, method) -> {
                        map.compute(method.getName(), (name, setOfArgLists) -> {
                            if (setOfArgLists == null) setOfArgLists = new HashSet<>();
                            setOfArgLists.add(Arrays.asList(method.getParameterTypes()));
                            return setOfArgLists;
                        });
                    }, (dstMap, srcMap) -> {
                        srcMap.forEach((key, value) -> dstMap.compute(key, (s, setOfArgLists) -> {
                            if (setOfArgLists == null) setOfArgLists = new HashSet<>();
                            setOfArgLists.addAll(value);
                            return setOfArgLists;
                        }));
                    });
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if (methods.containsKey(methodName)
                    && methods.get(methodName).contains(Arrays.asList(method.getParameterTypes()))) {
                StringBuilder message = new StringBuilder("Executed method: ").append(method.getName());
                if (args != null && args.length != 0) {
                    message.append(", params: [");
                    boolean isFirst = true;
                    for (Object arg: args) {
                        if (isFirst) {
                            isFirst = false;
                        } else {
                            message.append(", ");
                        }
                        message.append(arg);
                    }
                    message.append("]");
                }
                System.out.println(message);
            }
            return method.invoke(source, args);
        }
    }
}
