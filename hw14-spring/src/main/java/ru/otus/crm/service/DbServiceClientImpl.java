package ru.otus.crm.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.ClientDTO;
import ru.otus.mapper.ClientMapper;
import ru.otus.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);


    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional
    public ClientDTO saveClient(ClientDTO clientDTO) {
        var entity = clientMapper.toEntity(clientDTO);
        var savedClient = clientRepository.save(entity);
        log.info("saved client: {}", savedClient);
        return clientMapper.toDTO(savedClient);
    }

    @Override
    public Optional<ClientDTO> getClient(long id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDTO);
    }

    @Override
    public List<ClientDTO> findAll() {
        var clients = new ArrayList<>(clientRepository.findAll())
                .stream().map(clientMapper::toDTO).toList();
        log.info("clientList:{}", clients);
        return clients;
    }
}