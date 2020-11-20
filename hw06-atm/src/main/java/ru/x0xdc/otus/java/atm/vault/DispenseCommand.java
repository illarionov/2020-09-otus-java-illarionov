package ru.x0xdc.otus.java.atm.vault;

import ru.x0xdc.otus.java.atm.model.Banknote;
import ru.x0xdc.otus.java.atm.model.Change;
import ru.x0xdc.otus.java.atm.model.Result;

import java.util.List;

public class DispenseCommand extends Command<List<Banknote>> {

    private final Change change;

    public DispenseCommand(MoneyVault vault, Change change) {
        super(vault);
        this.change = change;
    }

    @Override
    public Result<List<Banknote>> execute() {
        return vault.dispenseCommand(change);
    }
}
