package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {

    /**
     * Элемент был добавлен в кэш
     */
    public static final String ACTION_ELEMENT_ADDED = "ADDED";

    /**
     * Элемент обновлен в кэше
     */
    public static final String ACTION_ELEMENT_UPDATED = "UPDATED";

    /**
     * Элемент удален из кэша
     */
    public static final String ACTION_ELEMENT_REMOVED = "REMOVED";

    private final Map<K, V> cache = new WeakHashMap<>();

    private final List<WeakReference<HwListener<? super K, ? super V>>> listeners = new LinkedList<>();

    @Override
    public void put(K key, V value) {
        boolean hasValue = cache.containsKey(key);
        cache.put(key, value);
        notifyListeners(key, value, hasValue ? ACTION_ELEMENT_UPDATED : ACTION_ELEMENT_ADDED);
    }

    @Override
    public void remove(K key) {
        if (cache.containsKey(key)) {
            V oldValue = cache.remove(key);
            notifyListeners(key, oldValue, ACTION_ELEMENT_REMOVED);
        }
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<? super K, ? super V> listener) {
        Objects.requireNonNull(listener, "Listener should not be null");
        for (var reference: listeners) {
            if (listener.equals(reference.get())) {
                // слушать уже добавлен
                return;
            }
        }
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<? super K, ? super V> listenerToRemove) {
        for (Iterator<WeakReference<HwListener<? super K, ? super V>>> it =  listeners.iterator(); it.hasNext();) {
            WeakReference<HwListener<? super K, ? super V>> reference =  it.next();
            var listener = reference.get();
            if (listener == null || listener.equals(listenerToRemove)) {
                it.remove();
            }
        }
    }

    private void notifyListeners(K key, V value, String action) {
        for (Iterator<WeakReference<HwListener<? super K, ? super V>>> it =  listeners.iterator(); it.hasNext();) {
            WeakReference<HwListener<? super K, ? super V>> reference =  it.next();
            var listener = reference.get();
            if (listener != null) {
                listener.notify(key, value, action);
            } else {
                it.remove();
            }
        }
    }
}
