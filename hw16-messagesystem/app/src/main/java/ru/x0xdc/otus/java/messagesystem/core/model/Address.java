package ru.x0xdc.otus.java.messagesystem.core.model;

import java.io.Serializable;
import java.util.Objects;

public final class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long id;

    private final String street;
    
    public Address(long id, String street) {
        this.id = id;
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
