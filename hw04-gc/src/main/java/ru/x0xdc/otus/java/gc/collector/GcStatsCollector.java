package ru.x0xdc.otus.java.gc.collector;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;
import ru.x0xdc.otus.java.gc.collector.GcUtils.TotalMemUsage;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.sun.management.GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION;
import static ru.x0xdc.otus.java.gc.collector.GcUtils.calcTotalMemUsage;
import static ru.x0xdc.otus.java.gc.collector.GcUtils.formatMB;

public class GcStatsCollector {

    /**
     * Накопленная за всё время статистика
     */
    private GcStats stats = new GcStats();

    /**
     * Статистика за последнюю минуту
     */
    private GcStats statsLastMinute = new GcStats(true);

    /**
     * Статистика из {@link java.lang.management.GarbageCollectorMXBean}, для контроля
     */
    private final GcMxBeansStats gcStats = new GcMxBeansStats();

    public synchronized void start() {
        if (isActive()) throw new IllegalStateException("Already started");

        long startTimestamp = System.nanoTime();

        stats.open(startTimestamp);
        statsLastMinute.open(startTimestamp);
        gcStats.open();
        for (GarbageCollectorMXBean gcbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            ((NotificationEmitter)gcbean).addNotificationListener(gcNotificationListener, null, null);
        }
    }

    public synchronized void stop() {
        if (!isActive()) throw new IllegalStateException();
        long endTimestamp = System.nanoTime();

        try {
            for (GarbageCollectorMXBean gcbean : ManagementFactory.getGarbageCollectorMXBeans()) {
                ((NotificationEmitter) gcbean).removeNotificationListener(gcNotificationListener);
            }
        } catch (ListenerNotFoundException e) {
            throw new IllegalStateException(e);
        }

        stats.close(endTimestamp);
        statsLastMinute.close(endTimestamp);
        gcStats.close();

        statsLastMinute.dumpBrief();
    }

    public synchronized boolean isActive() {
        return stats.isActive();
    }

    public synchronized void dumpStats() {
        gcStats.dump();
        System.out.println();
        stats.dump();

        double minutes = stats.getTotalTimeNanos() / (1000_000_000.0 * 60.0);
        double collectionsPerMinute = stats.getTotalCollections() / minutes;
        double secPerMinute = stats.getTotalGcTimeMs() / 1000.0 / minutes;

        System.out.printf(Locale.ROOT, "Collections per minute: %.2f; Time per minute: %.3f s (%.3f%%)%n",
                collectionsPerMinute, secPerMinute, secPerMinute * 100.0 / 60.0
        );
    }

    private final NotificationListener gcNotificationListener = new NotificationListener() {

        @Override
        public void handleNotification(Notification notification, Object handback) {
            if (!GARBAGE_COLLECTION_NOTIFICATION.equals(notification.getType())) {
                return;
            }

            long eventTimestamp = System.nanoTime();
            GarbageCollectionNotificationInfo event = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

            synchronized (GcStatsCollector.this) {
                stats.addCollectionEvent(event);

                // Раз в минуту выводим в консоль накопленную за эту минуту статистику
                long nextMin = statsLastMinute.getStartTimeNanos() + TimeUnit.MINUTES.toNanos(1);
                if (eventTimestamp >= nextMin) {
                    statsLastMinute.close(nextMin);
                    statsLastMinute.dumpBrief();
                    statsLastMinute.open(nextMin);
                }
                statsLastMinute.addCollectionEvent(event);
            }
            //logGcEvent(event);
        }

        private void logGcEvent(GarbageCollectionNotificationInfo notification) {
            String gcName = notification.getGcName();
            String gcAction = notification.getGcAction();
            String gcCause = notification.getGcCause();

            GcInfo gcInfo = notification.getGcInfo();
            long startTime = gcInfo.getStartTime();
            long duration = gcInfo.getDuration();

            TotalMemUsage memAfter = calcTotalMemUsage(gcInfo.getMemoryUsageAfterGc());
            TotalMemUsage memBefore = calcTotalMemUsage(gcInfo.getMemoryUsageBeforeGc());

            System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause
                    + "(" + duration + " ms)" + " MEM: " + formatMB(memBefore.used) + " -> " + formatMB(memAfter.used)
                    + " (" + formatMB(memAfter.committed) + ")");
        }
    };

}
