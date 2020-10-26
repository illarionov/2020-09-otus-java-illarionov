package ru.x0xdc.otus.java.testrunner.annotations;

import java.lang.annotation.*;


/**
 * {@code @After} используется для обозначения методов, выполняемых после каждого теста.
 * <br>
 * Методы, помеченные данной аннотацией, выполняются после каждого теста (метода, помеченного аннотацией {@code @Test}).
 * <br>
 *
 * {@code @After} метод не должен быть статическим или приватным, не должен возвращать значения и у него не должно быть параметров.
 *
 * @see Before
 * @see Test
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface After {
}
