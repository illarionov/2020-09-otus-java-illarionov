package ru.x0xdc.otus.java.atm.model;

import java.util.Currency;
import java.util.Objects;

public final class Banknote {

    private final Denomination denomination;

    public static Banknote of(Denomination denomination) {
        return new Banknote(denomination);
    }

    public static Banknote of(int value) {
        return new Banknote(Denomination.fromValue(value));
    }

    private Banknote(Denomination denomination) {
        Objects.requireNonNull(denomination, "Denomination should not be null");
        this.denomination = denomination;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public int getValue() {
        return denomination.getValue();
    }

    public Currency getCurrency() {
        return denomination.getCurrency();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banknote banknote = (Banknote) o;
        return denomination == banknote.denomination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination);
    }

    @Override
    public String toString() {
        return "Banknote{" + denomination + '}';
    }
}
