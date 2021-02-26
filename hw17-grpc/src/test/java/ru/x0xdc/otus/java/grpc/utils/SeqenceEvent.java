package ru.x0xdc.otus.java.grpc.utils;

import java.util.Objects;

public class SeqenceEvent<T> {

    private static final SeqenceEvent<?> END_EVENT = new SeqenceEvent<>(Type.ON_END, null);

    public enum Type {
        ON_NEXT,
        ON_END
    }

    private final Type type;

    private final T value;

    public static <T> SeqenceEvent<T> onNext(T value) {
        return new SeqenceEvent<>(Type.ON_NEXT, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> SeqenceEvent<T> onEnd() {
        return (SeqenceEvent<T>) END_EVENT;
    }

    private SeqenceEvent(Type type, T value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeqenceEvent<?> that = (SeqenceEvent<?>) o;
        return type == that.type && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return type == Type.ON_END ? "end()" : ("next(" + value + ")");
    }
}
