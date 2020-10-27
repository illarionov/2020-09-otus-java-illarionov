package ru.x0xdc.otus.java.gc.collector;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;

import static java.util.Map.Entry.comparingByKey;

/**
 * Накопленная статистика
 */
public class GcStats {

    /**
     * Время начала измерений по {@linkplain System#nanoTime()}
     */
    private long startTimeNanos;

    /**
     * Время окончания измерений по {@linkplain System#nanoTime()}
     */
    private long endTimeNanos;

    /**
     * Накопленная статистика: gcName -> gcCause -> {@link GcGenerationStats}
     */
    private Map<String, Map<String, GcGenerationStats>> stats;

    private final boolean trackMedian;

    GcStats() {
        this(false);
    }

    /**
     * @param trackMedian вычислять ли медианные значения времени GC
     */
    GcStats(boolean trackMedian) {
        this.trackMedian = trackMedian;
    }

    void open(long timestampNanos) {
        stats = new HashMap<>();
        ManagementFactory.getGarbageCollectorMXBeans()
                .stream()
                .map(GarbageCollectorMXBean::getName)
                .forEach(gcName -> stats.put(gcName, new HashMap<>()));
        endTimeNanos = 0;
        startTimeNanos = timestampNanos;
    }

    void close(long timestampNanos) {
        this.endTimeNanos = timestampNanos;
    }

    boolean isActive() {
        return startTimeNanos > 0 && endTimeNanos == 0;
    }

    public long getStartTimeNanos() {
        return startTimeNanos;
    }

    public long getEndTimeNanos() {
        return endTimeNanos;
    }

    public long getTotalTimeNanos() {
        return endTimeNanos - startTimeNanos;
    }

    void addCollectionEvent(GarbageCollectionNotificationInfo event) {
        String gcName = event.getGcName();
        String gcCause = event.getGcCause();
        GcInfo gcInfo = event.getGcInfo();
        long duration = gcInfo.getDuration();

        stats.get(gcName).compute(gcCause, (cause, stats) -> {
            if (stats == null) stats = createGenerationStats(gcName);
            stats.addGcEvent(duration);
            return stats;
        });
    }

    private GcGenerationStats createGenerationStats(String gcName) {
        return trackMedian ? new GcGenerationStatsWithMedian(gcName) : new GcGenerationStatsImpl(gcName);
    }

    public Set<String> getGcNames() {
        return stats.keySet();
    }

    public GcGenerationStats getStatsForGc(String gcName) {
        return stats.get(gcName)
                .values()
                .stream()
                .collect(() -> createGenerationStats(gcName), GcGenerationStats::addStats, GcGenerationStats::addStats);
    }

    public long getTotalCollections() {
        return stats.values()
                .stream()
                .flatMapToLong(map -> map.values().stream().mapToLong(GcGenerationStats::getCollectionCount))
                .sum();
    }

    public long getTotalGcTimeMs() {
        return stats.values()
                .stream()
                .flatMapToLong(map -> map.values().stream().mapToLong(GcGenerationStats::getCollectionTimeMs))
                .sum();
    }

    public void dump() {
        System.out.printf(Locale.ROOT, "Time: %.4f sec%n", getTotalTimeNanos() / 1000_000_000.0);
        System.out.println("Collected GC stats:");
        stats.entrySet().stream().sorted(comparingByKey()).forEach(entry -> {
            var gcName = entry.getKey();
            entry.getValue().entrySet().stream().sorted(comparingByKey()).forEach(subentry -> {
                dumpGc(gcName, subentry.getKey(), subentry.getValue());
            });

            if (stats.get(gcName).size() > 1) {
                GcGenerationStats total = getStatsForGc(gcName);
                dumpGc(gcName, "Total", total);
            }
        });
    }

    private void dumpGc(String gcName, String gcCause, GcGenerationStats stats) {
        long collectionsCount = stats.getCollectionCount();
        long time = stats.getCollectionTimeMs();
        String minAvgMax = formatMinAvgMaxTime(stats);

        System.out.printf(Locale.ROOT, "GC: %-50s Collections: %3d; time: %5d ms, %s%n",
                gcName + " (" + gcCause + ")" + ";",
                collectionsCount,
                time,
                minAvgMax
        );
    }

    public void dumpBrief() {
        System.out.printf(Locale.ROOT, "[%d] Minute GC stats:%n", getEndTimeNanos() / (60L * 1000_000_000));
        getGcNames().stream().sorted().forEach(gcName -> {
            GcGenerationStats total = getStatsForGc(gcName);
            long collectionsCount = total.getCollectionCount();
            System.out.printf(Locale.ROOT, "GC: %-50s Collections: %3d; time: %5d ms, %s%n",
                    gcName + ";",
                    collectionsCount,
                    total.getCollectionTimeMs(),
                    formatMinAvgMaxTime(total)
            );
        });
    }

    private static String formatMinAvgMaxTime(GcGenerationStats stats) {
        String minAvgMax;
        if (stats instanceof GcGenerationStatsWithMedian) {
            minAvgMax = String.format(Locale.ROOT, "min/avg/median/99pct/max: %d/%.2f/%.2f/%.2f/%d ms",
                    stats.getCollectionTimeMin(),
                    stats.getCollectionTimeAverage(),
                    ((GcGenerationStatsWithMedian) stats).getCollectionTimeMedian(),
                    ((GcGenerationStatsWithMedian) stats).getCollectionTimePercentile(99),
                    stats.getCollectionTimeMax());
        } else {
            minAvgMax = String.format(Locale.ROOT,"min/avg/max: %d/%.2f/%d ms",
                    stats.getCollectionTimeMin(),
                    stats.getCollectionTimeAverage(),
                    stats.getCollectionTimeMax());
        }
        return minAvgMax;
    }

}
