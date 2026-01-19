package ru.otus.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dto.ClientDTO;

import java.util.List;

@Controller
@AllArgsConstructor
public class ClientController {

    private final DBServiceClient dbServiceClient;

    @GetMapping("/clients")
    public String getClientsPage() {
        List<ClientDTO> clientsDTO = dbServiceClient.findAll();
        return "clients";
    }
}