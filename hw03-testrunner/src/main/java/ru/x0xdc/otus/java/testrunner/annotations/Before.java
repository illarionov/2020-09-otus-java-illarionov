package ru.x0xdc.otus.java.testrunner.annotations;

import java.lang.annotation.*;

/**
 * {@code @Before} используется для обозначения методов, выполняемых перед каждым тестом.
 * <br>
 * Методы, помеченные данной аннотацией, выполняются перед каждым тестом (методом, помеченным аннотацией {@code @Test}).
 * <br>
 * {@code @Before} метод не должен быть статическим или приватным, не должен возвращать значения и у него не должно быть параметров.
 *
 * @see After
 * @see Test
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Before {
}

