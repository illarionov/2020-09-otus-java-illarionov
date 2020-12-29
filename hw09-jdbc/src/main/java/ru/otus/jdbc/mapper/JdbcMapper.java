package ru.otus.jdbc.mapper;

import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 * @param <T>
 */
public interface JdbcMapper<T, K> {
    K insert(T objectData);

    K update(T objectData);

    //K insertOrUpdate(T objectData);

    Optional<T> findById(K id);

    List<T> findAll();
}
