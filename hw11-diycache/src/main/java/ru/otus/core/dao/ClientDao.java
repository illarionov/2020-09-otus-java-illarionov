package ru.otus.core.dao;

import ru.otus.core.model.Client;

import java.util.List;

public interface ClientDao extends BaseDao<Client, Long> {

    List<Client> findAll();
}
