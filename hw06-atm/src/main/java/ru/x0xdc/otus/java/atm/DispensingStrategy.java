package ru.x0xdc.otus.java.atm;

import ru.x0xdc.otus.java.atm.model.Change;

public interface DispensingStrategy {
    Change calculateDispense(Change leftovers, int sum) throws DispenseException;
}
