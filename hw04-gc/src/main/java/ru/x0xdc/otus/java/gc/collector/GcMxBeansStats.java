package ru.x0xdc.otus.java.gc.collector;

import java.lang.management.ManagementFactory;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;

/**
 * Сборщик статистики по счетчикам {@link java.lang.management.GarbageCollectorMXBean}, для отладки и контроля собственных подсчетов
 */
class GcMxBeansStats {

    private Map<String, GcGenerationStats> first, last;

    public GcMxBeansStats() {
    }

    public void open() {
        first = getGcCollectionCounters();
        last = first;
    }

    public void close() {
        last = getGcCollectionCounters();
    }

    private Map<String, GcGenerationStats> getGcCollectionCounters() {
        return ManagementFactory.getGarbageCollectorMXBeans()
                .stream()
                .map(gcbean -> new GcGenerationStatsImpl(gcbean.getName(), gcbean.getCollectionCount(), gcbean.getCollectionTime(), 0, 0))
                .collect(toUnmodifiableMap(GcGenerationStats::getName, identity()));
    }

    public void dump() {
        System.out.println("GC stats from getGarbageCollectorMXBeans():");
        last.values().stream()
                .sorted(Comparator.comparing(GcGenerationStats::getName))
                .forEach(stats -> {
                    var startStats = first.get(stats.getName());
                    long count = stats.getCollectionCount() - startStats.getCollectionCount();
                    long time = stats.getCollectionTimeMs() - startStats.getCollectionTimeMs();
                    System.out.printf(Locale.ROOT, "GC: %-20s Collections: %3d; time: %5d ms, avg: %5.2f ms%n",
                            stats.getName() + ";",
                            count, time,
                            count != 0 ? time / (double)count : 0
                    );
                });
    }

}
