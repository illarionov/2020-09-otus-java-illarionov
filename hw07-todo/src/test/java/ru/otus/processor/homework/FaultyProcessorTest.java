package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import ru.otus.Message;
import ru.otus.processor.Processor;

import java.time.Instant;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FaultyProcessorTest {

    @Test
    void shouldPassOnOddSecond() {
        var srcProcessor = new Processor() {
            public boolean isProcessed;
            @Override
            public Message process(Message message) {
                isProcessed = true;
                return message;
            }
        };

        Supplier<Instant> oddSecond = () -> Instant.parse("2020-11-21T11:11:11.00Z");

        Processor testProcessor = new FaultyProcessor(srcProcessor, oddSecond);

        testProcessor.process(new Message.Builder(42).build());

        assertThat(srcProcessor.isProcessed)
                .isTrue();
    }

    @Test
    void shouldFailOnEvenSecond() {
        var srcProcessor = new Processor() {
            public boolean isProcessed;
            @Override
            public Message process(Message message) {
                isProcessed = true;
                return message;
            }
        };

        Supplier<Instant> evenSecond = () -> Instant.parse("2020-11-21T11:11:10.00Z");

        Processor testProcessor = new FaultyProcessor(srcProcessor, evenSecond);

        assertThatThrownBy(() -> testProcessor.process(new Message.Builder(42).build()))
                .isInstanceOf(RuntimeException.class);
        
        assertThat(srcProcessor.isProcessed)
                .isFalse();
    }
}