package ru.x0xdc.otus.java.atm.vault;

import ru.x0xdc.otus.java.atm.model.Result;

public abstract class Command<R> {

    protected final MoneyVault vault;

    Command(MoneyVault vault) {
        this.vault = vault;
    }

    public abstract Result<R> execute();
}
