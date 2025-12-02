package ru.otus.mapper;

import org.springframework.stereotype.Component;
import ru.otus.dto.ClientDTO;
import ru.otus.entity.Address;
import ru.otus.entity.Client;
import ru.otus.entity.Phone;

import java.util.Collections;
import java.util.List;

@Component
public class ClientMapper {


    public ClientDTO toDTO(Client client) {
        Long id = client.id();
        String name = client.name();
        String address = client.address() != null ? client.address().street() : null;
        List<String> phones = client.phones() != null
                ? client.phones().stream().map(Phone::number).toList()
                : Collections.emptyList();

        return new ClientDTO(id, name, address, phones);
    }


    public Client toEntity(ClientDTO clientDTO) {
        Long id = clientDTO.id();
        String name = clientDTO.name();
        Address address = new Address(null, clientDTO.address());
        List<Phone> phones = clientDTO.phones().stream()
                .map(number -> new Phone(null, number))
                .toList();

        return new Client(id, name, address, phones);
    }
}