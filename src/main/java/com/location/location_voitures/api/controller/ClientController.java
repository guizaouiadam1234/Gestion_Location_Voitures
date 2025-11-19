package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.ClientDTO;
import com.location.location_voitures.api.model.Client;
import com.location.location_voitures.api.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ClientDTO create(@RequestBody ClientDTO dto) {
        Client client = new Client();
        BeanUtils.copyProperties(dto, client);

        Client saved = clientService.createClient(client);

        BeanUtils.copyProperties(saved, dto);
        return dto;
    }

    @GetMapping
    public List<Client> getAll() {
        return clientService.getAllClients();
    }
}
