package ru.x0xdc.otus.java.messagesystem.core.model;

import java.io.Serializable;
import java.util.Objects;

public final class Phone implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long id;

    private final String number;

    public Phone(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return id == phone.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
