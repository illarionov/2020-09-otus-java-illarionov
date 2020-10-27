package ru.x0xdc.otus.java.gc.collector;

public interface GcGenerationStats {
    String getName();

    long getCollectionCount();

    long getCollectionTimeMs();

    void addGcEvent(long duration);

    long getCollectionTimeMin();

    long getCollectionTimeMax();

    double getCollectionTimeAverage();

    void addStats(GcGenerationStats stats);

}
