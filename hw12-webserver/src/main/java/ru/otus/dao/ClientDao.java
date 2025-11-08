package ru.otus.dao;

import ru.otus.crm.model.Client;

import java.util.List;


public interface ClientDao {

    Client save(Client client);

    List<Client> findAll();

}
