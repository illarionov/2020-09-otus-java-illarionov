package ru.x0xdc.otus.java.gc;

import ru.x0xdc.otus.java.gc.collector.GcStatsCollector;
import ru.x0xdc.otus.java.gc.collector.GcUtils;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.*;

public class Main {

    public static void main(String... args) throws Exception {
        GcStatsCollector collector = new GcStatsCollector();

        GcUtils.printRuntimeInfo();

        int size = 1 * 1000 * 1000;
        int loopCounter = 100;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.x0xdc.otus.java.gc:type=Benchmark");
        Benchmark benchmark = new Benchmark(loopCounter);
        mbs.registerMBean(benchmark, name);
        benchmark.setSize(size);

        System.gc();
        System.gc();

        collector.start();
        try {
            benchmark.run();
        } catch (OutOfMemoryError ooe) {
            benchmark.clearMemoryLeak();
            throw ooe;
        } finally {
            collector.stop();
            System.out.printf("Done: %d/%d test cycles%n", benchmark.getCyclesDone(), loopCounter);
            collector.dumpStats();
        }
    }
}
