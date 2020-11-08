package ru.x0xdc.otus.java.aop;

import ru.x0xdc.otus.java.aop.annotations.Log;

public interface TestLogging {

    @Log
    void calculation();

    @Log
    void calculation(int param);

    @Log
    void calculation(int param1, int param2);

    @Log
    void calculation(int param1, int param2, String param3);

    void calculation(int[] i);
}
