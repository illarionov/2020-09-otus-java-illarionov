package ru.otus.cachehw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MyCacheTest {

    @Test
    @DisplayName("Добавление элемента должно работать")
    void addShouldCacheValue() {
        HwCache<String, String> cache = new MyCache<>();

        String key = "key";
        String value = "value";

        cache.put(key, value);

        assertThat(cache.get(key))
                .isEqualTo(value);
    }

    @Test
    @DisplayName("Обновление элемента должно работать")
    void updateShouldWork() {
        HwCache<String, String> cache = new MyCache<>();

        String key = "key";
        String value1 = "value1";
        String value2 = "value2";

        cache.put(key, value1);
        cache.put(key, value2);

        assertThat(cache.get(key))
                .isEqualTo(value2);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Добавление нового элемента должно вызывать событие ADD")
    void addNewElementShouldTriggerAddedEvent() {
        HwCache<String, String> cache = new MyCache<>();
        HwListener<String, String> listener = mock(HwListener.class);
        cache.addListener(listener);

        cache.put("key", "value");
        Mockito.verify(listener).notify("key", "value", MyCache.ACTION_ELEMENT_ADDED);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Обновление элемента должно вызывать событие UPDATE")
    void updateShouldTriggerUpdateEvent() {
        HwCache<String, String> cache = new MyCache<>();
        HwListener<String, String> listener = mock(HwListener.class);

        cache.put("key", "value");
        cache.addListener(listener);

        cache.put("key", "value2");

        Mockito.verify(listener).notify("key", "value2", MyCache.ACTION_ELEMENT_UPDATED);
    }

    @Test
    @DisplayName("Удаление должно работать")
    void removeShouldRemoveElementFromCache() {
        HwCache<String, String> cache = new MyCache<>();

        String key = "key";
        String value = "value";

        cache.put(key, value);
        cache.remove(key);

        assertThat(cache.get(key))
                .isNull();
    }

    @Test
    @DisplayName("Удаление должно вызывать событие REMOVE")
    void removeShouldTriggerRemoveEvent() {
        HwCache<String, String> cache = new MyCache<>();
        HwListener<String, String> listener = mock(HwListener.class);

        cache.put("key", "value");
        cache.addListener(listener);

        cache.remove("key");

        Mockito.verify(listener).notify("key", "value", MyCache.ACTION_ELEMENT_REMOVED);
    }

    @Test
    @DisplayName("Добавление листенера должно работать")
    void testAddListener() {
        HwCache<String, String> cache = new MyCache<>();
        var listener = new HwListener<String, String>() {
            boolean notified = false;

            @Override
            public void notify(String key, String value, String action) {
                notified = true;
            }
        };

        cache.addListener(listener);
        cache.put("key", "value");

        assertThat(listener.notified)
                .isTrue();
    }

    @Test
    @DisplayName("Удаление листенера должно работать")
    void testRemoveListener() {
        HwCache<String, String> cache = new MyCache<>();
        var listener = new HwListener<String, String>() {
            int notifiedTimes = 0;

            @Override
            public void notify(String key, String value, String action) {
                notifiedTimes += 1;
            }
        };

        cache.addListener(listener);
        cache.put("key", "value");
        cache.removeListener(listener);
        cache.put("key2", "value");

        assertThat(listener.notifiedTimes)
                .isEqualTo(1);
    }

    @Test
    @DisplayName("Листенеры должны уведомляться в порядке добавления")
    void shouldNotifyInOrder() {
        HwCache<String, String> cache = new MyCache<>();
        List<Integer> notifications = new ArrayList<>();

        class Listener implements HwListener<String, String> {

            final int no;

            Listener(int no) {
                this.no = no;
            }


            @Override
            public void notify(String key, String value, String action) {
                notifications.add(no);
            }
        }

        var listener1 = new Listener(1);
        var listener2 = new Listener(2);
        var listener3 = new Listener(3);

        cache.addListener(listener1);
        cache.addListener(listener2);
        cache.addListener(listener3);

        cache.put("key", "value");

        assertThat(notifications)
                .containsExactly(1, 2, 3);
    }


    @Test
    @DisplayName("Кэш должен сбрасываться при недостатке памяти")
    void shouldBeClearedOnOutOfMemory() throws InterruptedException {
        HwCache<String, String> cache = new MyCache<>();

        cache.put("key", "value");

        System.gc();
        Thread.sleep(10);

        assertThat(cache.get("gey"))
                .isNull();
    }
}