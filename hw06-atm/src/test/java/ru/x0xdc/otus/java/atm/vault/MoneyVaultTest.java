package ru.x0xdc.otus.java.atm.vault;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.x0xdc.otus.java.atm.Atm;
import ru.x0xdc.otus.java.atm.AtmImpl;
import ru.x0xdc.otus.java.atm.DispensingStrategy;
import ru.x0xdc.otus.java.atm.model.Banknote;
import ru.x0xdc.otus.java.atm.model.Change;
import ru.x0xdc.otus.java.atm.model.Result;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MoneyVaultTest {

    @Test
    void withdrawShouldCallMoneyVaultWithdraw() {
        MoneyVault moneyVault = mock(MoneyVault.class);
        DispensingStrategy strategy = mock(DispensingStrategy.class);

        Change change = Change.builder().rub100(1).build();
        when(strategy.calculateDispense(Mockito.any(), Mockito.anyInt())).thenReturn(change);
        when(moneyVault.dispenseCommand(change)).thenReturn(Result.success(List.of(Banknote.of(100))));

        Atm atm = new AtmImpl(strategy, moneyVault);

        atm.withdraw(100);

        Mockito.verify(moneyVault).dispenseCommand(Mockito.any());
    }

    @Test
    void depositShouldCallMoneyVaultWithdraw() {
        MoneyVault moneyVault = mock(MoneyVault.class);
        DispensingStrategy strategy = mock(DispensingStrategy.class);

        when(moneyVault.depositCommand(Mockito.anyList())).thenReturn(Result.success(Change.empty()));

        Atm atm = new AtmImpl(strategy, moneyVault);

        atm.withdraw(100);

        Mockito.verify(moneyVault).dispenseCommand(Mockito.any());
    }

}