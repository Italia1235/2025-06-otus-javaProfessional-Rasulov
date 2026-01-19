package ru.otus.crm.service;

import ru.otus.dto.ClientDTO;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    ClientDTO saveClient(ClientDTO client);

    Optional<ClientDTO> getClient(long id);

    List<ClientDTO> findAll();
}
