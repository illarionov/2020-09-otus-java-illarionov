package ru.otus.processor.homework;

import ru.otus.Message;
import ru.otus.processor.Processor;

import java.time.Instant;

public class FaultyProcessor implements Processor {

    private final Processor processor;

    private final TimeProvider timeProvider;

    public FaultyProcessor(Processor processor) {
        this(processor, Instant::now);
    }

    public FaultyProcessor(Processor processor, TimeProvider timeProvider) {
        this.processor = processor;
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        Instant currentTime = timeProvider.get();
        if (currentTime.getEpochSecond() % 2 == 0) {
            throw new EvenSecondException("Method called at the even second " + currentTime);
        }
        return processor.process(message);
    }


    public static class EvenSecondException extends RuntimeException {
        public EvenSecondException() {
        }

        public EvenSecondException(String message) {
            super(message);
        }
    }
}

