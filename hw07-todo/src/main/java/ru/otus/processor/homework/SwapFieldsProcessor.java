package ru.otus.processor.homework;

import ru.otus.Message;
import ru.otus.processor.Processor;

public class SwapFieldsProcessor implements Processor {

    @Override
    public Message process(Message message) {
        return message.toBuilder()
                .field11(message.getField12())
                .field12(message.getField11())
                .build();
    }
}
