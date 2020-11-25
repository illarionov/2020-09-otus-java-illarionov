package ru.otus.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.Message;
import ru.otus.processor.Processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

class SwapFieldsProcessorTest {

    @Test
    @DisplayName("Процессор должен менять значения field11 и field12")
    void shouldSwapFieldsCorrectly() {
        Processor processor = new SwapFieldsProcessor();
        Message message = new Message.Builder(1)
                .field1("1")
                .field11("111")
                .field12("222")
                .build();

        Message result = processor.process(message);

        assertThat(result)
                .returns("1", from(Message::getField1))
                .returns("222", from(Message::getField11))
                .returns("111", from(Message::getField12));
    }

    @Test
    @DisplayName("Процессор не должен изменять исходное сообщение")
    void shouldNotChangeSourceMessage() {
        Processor processor = new SwapFieldsProcessor();
        Message message = new Message.Builder(1)
                .field1("1")
                .field11("111")
                .field12("222")
                .build();

        processor.process(message);

        assertThat(message)
                .returns("1", from(Message::getField1))
                .returns("111", from(Message::getField11))
                .returns("222", from(Message::getField12));
    }
}