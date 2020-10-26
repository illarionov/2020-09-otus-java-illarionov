package ru.x0xdc.otus.java.testrunner.runner;

import java.util.Objects;

final class TestResult {

    public enum Status {
        SUCCEEDED,
        FAILED
    }

    private static final TestResult SUCCEEDED = new TestResult(Status.SUCCEEDED, null);

    static TestResult succeeded() {
        return SUCCEEDED;
    }

    static TestResult failed(Throwable error) {
        Objects.requireNonNull(error);
        return new TestResult(Status.FAILED, error);
    }

    private final Status status;

    private final Throwable error;

    private TestResult(Status status, Throwable error) {
        this.status = status;
        this.error = error;
    }

    public Status getStatus() {
        return status;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isSucceeded() {
        return status == Status.SUCCEEDED;
    }

    public boolean isFailed() {
        return status == Status.FAILED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult that = (TestResult) o;
        return status == that.status && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, error);
    }
}
