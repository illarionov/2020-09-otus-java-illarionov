package ru.otus.core.annnotations;

import java.lang.annotation.*;

/**
 * Аннотация {@linkplain Id} используется для обозначения первичного ключа сущности
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
    /**
     * Значение поля генерируется на сервере при вставке.
     */
    boolean autoGenerated() default true;
}
