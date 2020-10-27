package ru.x0xdc.otus.java.gc.collector;

import java.lang.management.*;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public final class GcUtils {

    private GcUtils() {}


    public static void printRuntimeInfo() {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        System.out.printf(Locale.ROOT, "PID: %s%nVM spec: %s (%s) %s%nVM name: %s (%s) %s%nVM arguments: %s%n",
                mxBean.getName(),
                mxBean.getSpecName(), mxBean.getSpecVendor(), mxBean.getSpecVersion(),
                mxBean.getVmName(), mxBean.getVmVendor(), mxBean.getVmVersion(),
                String.join(" ", mxBean.getInputArguments())
        );
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        System.out.printf(Locale.ROOT, "Heap memory usage. Init: %.2fM, committed: %.2fM, max: %.2fM%n",
                heap.getInit() / (1024 * 1024d),
                heap.getCommitted() / (1024 * 1024d),
                heap.getMax() / (1024 * 1024d));
        System.out.println("GC names: " + ManagementFactory.getGarbageCollectorMXBeans().stream()
                .map(GarbageCollectorMXBean::getName).sorted().collect(Collectors.joining(", ")));
    }

    public static TotalMemUsage calcTotalMemUsage(Map<String, MemoryUsage> memUsage) {
        return memUsage.entrySet()
                .stream()
                .filter(item -> {
                    return !(item.getKey().startsWith("CodeHeap")
                            || item.getKey().equals("Compressed Class Space")
                            || item.getKey().equals("Metaspace"));
                })
                .collect(TotalMemUsage::new, (total, entry) -> total.add(entry.getValue()), TotalMemUsage::add);
    }

    static String formatMB(long v) {
        return String.format(Locale.ROOT, "%.2fM", v / (1024 * 1024d));
    }

    public static class TotalMemUsage {
        public long init, committed, max, used;

        void add (MemoryUsage usage){
            init += usage.getInit();
            committed += usage.getCommitted();
            max += usage.getMax();
            used += usage.getUsed();
        }

        void add(TotalMemUsage other) {
            init += other.init;
            committed += other.committed;
            max += other.max;
            used += other.used;
        }
    }
}
