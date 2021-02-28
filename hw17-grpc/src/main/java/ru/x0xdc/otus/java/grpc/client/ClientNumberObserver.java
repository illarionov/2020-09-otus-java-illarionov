package ru.x0xdc.otus.java.grpc.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceReply;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceRequest;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceServiceGrpc;
import ru.x0xdc.otus.java.grpc.utils.SequenceEmitter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ClientNumberObserver {
    private static final Logger logger = LoggerFactory.getLogger(ClientNumberObserver.class);

    private final NumberSequenceServiceGrpc.NumberSequenceServiceStub numberSequenceService;

    private int lastServerValue = 0;

    private int currentValue = -1;

    private final SequenceEmitter sequenceEmitter;

    private final AtomicReference<CountDownLatch> completeLatch;

    public ClientNumberObserver(ScheduledExecutorService executorService, NumberSequenceServiceGrpc.NumberSequenceServiceStub numberSequenceService) {
        this.numberSequenceService = numberSequenceService;
        this.sequenceEmitter = new SequenceEmitter(0, 50,
                1, TimeUnit.SECONDS, executorService, sequenceObserver);
        this.completeLatch = new AtomicReference<>(null);
    }

    public void start() {
        this.completeLatch.set(new CountDownLatch(1));
        NumberSequenceRequest request = NumberSequenceRequest.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();
        numberSequenceService.getNumbers(request, new StreamObserver<>() {
            @Override
            public void onNext(NumberSequenceReply value) {
                int newValue = value.getNum();
                logger.info("new value: {}", newValue);
                synchronized (ClientNumberObserver.this) {
                    lastServerValue = newValue;
                }
            }

            @Override
            public void onError(Throwable t) {
                logger.error("Server error", t);
                stopClientCounter();
            }

            @Override
            public void onCompleted() {
                logger.error("Server stream completed");
            }
        });
        startClientCounter();
    }

    private void startClientCounter() {
        logger.info("Numbers Client is starting...");
        synchronized (this) {
            currentValue = 0;
            sequenceEmitter.start();
        }
    }

    private void stopClientCounter() {
        sequenceEmitter.stop();
        CountDownLatch latch = completeLatch.getAndSet(null);
        if (latch != null) {
            latch.countDown();
        }
    }

    public void awaitCompletion() throws InterruptedException {
        CountDownLatch latch = completeLatch.get();
        if (latch != null) {
            latch.await();
        }
    }

    private final SequenceEmitter.Observer sequenceObserver = new SequenceEmitter.Observer() {
        @Override
        public void onNext(int counterValue) {
            int newCurrentValue;
            synchronized (ClientNumberObserver.this) {
                newCurrentValue = currentValue = currentValue + lastServerValue + 1;
                lastServerValue = 0;
            }
            logger.info("currentValue: {}", newCurrentValue);
        }

        @Override
        public void onEnd() {
            logger.info("Sequence complete");
            CountDownLatch latch = completeLatch.getAndSet(null);
            if (latch != null) {
                latch.countDown();
            }
        }
    };
}
