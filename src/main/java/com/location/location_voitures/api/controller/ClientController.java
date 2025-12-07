package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.ClientDTO;
import com.location.location_voitures.api.model.Client;
import com.location.location_voitures.api.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final com.location.location_voitures.api.service.mapper.ClientMapper clientMapper;

    @PostMapping
    public ResponseEntity<ClientDTO> create(@Valid @RequestBody ClientDTO dto) {
        Client client = clientMapper.toEntity(dto);

        Client saved = clientService.createClient(client);

        ClientDTO out = clientMapper.toDto(saved);
        return ResponseEntity.status(201).body(out);
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAll() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
}
