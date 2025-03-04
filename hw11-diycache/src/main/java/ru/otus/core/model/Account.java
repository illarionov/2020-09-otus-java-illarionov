package ru.otus.core.model;

import ru.otus.core.annnotations.Id;

import java.util.Objects;

public final class Account {

    @Id(autoGenerated = false)
    private final String no;

    private final String type;

    private final double rest;

    public Account(String no, String type, double rest) {
        Objects.requireNonNull(no);
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public String getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public double getRest() {
        return rest;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return no.equals(account.no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no);
    }

    @Override
    public String toString() {
        return "Account{" +
                "no='" + no + '\'' +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
