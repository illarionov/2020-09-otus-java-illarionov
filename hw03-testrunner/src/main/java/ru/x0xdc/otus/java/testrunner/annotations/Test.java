package ru.x0xdc.otus.java.testrunner.annotations;

import java.lang.annotation.*;

/**
 * {@code @Test} используется для обозначения тестовых методов
 * <br>
 *
 * {@code @Test} метод не должен быть статическим или приватным, не должен возвращать значения и у него не должно быть параметров.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
