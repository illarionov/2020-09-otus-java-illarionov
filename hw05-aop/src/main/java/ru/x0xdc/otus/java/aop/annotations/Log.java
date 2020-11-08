package ru.x0xdc.otus.java.aop.annotations;

import java.lang.annotation.*;

/**
 * {@code @Log} используется для обозначения методов, при выполнении которых в консоль будет логироваться имя
 * вызываемого метода и его параметры
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
}
