package ru.x0xdc.otus.java.testrunner.runner;

import java.util.Objects;

public final class TestSuiteResult {
    private final int total;

    private final int failed;

    TestSuiteResult(int total, int failed) {
        this.total = total;
        this.failed = failed;
    }

    public int getTotal() {
        return total;
    }

    public int getFailed() {
        return failed;
    }

    public int getSucceeded() {
        return getTotal() - getFailed();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestSuiteResult that = (TestSuiteResult) o;
        return total == that.total && failed == that.failed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, failed);
    }
}
