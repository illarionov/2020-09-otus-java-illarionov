package ru.otus.utils;

public final class Preconditions {

    private Preconditions() {
    }

    public static <T> T checkNotNull(T value, String valueName) throws IllegalArgumentException {
        if (null == value) {
            throw new IllegalArgumentException("Null value for '" + valueName + "'");
        } else {
            return value;
        }
    }

    public static String checkNotEmpty(String value, String valueName) throws IllegalArgumentException {
        checkNotNull(value, valueName);
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Empty string '" + valueName + "'");
        } else {
            return value;
        }
    }

    public static String checkNotBlank(String value, String valueName) throws IllegalArgumentException  {
        checkNotEmpty(value, valueName);
        if (value.isBlank()) {
            throw new IllegalArgumentException("Blank string for '" + valueName + "'");
        }
        return value;
    }

}
