package ru.x0xdc.otus.java.grpc.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SequenceEmitter {
    private final ScheduledExecutorService executorService;

    private final int firstValue;

    private final int lastValue;

    private final int period;

    private final TimeUnit periodTimeUnit;

    private final Observer observer;

    private int currentValue;

    private ScheduledFuture<?> scheduledFuture;

    public interface Observer {
        void onNext(int currentValue);

        void onEnd();
    }

    public SequenceEmitter(int firstValue, int lastValue, int period, TimeUnit periodTimeUnit, ScheduledExecutorService executorService, Observer observer) {
        this.executorService = executorService;
        this.firstValue = firstValue;
        this.lastValue = lastValue;
        this.period = period;
        this.periodTimeUnit = periodTimeUnit;
        this.observer = observer;
    }

    public void start() {
        if (firstValue >= lastValue) {
            observer.onEnd();
            return;
        }

        synchronized (this) {
            this.currentValue = firstValue;
            scheduledFuture = executorService.scheduleAtFixedRate(emmitNextValRunnable, 0, period, periodTimeUnit);
        }
    }

    public void stop() {
        synchronized (this) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(false);
                scheduledFuture = null;
                observer.onEnd();
            }
        }
    }

    private final Runnable emmitNextValRunnable = new Runnable() {
        @Override
        public void run() {
            int emmitValue;
            boolean rageCompleted = false;
            synchronized (SequenceEmitter.this) {
                emmitValue = currentValue;
                currentValue += 1;
                if (currentValue == lastValue) {
                    rageCompleted = true;
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel(false);
                        scheduledFuture = null;
                    }
                }
            }
            observer.onNext(emmitValue);
            if (rageCompleted) {
                observer.onEnd();
            }
        }
    };
}
