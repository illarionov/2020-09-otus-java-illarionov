package ru.otus.processor.homework;

import java.time.Instant;

@FunctionalInterface
public interface TimeProvider {
    Instant get();
}
