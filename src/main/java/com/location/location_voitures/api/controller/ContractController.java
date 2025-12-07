package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.ContractDTO;
import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.service.ContractService;
import com.location.location_voitures.api.service.mapper.ContractMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ResponseEntity<ContractDTO> create(@Valid @RequestBody ContractDTO dto) {
        Contract c = ContractMapper.toEntity(dto);
        Contract saved = contractService.createContract(c);
        return ResponseEntity.status(201).body(ContractMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getAll() {
        List<Contract> all = contractService.getAllContracts();
        return ResponseEntity.ok(all.stream().map(ContractMapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getById(@PathVariable String id) {
        return contractService.getById(id)
                .map(ContractMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContractDTO> update(@PathVariable String id, @Valid @RequestBody ContractDTO dto) {
        Contract c = ContractMapper.toEntity(dto);
        c.setId(id);
        Contract updated = contractService.updateContract(c);
        return ResponseEntity.ok(ContractMapper.toDto(updated));
    }
}
