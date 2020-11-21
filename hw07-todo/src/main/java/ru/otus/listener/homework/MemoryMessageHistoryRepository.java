package ru.otus.listener.homework;

import ru.otus.Message;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class MemoryMessageHistoryRepository implements MessageHistoryRepository {

    private static final int DEFAULT_MAX_SIZE = 5000;

    private final LinkedList<MessageLogRecord> log;

    private final int maxSize;

    private final Supplier<Instant> timeProvider;

    public MemoryMessageHistoryRepository() {
        this(DEFAULT_MAX_SIZE);
    }

    public MemoryMessageHistoryRepository(int maxSize) {
        this(maxSize, Instant::now);
    }

    MemoryMessageHistoryRepository(int maxSize, Supplier<Instant> timeProvider) {
        log = new LinkedList<>();
        this.maxSize = maxSize;
        this.timeProvider = timeProvider;
    }

    @Override
    public void logMessage(Message oldMessage, Message newMessage) {
        MessageLogRecord record = new MessageLogRecord(timeProvider.get(), oldMessage, newMessage);

        while (log.size() >= maxSize) log.removeFirst();

        log.add(record);
    }

    @Override
    public List<MessageLogRecord> getLastMessages(int count) {
        return List.copyOf(log.subList(Math.max(log.size() - count, 0), log.size()));
    }

    @Override
    public void close() {    }
}
