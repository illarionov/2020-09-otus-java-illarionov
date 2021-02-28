package ru.x0xdc.otus.java.messagesystem.mappers;

public interface Mapper<T, R> {
    R map(T item);

    T mapFrom(R item);
}
