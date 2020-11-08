package ru.x0xdc.otus.java.aop;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.x0xdc.otus.java.aop.annotations.Log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class IocTest {

    private final Ioc ioc = Ioc.getInstance();

    private ByteArrayOutputStream consoleOut;

    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        consoleOut = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(consoleOut));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    static class TestArgs<T> {
        final String name;
        final Consumer<T> method;
        final String expectedOutput;

        TestArgs(String name, Consumer<T> method, String expectedOutput) {
            this.name = name;
            this.method = method;
            this.expectedOutput = expectedOutput;
        }

        @Override
        public String toString() {
            return "Testing " + name + " expecting output: " + expectedOutput;
        }
    }

    @DisplayName("Методы, помеченные @Log должны корректно логироваться в консоль")
    @ParameterizedTest()
    @MethodSource("shouldLogOnAnnotatedMethodsProvider")
    void shouldLogOnAnnotatedMethods(TestArgs<TestLogging> args) {
        TestLogging tl = ioc.createTestLogging();
        args.method.accept(tl);
        assertThat(consoleOut.toString())
                .isEqualToIgnoringNewLines(args.expectedOutput);
    }

    static Stream<TestArgs<TestLogging>> shouldLogOnAnnotatedMethodsProvider() {
        return Stream.of(
                new TestArgs<>("calculation()", TestLogging::calculation, "Executed method: calculation"),
                new TestArgs<>("calculation(1)", l -> l.calculation(1), "Executed method: calculation, params: [1]"),
                new TestArgs<>("calculation(2,3)", l -> l.calculation(2, 3), "Executed method: calculation, params: [2, 3]"),
                new TestArgs<>("calculation(4,5,6)", l -> l.calculation(4, 5, "6"), "Executed method: calculation, params: [4, 5, 6]"),
                new TestArgs<>("calculation(new int[]{7})", l -> l.calculation(new int[]{7}), ""));
    }



     interface InterfaceWithLogMethod1 {
        @Log void method();
        void method(int arg1);
        void method(int arg1, int arg2);
        void method(int arg1, int arg2, int arg3);
     }

    interface InterfaceWithLogMethod2 {
        void method();
        @Log void method(int arg1);
    }

    static class ShouldLogBothOnInterfaceAndImplementationImpl implements InterfaceWithLogMethod1, InterfaceWithLogMethod2 {
        @Override public void method() { }
        @Override public void method(int arg1) { }
        @Log @Override public void method(int arg1, int arg2) {  }
        @Override public void method(int arg1, int arg2, int arg3) {}
    }

    @DisplayName("Методы, помеченные @Log как у интерфейса, так и у класса, должны корректно логироваться в консоль")
    @ParameterizedTest()
    @MethodSource("shouldLogBothOnInterfaceAndImplementationProvider")
    void shouldLogBothOnInterfaceAndImplementation(TestArgs<InterfaceWithLogMethod1> args) {
        InterfaceWithLogMethod1 tl = ioc.proxy(new ShouldLogBothOnInterfaceAndImplementationImpl());
        args.method.accept(tl);
        assertThat(consoleOut.toString())
                .isEqualToIgnoringNewLines(args.expectedOutput);
    }

    static Stream<TestArgs<InterfaceWithLogMethod1>> shouldLogBothOnInterfaceAndImplementationProvider() {
        return Stream.of(
                new TestArgs<>("method()", l -> l.method(), "Executed method: method"),
                new TestArgs<>("method(1)", l -> l.method(1), "Executed method: method, params: [1]"),
                new TestArgs<>("method(2,3)", l -> l.method(2,3), "Executed method: method, params: [2, 3]"),
                new TestArgs<>("method(2,3,4)", l -> l.method(2,3,4), "")
        );
    }


    interface SimpleInterfaceWithMethod {
        @Log int method();
    }

    static class SimpleInterfaceWithMethodImpl implements SimpleInterfaceWithMethod {
        @Override public int method() { return 42; }
    }

    @Test
    void originalMethodShouldBeCalled() {
        SimpleInterfaceWithMethod testClass = ioc.proxy(new SimpleInterfaceWithMethodImpl());

        int result = testClass.method();

        assertThat(result).isEqualTo(42);
        assertThat(consoleOut.toString()).isEqualToIgnoringNewLines("Executed method: method");
    }


    interface SimpleInterface1 {
        @Log void method();
    }

    interface SimpleInterface2 {}

    class ClassWithTwoInterfaces implements SimpleInterface1, SimpleInterface2 {
        @Override public void method() { }
    }

    @Test
    @DisplayName("Запроксированный объект должен имплементировать все интерфейсы исходного")
    void proxyShouldImplementAllInterfaces() {
        ClassWithTwoInterfaces source = new ClassWithTwoInterfaces();
        SimpleInterface1 proxy = ioc.proxy(source);
        assertThat(proxy).isInstanceOf(SimpleInterface2.class);
    }
}