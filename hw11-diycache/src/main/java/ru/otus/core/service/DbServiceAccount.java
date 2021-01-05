package ru.otus.core.service;

import ru.otus.core.model.Account;

import java.util.Optional;

public interface DbServiceAccount {
    String saveAccount(Account client);

    Optional<Account> getAccount(String id);
}
