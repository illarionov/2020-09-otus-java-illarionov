package ru.x0xdc.otus.java.gc.collector;

import java.util.Objects;

class GcGenerationStatsImpl implements GcGenerationStats {

    private final String name;

    private long collectionCount;

    private long collectionTimeMs;

    private long collectionTimeMin;

    private long collectionTimeMax;

    GcGenerationStatsImpl(String name) {
        this(name, 0, 0, 0, 0);
    }

    GcGenerationStatsImpl(String name, long collectionCount, long collectionTimeMs, long collectionTimeMin, long collectionTimeMax) {
        Objects.requireNonNull(name);
        this.name = name;
        this.collectionCount = collectionCount;
        this.collectionTimeMs = collectionTimeMs;
        this.collectionTimeMin = collectionTimeMin;
        this.collectionTimeMax = collectionTimeMax;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getCollectionCount() {
        return collectionCount;
    }

    @Override
    public long getCollectionTimeMs() {
        return collectionTimeMs;
    }

    @Override
    public long getCollectionTimeMin() {
        return collectionTimeMin;
    }

    @Override
    public long getCollectionTimeMax() {
        return collectionTimeMax;
    }

    @Override
    public double getCollectionTimeAverage() {
        return collectionCount != 0 ? collectionTimeMs / (double)collectionCount : 0.0;
    }

    @Override
    public void addGcEvent(long duration) {
        this.collectionCount += 1;
        this.collectionTimeMs += duration;
        if (collectionTimeMin == 0 || collectionTimeMin > duration) collectionTimeMin = duration;
        if (collectionTimeMax < duration) collectionTimeMax = duration;
    }

    @Override
    public void addStats(GcGenerationStats stats) {
        this.collectionCount += stats.getCollectionCount();
        this.collectionTimeMs += stats.getCollectionTimeMs();
        if (collectionTimeMin != 0 && stats.getCollectionTimeMin() != 0) {
            this.collectionTimeMin = Math.min(collectionTimeMin, stats.getCollectionTimeMin());
        } else if (collectionTimeMin == 0) {
            collectionTimeMin = stats.getCollectionTimeMin();
        }
        this.collectionTimeMax = Math.max(collectionTimeMax, stats.getCollectionTimeMax());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GcGenerationStatsImpl that = (GcGenerationStatsImpl) o;
        return collectionCount == that.collectionCount && collectionTimeMs == that.collectionTimeMs && collectionTimeMin == that.collectionTimeMin && collectionTimeMax == that.collectionTimeMax && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, collectionCount, collectionTimeMs, collectionTimeMin, collectionTimeMax);
    }
}
