package ru.otus.utils;

import jakarta.servlet.http.HttpServletRequest;

public final class ServletUtils {
    private ServletUtils() {
    }

    public static String getParameterOrEmptyString(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value != null && !value.isBlank() ? value : "";
    }
}
