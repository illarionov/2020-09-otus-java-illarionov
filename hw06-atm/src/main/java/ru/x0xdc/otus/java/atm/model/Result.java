package ru.x0xdc.otus.java.atm.model;

import java.util.Objects;

public final class Result<T> {

    private final T result;

    private final Throwable error;

    private final Status status;

    public static <T> Result<T> success(T result) {
        return new Result<>(result, null, Status.SUCCESS);
    }

    public static <T> Result<T> failed(Throwable error) {
        return new Result<>(null, error, Status.FAILED);
    }

    private Result(T result, Throwable error, Status status) {
        this.result = result;
        this.error = error;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public T getResult() {
        return result;
    }

    public Throwable getError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result1 = (Result<?>) o;
        return Objects.equals(result, result1.result) && Objects.equals(error, result1.error) && status == result1.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, error, status);
    }
}
