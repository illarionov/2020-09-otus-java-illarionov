package ru.otus.cachehw;

import java.util.function.Function;

/**
 * @author sergey
 * created on 14.12.18.
 */
public interface HwCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(HwListener<? super K, ? super V> listener);

    void removeListener(HwListener<? super K, ? super V> listener);

    default V computeIfAbsent(K key,
                              Function<? super K, ? extends V> mappingFunction) {
        V value = get(key);
        if (value != null) {
            return value;
        }

        V newValue = mappingFunction.apply(key);
        if (newValue != null) {
            put(key, newValue);
        }

        return newValue;
    }
}
