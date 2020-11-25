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

    public MessageLogRecord(MessageLogRecord other) {
        this(other.getTimestamp(), other.oldMessage, other.newMessage);
    }

    public MessageLogRecord(Instant timestamp, Message oldMessage, Message newMessage) {
        Objects.requireNonNull(timestamp);
        this.timestamp = timestamp;
        this.oldMessage = oldMessage != null ? oldMessage.toBuilder().build() : null;
        this.newMessage = newMessage != null ? newMessage.toBuilder().build() : null;
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
    public String toString() {
        return timestamp + " " + oldMessage + " => " + newMessage;
    }
}