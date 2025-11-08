package ru.otus.services;


import ru.otus.crm.model.Client;

import java.util.List;

public interface ClientService {
    Client createClient(Client client);
    List<Client> getAllClients();
}