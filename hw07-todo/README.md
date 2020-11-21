# Обработчик сообщений

**Цель:**
Применить на практике шаблоны проектирования.

Реализовать TODO из модуля **homework**:

1) Добавить поля *field11* - *field13* в класс *[Message]* (для *field13* использовать
   класс *[ObjectForMessage]*)
1) Сделать процессор, который поменяет местами значения *field11* и *field12* 
   [SwapFieldsProcessor]
1) Сделать процессор, который будет выбрасывать исключение в четную секунду, сделать тест
   с гарантированным результатом. [FaultyProcessor]
1) Сделать *Listener* для ведения истории: старое сообщение — новое (как сделать, чтобы
   сообщения не портились?) [LoggingListener]                         

[Message]: src/main/java/ru/otus/Message.java

[ObjectForMessage]: src/main/java/ru/otus/ObjectForMessage.java

[SwapFieldsProcessor]: src/main/java/ru/otus/processor/homework/SwapFieldsProcessor.java

[FaultyProcessor]: src/main/java/ru/otus/processor/homework/FaultyProcessor.java

[LoggingListener]: src/main/java/ru/otus/listener/homework/LoggingListener.java 