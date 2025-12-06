package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.ContractDTO;
import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.service.ContractService;
import com.location.location_voitures.api.service.mapper.ContractMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contrats")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public ContractDTO create(@RequestBody ContractDTO dto) {
        Contract c = ContractMapper.toEntity(dto);
        Contract saved = contractService.createContract(c);
        return ContractMapper.toDto(saved);
    }

    @GetMapping
    public List<ContractDTO> getAll() {
        List<Contract> all = contractService.getAllContracts();
        return all.stream().map(ContractMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ContractDTO getById(@PathVariable String id) {
        return contractService.getById(id).map(ContractMapper::toDto).orElse(null);
    }

    @PutMapping("/{id}")
    public ContractDTO update(@PathVariable String id, @RequestBody ContractDTO dto) {
        Contract c = ContractMapper.toEntity(dto);
        c.setId(id);
        Contract updated = contractService.updateContract(c);
        return ContractMapper.toDto(updated);
    }
}
