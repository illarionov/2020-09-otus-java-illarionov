package ru.x0xdc.otus.java.testrunner;

import ru.x0xdc.otus.java.testrunner.annotations.After;
import ru.x0xdc.otus.java.testrunner.annotations.Before;
import ru.x0xdc.otus.java.testrunner.annotations.Test;
import ru.x0xdc.otus.java.testrunner.runner.TestRunner;

public class Main {

    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        String className = args.length > 0 ? args[0] : TestClass.class.getName();

        var result = runner.run(className);

        System.out.printf("%n%1$d tests completed, %2$d succeeded, %3$d failed%n", result.getTotal(),
                result.getSucceeded(), result.getFailed());
    }

    public static class TestClass {

        int state;

        @Before
        void before1() {
            System.out.println("before1");
        }

        @Before
        void before2() {
            System.out.println("before2");
        }

        @Test
        void test1() {
            System.out.println("test1");
            state = 1;
        }

        @Test
        void test2() {
            System.out.println("test2");
            if (state != 0) throw new IllegalStateException();
        }

        @Test
        void test3() {
            System.out.println("test3");
            throw new RuntimeException();
        }

        @After
        void after1() {
            System.out.println("after1");
        }

        @After
        void after2() {
            System.out.println("after2");
        }
    }
}
