package ru.x0xdc.otus.java.atm.vault;

import ru.x0xdc.otus.java.atm.model.Banknote;
import ru.x0xdc.otus.java.atm.model.Change;
import ru.x0xdc.otus.java.atm.model.Result;

import java.util.List;

public class DepositCommand extends Command<Change> {

    private final List<Banknote> banknotes;

    public  DepositCommand(MoneyVault vault, List<Banknote> banknotes) {
        super(vault);
        this.banknotes = banknotes;
    }

    @Override
    public Result<Change> execute() {
        return vault.depositCommand(banknotes);
    }
}
