package ru.otus.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dto.ClientDTO;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ClientApiController {

    private final DBServiceClient dbServiceClient;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return dbServiceClient.findAll();
    }

    @PostMapping("/clients")
    public ClientDTO createClient(@RequestBody ClientDTO clientDTO) {
        return dbServiceClient.saveClient(clientDTO);
    }
}