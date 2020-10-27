package ru.x0xdc.otus.java.gc;

import com.google.gson.Gson;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;

public class Benchmark implements BenchmarkMBean {
    private final int loopCounter;
    private volatile int size = 0;

    private final Gson gson = new Gson();

    private final List<byte[]> leak = new LinkedList<>();

    private int loopIdx;

    private final int leakItemSize;

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
        this.leakItemSize = getLeakItemSize();
    }

    void run() throws InterruptedException {

        for (loopIdx = 0; loopIdx < loopCounter; loopIdx++) {
            int local = size;

            String testObject = gson.toJson(new TestObject("", 1, 2,
                    new float[] {3, 4, 5}, '6', 7));

            Object[] array = new Object[local];
            for (int i = 0; i < local; i++) {
                array[i] = gson.fromJson(testObject, TestObject.class);
                if (i % 25 == 0) leak.add(new byte[leakItemSize]);
            }
            Thread.sleep(10); //Label_1
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        System.out.println("new size:" + size);
        this.size = size;
    }

    public int getCyclesDone() {
        return loopIdx;
    }

    public void clearMemoryLeak() {
        leak.clear();
    }

    private static int getLeakItemSize() {
        long memoryMax = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
        if (memoryMax <= 0) memoryMax = 4 * 1024 * 1024 * 1024L;
        // при -Xmx512M оптимальное значение - 64.
        return (int)(memoryMax * 64 / (512 * 1024 * 1024));
    }

    private static class TestObject {

        private String a;

        private int b;

        private double c;

        private float[] d;

        private char e;

        private long f;

        public TestObject(String a, int b, double c, float[] d, char e, long f) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
        }
    }
}
