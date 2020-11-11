package ru.x0xdc.otus.java;

import ru.x0xdc.otus.java.atm.Atm;
import ru.x0xdc.otus.java.atm.AtmImpl;
import ru.x0xdc.otus.java.atm.model.Banknote;
import ru.x0xdc.otus.java.atm.model.Result;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Result<List<Banknote>> withdrawResult;
        Result<Void> depositResult;

        Atm atm = new AtmImpl();

        System.out.println("Atm balance: " + atm.getAvailableBalance());

        depositResult = atm.deposit(List.of(Banknote.of(100), Banknote.of(100), Banknote.of(5000)));

        System.out.println("ATM deposit 5200: " + depositResult.getStatus() + "; balance after deposit: "
                + atm.getAvailableBalance());

        withdrawResult = atm.withdraw(200);

        System.out.println("ATM withdraw 200: " + withdrawResult.getStatus() + "; banknotes: "
                + withdrawResult.getResult() + "; balance after withdraw: " + atm.getAvailableBalance());

        withdrawResult = atm.withdraw(100);

        System.out.println("ATM withdraw 100: " + withdrawResult.getStatus() + "; error: "
                + withdrawResult.getError().getMessage());
    }
}
