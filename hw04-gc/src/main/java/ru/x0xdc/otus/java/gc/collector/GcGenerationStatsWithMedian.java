package ru.x0xdc.otus.java.gc.collector;

import com.google.common.math.Quantiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Коллектор с вычислением медианных значений
 */
public class GcGenerationStatsWithMedian implements GcGenerationStats {

    private final String name;

    private List<Long> collections;

    public GcGenerationStatsWithMedian(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        this.collections = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getCollectionCount() {
        return collections.size();
    }

    @Override
    public long getCollectionTimeMs() {
        return collections.stream().mapToLong(Long::longValue).sum();
    }

    @Override
    public void addGcEvent(long duration) {
        collections.add(duration);
    }

    @Override
    public void addStats(GcGenerationStats stats) {
        if (stats instanceof GcGenerationStatsWithMedian) {
            this.collections.addAll(((GcGenerationStatsWithMedian) stats).collections);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public long getCollectionTimeMin() {
        return !collections.isEmpty() ? Collections.min(collections) : 0;
    }

    @Override
    public long getCollectionTimeMax() {
        return !collections.isEmpty() ? Collections.max(collections) : 0;
    }

    @Override
    public double getCollectionTimeAverage() {
        if (collections.isEmpty()) return 0;
        return collections.stream().mapToDouble(Double::valueOf).average().orElse(0);
    }

    public double getCollectionTimeMedian() {
        //noinspection UnstableApiUsage
        if (collections.isEmpty()) return 0;
        return Quantiles.median().compute(collections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GcGenerationStatsWithMedian that = (GcGenerationStatsWithMedian) o;
        return name.equals(that.name) && collections.equals(that.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, collections);
    }

    @SuppressWarnings("UnstableApiUsage")
    public double getCollectionTimePercentile(int pct) {
        if (collections.isEmpty()) return 0;
        return Quantiles.percentiles().index(pct).compute(collections);
    }
}
