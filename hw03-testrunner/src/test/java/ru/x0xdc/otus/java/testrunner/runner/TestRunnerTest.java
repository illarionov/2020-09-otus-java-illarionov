package ru.x0xdc.otus.java.testrunner.runner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.x0xdc.otus.java.testrunner.annotations.After;
import ru.x0xdc.otus.java.testrunner.annotations.Before;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Index.atIndex;

class TestRunnerTest {

    TestRunner testRunner;

    @BeforeEach
    void init() {
        testRunner = new TestRunner();
    }


    @Test
    @DisplayName("Аннотированные методы должны запускаться в порядке @Before, @Test, @After")
    void shouldExecuteAnnotatedMethodsInOrder() {
        TestClassCheckOrder.calledMethods.clear();

        TestSuiteResult result = testRunner.run(TestClassCheckOrder.class.getName());

        List<String> testMethods = new ArrayList<>(TestClassCheckOrder.calledMethods);

        assertThat(testMethods).hasSize(3*8);

        testSublist(testMethods.subList(0, 8));
        testSublist(testMethods.subList(8, 16));
        testSublist(testMethods.subList(16, 24));

        assertThat(List.of(testMethods.get(4), testMethods.get(12), testMethods.get(20)))
            .containsExactlyInAnyOrder("test1", "test2", "test3");

        assertThat(result)
                .isEqualTo(new TestSuiteResult(3, 0));
    }

    private void testSublist(List<String> methods) {
        assertThat(methods).startsWith("instantiate");
        assertThat(methods.subList(1, 4))
                .containsExactlyInAnyOrder("before1", "before2", "before3");
        assertThat(methods)
                .satisfies(str -> assertThat(str).matches("test\\d"), atIndex(4));
        assertThat(methods.subList(5, 8))
                .containsExactlyInAnyOrder("after1", "after2", "after3");
    }

    static class TestClassCheckOrder {
        public static List<String> calledMethods = new ArrayList<>();
        TestClassCheckOrder() { calledMethods.add("instantiate"); }

        @ru.x0xdc.otus.java.testrunner.annotations.Test void test1() {calledMethods.add("test1");}
        @ru.x0xdc.otus.java.testrunner.annotations.Test void test2() {calledMethods.add("test2");}
        @ru.x0xdc.otus.java.testrunner.annotations.Test void test3() {calledMethods.add("test3");}
        @After  void after1() { calledMethods.add("after1");  }
        @After  void after2() { calledMethods.add("after2");  }
        @After  void after3() { calledMethods.add("after3");  }
        @Before void before1() { calledMethods.add("before1");  }
        @Before void before2() { calledMethods.add("before2");  }
        @Before void before3() { calledMethods.add("before3");  }
    }


    @Test
    @DisplayName("Должен выбрасывать исключение для несуществующих классов")
    void shouldFailForNonExistentClasses() {
        assertThatThrownBy(() -> testRunner.run("NonExistentClass"))
                .isInstanceOf(TestRunner.ClassNotFoundRuntimeException.class);
    }


    @Test
    @DisplayName("Должен игнорировать приватные, статические, методы с аргументами или возвращаемым значением")
    void shouldIgnorePrivateMethods() {
        TestClassWithPrivateMethods.calledMethods.clear();

        TestSuiteResult result = testRunner.run(TestClassWithPrivateMethods.class.getName());

        assertThat(TestClassWithPrivateMethods.calledMethods).isEmpty();
        assertThat(result).isEqualTo(new TestSuiteResult(0, 0));
    }

    static class TestClassWithPrivateMethods {
        public static List<String> calledMethods = new ArrayList<>();
        @ru.x0xdc.otus.java.testrunner.annotations.Test private void test1() {calledMethods.add("test1");}
        @ru.x0xdc.otus.java.testrunner.annotations.Test static void test2() {calledMethods.add("test2");}
        @ru.x0xdc.otus.java.testrunner.annotations.Test void test3(int param1) {calledMethods.add("test3");}
        @ru.x0xdc.otus.java.testrunner.annotations.Test int test4() {calledMethods.add("test4"); return 0;}
    }


    @Test
    @DisplayName("Должен выбрасывать исключение на абстрактных классах")
    void shouldFailOnAbstractClasses() {
        assertThatThrownBy(() -> testRunner.run(TestAbstractClass.class.getName()))
                .isInstanceOf(TestRunner.InstantiationRuntimeException.class);
    }

    static abstract class TestAbstractClass {
        @ru.x0xdc.otus.java.testrunner.annotations.Test public void test1() {}
        @ru.x0xdc.otus.java.testrunner.annotations.Test public abstract void test2();
    }


    @Test
    @DisplayName("Должен выбрасывать исключение на классе, у которого нет конструктора без аргументов")
    void shouldFailOnTestsWithoutEmptyConstructor() {
        assertThatThrownBy(() -> testRunner.run(TestClassNoEmptyConstructor.class.getName()))
                .isInstanceOf(TestRunner.InstantiationRuntimeException.class);
    }

    static class TestClassNoEmptyConstructor {
        public TestClassNoEmptyConstructor(int unused) {}
        @ru.x0xdc.otus.java.testrunner.annotations.Test public void test1() {}
    }


    @Test
    @DisplayName("Должен работать с приватными конструкторами")
    void shouldPassOnPrivateConstructor() {
        TestSuiteResult result = testRunner.run(TestClassPrivateConstructor.class.getName());
        assertThat(result)
                .isEqualTo(new TestSuiteResult(1, 0));
    }

    static class TestClassPrivateConstructor {
        private TestClassPrivateConstructor() {}
        @ru.x0xdc.otus.java.testrunner.annotations.Test public void test1() {}
    }


    @Test
    @DisplayName("Должен создавать отдельный объект тестируемого класса для каждой такой \"тройки\" @Before - @Test - @After")
    void shouldInstantiateObjectForEachTest() {
        TestSuiteResult result = testRunner.run(TestClassShouldInstantiateObjectForEachTest.class.getName());
        assertThat(result)
                .isEqualTo(new TestSuiteResult(2, 0));
    }

    @SuppressWarnings("PointlessBooleanExpression")
    static class TestClassShouldInstantiateObjectForEachTest {

        boolean stateBefore1, stateBefore2, stateTest1, stateTest2;

        @Before
        void before1() {
            stateBefore1 = true;
        }

        @Before
        void before2() {
            stateBefore2 = true;
        }

        @ru.x0xdc.otus.java.testrunner.annotations.Test
        void test1() {
            if (stateBefore1 != true) throw new IllegalStateException();
            if (stateBefore2 != true) throw new IllegalStateException();
            stateTest1 = true;
        }

        @ru.x0xdc.otus.java.testrunner.annotations.Test
        void test2() {
            if (stateBefore1 != true) throw new IllegalStateException();
            if (stateBefore2 != true) throw new IllegalStateException();
            if (stateTest1 == true) throw new IllegalStateException();
            stateTest2 = true;
        }

        @After
        void after1() {
            if (stateBefore1 != true) throw new IllegalStateException();
            if (stateBefore2 != true) throw new IllegalStateException();
            if (stateTest1 == false && stateTest2 == false) throw new IllegalStateException();
            if (stateTest1 == true && stateTest2 == true) throw new IllegalStateException();
        }
    }


    @Test
    @DisplayName("Провалившийся тест не должен останавливать выполнение остальных тестов")
    void failedTestShouldNotStopExecution() {
        TestClassFailedTestShouldNotStopExecution.calledMethods.clear();

        TestSuiteResult result = testRunner.run(TestClassFailedTestShouldNotStopExecution.class.getName());

        assertThat(result).isEqualTo(new TestSuiteResult(3, 1));
        assertThat(TestClassFailedTestShouldNotStopExecution.calledMethods)
                .containsExactlyInAnyOrder("test1", "test2", "test3");
    }

    static class TestClassFailedTestShouldNotStopExecution {
        public static List<String> calledMethods = new ArrayList<>();
        @ru.x0xdc.otus.java.testrunner.annotations.Test void test1() {calledMethods.add("test1");}
        @ru.x0xdc.otus.java.testrunner.annotations.Test void test2() {calledMethods.add("test2"); throw new RuntimeException();}
        @ru.x0xdc.otus.java.testrunner.annotations.Test void test3() {calledMethods.add("test3");}
    }


    @Test
    @DisplayName("При исключении в @Before тест должен провалиться")
    void exceptionInBeforeShouldFailTest() {
        TestClassExceptionInBeforeShouldFailTest.calledMethods.clear();

        TestSuiteResult result = testRunner.run(TestClassExceptionInBeforeShouldFailTest.class.getName());

        assertThat(result).isEqualTo(new TestSuiteResult(1, 1));

        assertThat(TestClassExceptionInBeforeShouldFailTest.calledMethods)
                .containsExactly("before1", "after");
    }

    static class TestClassExceptionInBeforeShouldFailTest {
        public static List<String> calledMethods = new ArrayList<>();

        boolean beforeCalled;

        @Before void before1() {
            beforeCalled = true;
            calledMethods.add("before1");
            throw new RuntimeException();
        }

        @Before void before2() {
            if (beforeCalled) {
                calledMethods.add("before2");
            }
        }

        @ru.x0xdc.otus.java.testrunner.annotations.Test
        void test() {
            calledMethods.add("test");
        }

        @After
        void after() {
            calledMethods.add("after");
        }
    }

    
    @Test
    @DisplayName("Исключение в тесте или одном из @After не должно прервать выполнение остальных @After")
    void exceptionInTestOrAfterShouldNotStopAfter() {
        TestClassExceptionInTestOrAfterShouldNotStopAfter.calledMethods.clear();

        TestSuiteResult result = testRunner.run(TestClassExceptionInTestOrAfterShouldNotStopAfter.class.getName());

        assertThat(result).isEqualTo(new TestSuiteResult(1, 1));

        assertThat(TestClassExceptionInTestOrAfterShouldNotStopAfter.calledMethods)
                .startsWith("test")
                .containsExactlyInAnyOrder("test", "after1", "after2");
    }

    static class TestClassExceptionInTestOrAfterShouldNotStopAfter {
        public static List<String> calledMethods = new ArrayList<>();

        @ru.x0xdc.otus.java.testrunner.annotations.Test
        void test() { calledMethods.add("test"); throw new RuntimeException(); }

        @After void after1() { calledMethods.add("after1"); throw new RuntimeException(); }
        @After void after2() { calledMethods.add("after2"); throw new RuntimeException(); }
    }
    
}