package ru.otus.listener.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.Message;
import ru.otus.ObjectForMessage;
import ru.otus.processor.homework.TimeProvider;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryMessageHistoryRepositoryTest {

    @Test
    @DisplayName("Логирование работает корректно")
    void logMessageShouldWorkCorrectly() {
        TimeProvider instantProvider = () -> Instant.parse("2020-11-22T10:00:00.00Z");
        MemoryMessageHistoryRepository repository = new MemoryMessageHistoryRepository(1, instantProvider);

        Message oldMessage = new Message.Builder(42)
                .field11("field11")
                .field13(new ObjectForMessage(List.of("field13")))
                .build();

        Message newMessage = new Message.Builder(42)
                .field1("field1")
                .field13(new ObjectForMessage(List.of("field13")))
                .build();

        repository.logMessage(oldMessage, newMessage);

        MessageLogRecord logRecord = repository.getLastMessages(1).get(0);

        assertThat(logRecord.getTimestamp())
                .isEqualTo("2020-11-22T10:00:00.00Z");

        assertThat(logRecord.getOldMessage())
                .returns(42L, Message::getId)
                .returns("field11", Message::getField11)
                .returns(List.of("field13"), m -> m.getField13().getData());

        assertThat(logRecord.getNewMessage())
                .returns(42L, Message::getId)
                .returns("field1", Message::getField1)
                .returns(List.of("field13"), m -> m.getField13().getData());
    }

    @Test
    void getLastMessagesShouldReturnEmptyListOnEmptyRepository() {
        MemoryMessageHistoryRepository repository = new MemoryMessageHistoryRepository();

        assertThat(repository.getLastMessages(100)).isEmpty();
        
        assertThat(repository.getLastMessages(0)).isEmpty();
    }

    @Test
    @DisplayName("Изменение данных field13 после логирования не влияет на залогированные результаты")
    void messageModificationShouldNotChangeLog() {
        MemoryMessageHistoryRepository repository = new MemoryMessageHistoryRepository();

        ObjectForMessage oldMessageObject = new ObjectForMessage(List.of("original", "message", "data"));
        Message oldMessage = new Message.Builder(42)
                .field13(oldMessageObject)
                .build();
        Message newMessage = new Message.Builder(42).build();

        repository.logMessage(oldMessage, newMessage);

        oldMessageObject.setData(List.of("new", "message", "data"));

        MessageLogRecord logRecord = repository.getLastMessages(1).get(0);

        assertThat(logRecord.getOldMessage().getField13().getData())
                .containsExactly("original", "message", "data");
    }

    @Test
    @DisplayName("Изменение списка объекта в field13 после логирования не влияет на залогированные результаты")
    void messageContentModificationShouldNotChangeLog() {
        MemoryMessageHistoryRepository repository = new MemoryMessageHistoryRepository();

        List<String> messageList = new ArrayList<>(List.of("original", "list"));
        Message oldMessage = new Message.Builder(42).build();
        Message newMessage = new Message.Builder(42)
                .field13(new ObjectForMessage(messageList))
                .build();

        repository.logMessage(oldMessage, newMessage);

        messageList.set(0, "modified");
        messageList.add("added");

        MessageLogRecord logRecord = repository.getLastMessages(1).get(0);

        assertThat(logRecord.getNewMessage().getField13().getData())
                .containsExactly("original", "list");
    }

    @Test
    @DisplayName("Изменение возвращенного объекта не должно влиять на результаты")
    void modificationOfReturningDataShouldNotChangeLog() {
        MemoryMessageHistoryRepository repository = new MemoryMessageHistoryRepository();
        Message oldMessage = new Message.Builder(42)
                .field13(new ObjectForMessage(List.of("field13", "data")))
                .build();
        Message newMessage = new Message.Builder(42).build();
        repository.logMessage(oldMessage, newMessage);

        MessageLogRecord logRecord = repository.getLastMessages(1).get(0);

        logRecord.getOldMessage().getField13().setData(List.of("new", "data"));

        MessageLogRecord logRecordNew = repository.getLastMessages(1).get(0);

        assertThat(logRecordNew.getOldMessage().getField13().getData())
                .containsExactly("field13", "data");

    }
}