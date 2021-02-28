package ru.x0xdc.otus.java.grpc.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static ru.x0xdc.otus.java.grpc.utils.SeqenceEvent.onEnd;
import static ru.x0xdc.otus.java.grpc.utils.SeqenceEvent.onNext;

class SequenceEmitterTest {

    @DisplayName("Генератор генерирует правильные последовательности на корректных диапазонах")
    @ParameterizedTest(name="[{index}] Sequence of [{0}, {1}] should emmit: {2}")
    @MethodSource
    void emitterShouldEmmitCorrectSequences(int startValue, int endValue, List<SeqenceEvent<Integer>> expectedResult) throws InterruptedException {
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        List<SeqenceEvent> events = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(2);
        SequenceEmitter emitter = new SequenceEmitter(startValue, endValue, 5, TimeUnit.MILLISECONDS, service, new SequenceEmitter.Observer() {
            @Override
            public void onNext(int currentValue) {
                events.add(SeqenceEvent.onNext(currentValue));
            }

            @Override
            public void onEnd() {
                events.add(SeqenceEvent.onEnd());
                latch.countDown();
            }
        });
        emitter.start();
        latch.await(5L * (expectedResult.size() + 3), TimeUnit.MILLISECONDS);
        assertThat(events)
                .containsExactlyElementsOf(expectedResult);
    }

    static Stream<Arguments> emitterShouldEmmitCorrectSequences() {
        return Stream.of(
                arguments(0, 0, List.of(onEnd())),
                arguments(0, 1, List.of(onNext(0), onEnd())),
                arguments(0, -1, List.of(onEnd())),
                arguments(10, 15, List.of(onNext(10), onNext(11), onNext(12), onNext(13),
                        onNext(14), onEnd()))
        );
    }

    @Test
    @DisplayName("При прерывании генератор должен остановиться и заэммитить on_end")
    void cancelledGeneratorShouldEmmitOnEnd() throws InterruptedException {
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(2);
        List<SeqenceEvent> events = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch phase1Latch = new CountDownLatch(5);
        SequenceEmitter emitter = new SequenceEmitter(1, 10, 5, TimeUnit.MILLISECONDS, service, new SequenceEmitter.Observer() {

            @Override
            public void onNext(int currentValue) {
                events.add(SeqenceEvent.onNext(currentValue));
                phase1Latch.countDown();
            }

            @Override
            public void onEnd() {
                events.add(SeqenceEvent.onEnd());
                phase1Latch.countDown();
            }
        });
        emitter.start();
        phase1Latch.await(3, TimeUnit.SECONDS);
        emitter.stop();
        Thread.sleep(30);
        assertThat(events)
                .containsExactly(onNext(1), onNext(2), onNext(3), onNext(4), onNext(5), onEnd());
    }

}