package ru.otus.listener.homework;

import ru.otus.Message;
import ru.otus.listener.Listener;

public class LoggingListener implements Listener {

    private final MessageHistoryRepository repository;

    public LoggingListener(MessageHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        repository.logMessage(oldMsg, newMsg);
    }
}
