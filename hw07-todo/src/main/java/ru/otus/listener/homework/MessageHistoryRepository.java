package ru.otus.listener.homework;

import ru.otus.Message;

import java.util.List;

public interface MessageHistoryRepository extends AutoCloseable {

    void logMessage(Message oldMessage,Message newMessage);

    List<MessageLogRecord> getLastMessages(int count);

}
