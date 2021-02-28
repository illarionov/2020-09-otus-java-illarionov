package ru.x0xdc.otus.java.grpc.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.x0xdc.otus.java.grpc.protobuf.generated.NumberSequenceReply;
import ru.x0xdc.otus.java.grpc.utils.SequenceEmitter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class NumberGenerator {
    private static final Logger logger = LoggerFactory.getLogger(SequenceEmitter.class.getName());

    private final StreamObserver<NumberSequenceReply> observer;

    private final SequenceEmitter sequenceEmitter;

    NumberGenerator(ScheduledExecutorService executorService, StreamObserver<NumberSequenceReply> observer, int firstValue, int lastValue) {
        this.observer = observer;
        this.sequenceEmitter = new SequenceEmitter(firstValue, lastValue,
                2, TimeUnit.SECONDS, executorService, sequenceObserver);
    }

    public void start() {
        logger.info("Start emitting sequence");
        sequenceEmitter.start();
    }

    private final SequenceEmitter.Observer sequenceObserver = new SequenceEmitter.Observer() {
        @Override
        public void onNext(int currentValue) {
            logger.info("Sending value {}", currentValue);
            synchronized (observer) {
                observer.onNext(NumberSequenceReply.newBuilder().setNum(currentValue).build());
            }
        }

        @Override
        public void onEnd() {
            logger.info("Sending complete notification");
            synchronized (observer) {
                observer.onCompleted();
            }
        }
    };
}
