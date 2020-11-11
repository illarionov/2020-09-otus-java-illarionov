package ru.x0xdc.otus.java.atm;

import ru.x0xdc.otus.java.atm.model.Banknote;
import ru.x0xdc.otus.java.atm.model.Change;
import ru.x0xdc.otus.java.atm.model.Result;
import ru.x0xdc.otus.java.atm.model.Status;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AtmImpl implements Atm {

    private final DispensingStrategy dispensingStrategy;

    private final MoneyVault moneyVault;

    public AtmImpl() {
        this(Change.empty());
    }

    public AtmImpl(Change initialQuantities) {
        this(new GreedyDispensingStrategy(), new MoneyVault(initialQuantities));
    }

    public AtmImpl(DispensingStrategy dispensingStrategy, MoneyVault moneyVault) {
        Objects.requireNonNull(dispensingStrategy, "Dispensing strategy required");
        Objects.requireNonNull(moneyVault, "moneyVault should not be null");
        this.dispensingStrategy = dispensingStrategy;
        this.moneyVault = moneyVault;
    }

    @Override
    public Result<List<Banknote>> withdraw(int sum) {
        if (sum < 0) throw new IllegalArgumentException("Sum must be non-negative");
        if (sum == 0) {
            return Result.success(Collections.emptyList());
        }

        Change toDispense;

        try {
            toDispense = dispensingStrategy.calculateDispense(moneyVault.getLeftover(), sum);
        } catch (DispenseException e) {
            return Result.failed(e);
        }

        return moneyVault.dispense(toDispense);
    }

    @Override
    public Result<Void> deposit(List<Banknote> banknotes) {
        Objects.requireNonNull(banknotes, "Banknotes should not be null");
        if (banknotes.isEmpty()) {
            return Result.success(null);
        }

        Result<Change> result = moneyVault.deposit(banknotes);
        return result.getStatus() == Status.SUCCESS ? Result.success(null) : Result.failed(result.getError());
    }

    @Override
    public int getAvailableBalance() {
        return moneyVault.getAvailableBalance();
    }
}
