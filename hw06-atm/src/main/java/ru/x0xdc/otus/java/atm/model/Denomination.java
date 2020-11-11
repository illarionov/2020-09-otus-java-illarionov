package ru.x0xdc.otus.java.atm.model;

import java.util.Currency;

public enum Denomination {

    RUB5(5),

    RUB10(10),

    RUB50(50),

    RUB100(100),

    RUB200(200),

    RUB500(500),

    RUB1000(1000),

    RUB2000(2000),

    RUB5000(5000);

    private final int value;

    public static Denomination fromValue(int value) {
        for (Denomination d: values()) {
            if (d.getValue() == value) return d;
        }
        throw new IllegalArgumentException("Incorrect face value");
    }

    Denomination(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Currency getCurrency() {
        return Currency.getInstance("RUB");
    }
}
