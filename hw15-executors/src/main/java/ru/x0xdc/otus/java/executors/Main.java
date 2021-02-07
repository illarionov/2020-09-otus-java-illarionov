package ru.x0xdc.otus.java.executors;

import java.io.PrintStream;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        PingPongExecutor counter = new PingPongExecutor(10, System.out);
        counter.execute();
    }

    public static class PingPongExecutor {
        private final int countTo;
        private final PrintStream printStream;

        public PingPongExecutor(int countTo, PrintStream printStream) {
            this.countTo = countTo;
            this.printStream = printStream;
        }

        void execute() throws InterruptedException {
            Arbiter arbiter = new Arbiter(1, 2);
            ForwardThanBackIterator iterator = new ForwardThanBackIterator(countTo);

            WorkingThread thread1 = new WorkingThread(1, iterator, printStream, arbiter);
            WorkingThread thread2 = new WorkingThread(2, iterator, printStream, arbiter);

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();
        }
    }

    static class WorkingThread extends Thread {
        private final int threadNo;
        private final ForwardThanBackIterator iterator;
        private final PrintStream printStream;
        private final Arbiter arbiter;

        private boolean isFinished;

        WorkingThread(int threadNo, ForwardThanBackIterator iterator, PrintStream printStream, Arbiter arbiter) {
            this.threadNo = threadNo;
            this.iterator = iterator;
            this.printStream = printStream;
            this.arbiter = arbiter;
        }

        @Override
        public void run() {
            try {
                synchronized (arbiter) {
                    MAIN:
                    while (!isFinished) {
                        while (!arbiter.isCurrentThread(threadNo)) {
                            arbiter.wait();
                            if (isFinished) {
                                break MAIN;
                            }
                        }
                        arbiter.switchToNextThread();
                        arbiter.notifyAll();

                        int val = iterator.getNext();
                        if (val != ForwardThanBackIterator.IS_FINISHED) {
                            printStream.println("Thread " + threadNo + ": " + val);
                        } else {
                            isFinished = true;
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    static class Arbiter {
        final int threadStart;
        final int threadMax;

        int nextThreadNo;

        public Arbiter(int threadStart, int threadMax) {
            this.threadStart = threadStart;
            this.threadMax = threadMax;
            this.nextThreadNo = threadStart;
        }

        public boolean isCurrentThread(int threadNo) {
            return threadNo == nextThreadNo;
        }

        public void switchToNextThread() {
            if (nextThreadNo == threadMax) {
                nextThreadNo = threadStart;
            } else {
                nextThreadNo += 1;
            }
        }
    }

    static class ForwardThanBackIterator {
        public static final int IS_FINISHED = Integer.MIN_VALUE;

        private final int countTo;
        private int counter = 0;
        private int addenum = 1;

        ForwardThanBackIterator(int countTo) {
            if (countTo <= 0) throw new IllegalArgumentException("countTo must be positive");
            this.countTo = countTo;
        }

        public int getNext() {
            if (counter == IS_FINISHED) {
                return counter;
            }

            counter += addenum;
            if (counter == countTo) {
                addenum *= -1;
            } else if (counter == 0) {
                counter = IS_FINISHED;
            }
            return counter;
        }
    }
}
