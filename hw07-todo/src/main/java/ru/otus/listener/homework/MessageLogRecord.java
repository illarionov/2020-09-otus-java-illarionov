package ru.otus.listener.homework;

import ru.otus.Message;

import java.time.Instant;
import java.util.Objects;

public final class MessageLogRecord {

    private final Instant timestamp;

    private final Message oldMessage;

    private final Message newMessage;

    public MessageLogRecord(Message oldMessage, Message newMessage) {
        this(Instant.now(), oldMessage, newMessage);
    }

    public MessageLogRecord(Instant timestamp, Message oldMessage, Message newMessage) {
        Objects.requireNonNull(timestamp);
        this.timestamp = timestamp;
        this.oldMessage = oldMessage;
        this.newMessage = newMessage;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Message getOldMessage() {
        return oldMessage;
    }

    public Message getNewMessage() {
        return newMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageLogRecord that = (MessageLogRecord) o;
        return timestamp.equals(that.timestamp) && Objects.equals(oldMessage, that.oldMessage) && Objects.equals(newMessage, that.newMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, oldMessage, newMessage);
    }

    @Override
    public String toString() {
        return timestamp + " " + oldMessage + " => " + newMessage;
    }
}