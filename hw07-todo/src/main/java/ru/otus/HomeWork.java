package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.LoggingListener;
import ru.otus.listener.homework.MemoryMessageHistoryRepository;
import ru.otus.processor.ProcessorUpperField10;
import ru.otus.processor.homework.FaultyProcessor;
import ru.otus.processor.homework.SwapFieldsProcessor;

import java.util.List;
import java.util.stream.Collectors;

public class HomeWork {

    public static void main(String[] args) {
        var processors = List.of(new SwapFieldsProcessor(),
                new FaultyProcessor(new ProcessorUpperField10()));

        var loggingRepository = new MemoryMessageHistoryRepository();
        var loggingListener = new LoggingListener(loggingRepository);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            System.err.println("Failed to process message");
            ex.printStackTrace();
        });

        complexProcessor.addListener(loggingListener);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(new ObjectForMessage(List.of("field13-1", "field13-2", "field13-3")))
                .build();

        var result = complexProcessor.handle(message);
        complexProcessor.removeListener(loggingListener);
        try {
            loggingRepository.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Result:" + result);
        System.out.println("Log: \n" + loggingRepository.getLastMessages(100).stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n")));
    }
}
