package ru.x0xdc.otus.java.atm.model;

import java.util.Objects;

public class Cassette {

    private final Denomination denomination;

    private int quantity;

    public Cassette(Denomination denomination) {
        this(denomination, 0);
    }

    public Cassette(Denomination denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotalBalance() {
        return getQuantity() * getDenomination().getValue();
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity should be positive");
        this.quantity = quantity;
    }

    public void addNotes(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity should be positive");
        this.quantity += quantity;
    }

    public void removeNotes(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity should be positive");
        if (quantity > this.quantity) throw new IllegalArgumentException("Removing more notes than available");
        this.quantity -= quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cassette cassette = (Cassette) o;
        return quantity == cassette.quantity && denomination == cassette.denomination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination, quantity);
    }
}
