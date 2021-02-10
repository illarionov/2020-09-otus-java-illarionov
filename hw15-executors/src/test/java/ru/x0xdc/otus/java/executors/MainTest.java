package ru.x0xdc.otus.java.executors;

import org.junit.jupiter.api.RepeatedTest;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    
    @RepeatedTest(500)
    void counterShouldWorkCorrectly() {
        final int countTo = 100;
        OutputStream os = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(os);
        Main.PingPongExecutor counter = new Main.PingPongExecutor(countTo, printStream);

        String expected = IntStream.concat(
                IntStream.iterate(1, n -> n <= countTo, n -> n += 1),
                IntStream.iterate(countTo -1, n -> n > 0, n -> n -= 1))
                .mapToObj(n -> String.format("Thread %d: %d\n", (1 + (n - 1) % 2), n))
                .collect(Collectors.joining(""));

        try {
            counter.execute();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(expected, os.toString());
    }

}