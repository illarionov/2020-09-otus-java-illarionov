package ru.x0xdc.otus.java.testrunner.runner;

import ru.x0xdc.otus.java.testrunner.annotations.After;
import ru.x0xdc.otus.java.testrunner.annotations.Before;
import ru.x0xdc.otus.java.testrunner.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {

    private Class<?> clazz;
    private List<Method> beforeMethods;
    private List<Method> afterMethods;
    private List<Method> testMethods;

    private Map<Method, TestResult> result;

    public TestRunner() {
    }

    public TestSuiteResult run(String className) {
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException("Can not find class `" + className + "`", e);
        }

        resolveAnnotatedMethods();
        runTests();

        return new TestSuiteResult(result.size(),
                (int)result.values().stream().filter(TestResult::isFailed).count());
    }

    private void resolveAnnotatedMethods() {
        beforeMethods = new ArrayList<>();
        afterMethods = new ArrayList<>();
        testMethods = new ArrayList<>();

        for (Method method: clazz.getDeclaredMethods()) {
            if (!isNonPrivateVoidMethodWithNoParameters(method)) {
                continue;
            }

            if (method.getDeclaredAnnotation(Before.class) != null) {
                method.setAccessible(true);
                beforeMethods.add(method);
            }
            if (method.getDeclaredAnnotation(After.class) != null) {
                method.setAccessible(true);
                afterMethods.add(method);
            }
            if (method.getDeclaredAnnotation(Test.class) != null) {
                method.setAccessible(true);
                testMethods.add(method);
            }
        }
    }

    private static boolean isNonPrivateVoidMethodWithNoParameters(Method method) {
        if (!Void.TYPE.equals(method.getReturnType())) {
            return false;
        }

        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)
                || Modifier.isAbstract(modifiers)
                || Modifier.isPrivate(modifiers)) {
            return false;
        }

        //noinspection RedundantIfStatement
        if (method.getParameterCount() != 0) {
            return false;
        }
        
        return true;
    }

    private void runTests() {
        result = new LinkedHashMap<>();

        for (Method method: testMethods) {
            TestResult t = runTest(method);
            result.put(method, t);
            reportStatus(method, t);
        }
    }

    private TestResult runTest(Method testMethod) throws InstantiationRuntimeException {
        Object instance;
        boolean beforeFailed = false;
        List<Throwable> exceptions = new ArrayList<>();

        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new InstantiationRuntimeException("Failed to instantiate test method", e);
        }

        for (Method beforeMethod: beforeMethods) {
            Throwable e = invokeMethod(beforeMethod, instance);
            if (e != null) {
                exceptions.add(e);
                // исключение в @Before останавливает выполнение остальных @Before и самого теста, но не @After
                beforeFailed = true;
                break;
            }
        }

        if (!beforeFailed) {
            Throwable e = invokeMethod(testMethod, instance);
            if (e != null) exceptions.add(e);
        }

        for (Method afterMethod: afterMethods) {
            Throwable e = invokeMethod(afterMethod, instance);
            if (e != null) exceptions.add(e);
        }

        Throwable result;
        if (!exceptions.isEmpty()) {
            result = exceptions.get(0);
            for (Throwable e: exceptions.subList(1, exceptions.size())) {
                result.addSuppressed(e);
            }
            return TestResult.failed(result);
        } else {
            return TestResult.succeeded();
        }
    }

    private Throwable invokeMethod(Method method, Object instance) {
        Throwable result = null;
        try {
            method.invoke(instance);
        } catch (Throwable e) {
            result = e instanceof InvocationTargetException ? e.getCause() : e;
        }
        return result;
    }

    private void reportStatus(Method method, TestResult result) {
        System.out.printf("%n%1$s > %2$s() %3$s%n", clazz.getSimpleName(), method.getName(),
                result.getStatus());
        if (result.isFailed()) {
            result.getError().printStackTrace(System.out);
        }
        System.out.println();
    }

    public static class ClassNotFoundRuntimeException extends RuntimeException {

        public ClassNotFoundRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class InstantiationRuntimeException extends RuntimeException {

        public InstantiationRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }

    }
}
