# Сравнение разных сборщиков мусора

Цель: 
на примере простого приложения понять какое влияние оказывают сборщики мусора

Написать приложение, которое следит за сборками мусора и пишет в лог количество сборок каждого типа
(young, old) и время, которое ушло на сборки в минуту.

Добиться *OutOfMemory* в этом приложении через медленное подтекание по памяти
(например добавлять элементы в *List* и удалять только половину).

Настроить приложение (можно добавлять `Thread.sleep(...)`) так, чтобы оно падало
с `OOM` примерно через 5 минут после начала работы.

Собрать статистику (количество сборок, время на сборки) по разным GC.

Сделать выводы, какой GC лучше и почему?

Выводы оформить в файле [Conclusions.md](Conclusions.md) в корне папки проекта.  
Результаты измерений свести в таблицу.

Попробовать провести этот эксперимент на небольшом хипе порядка 256Мб, и на максимально возможном, который у вас может быть.