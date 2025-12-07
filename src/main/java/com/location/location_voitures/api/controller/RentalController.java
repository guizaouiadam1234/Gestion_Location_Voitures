package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.ClientDTO;
import com.location.location_voitures.api.dto.ContractDTO;
import com.location.location_voitures.api.model.Client;
import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.service.RentalFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalFacade rentalFacade;
    private final com.location.location_voitures.api.service.mapper.ClientMapper clientMapper;
    private final com.location.location_voitures.api.service.mapper.ContractMapper contractMapper;

    @PostMapping
    public ResponseEntity<ContractDTO> createRentalWithNewClient(@Valid @RequestBody RentalRequest request) {
        Client client = clientMapper.toEntity(request.getClient());

        Contract contract = contractMapper.toEntity(request.getContract());

        Contract saved = rentalFacade.createRentalAndClient(client, contract);
        return ResponseEntity.status(201).body(contractMapper.toDto(saved));
    }

    @PostMapping("/with-client/{clientId}")
    public ResponseEntity<ContractDTO> createRentalForExistingClient(@PathVariable String clientId, @Valid @RequestBody ContractDTO contractDTO) {
        Contract contract = contractMapper.toEntity(contractDTO);
        Contract saved = rentalFacade.createRentalWithExistingClient(clientId, contract.getVehicleId(), contract);
        return ResponseEntity.status(201).body(contractMapper.toDto(saved));
    }

    public static class RentalRequest {
        @NotNull
        @Valid
        private ClientDTO client;

        @NotNull
        @Valid
        private ContractDTO contract;

        public ClientDTO getClient() { return client; }
        public void setClient(ClientDTO client) { this.client = client; }
        public ContractDTO getContract() { return contract; }
        public void setContract(ContractDTO contract) { this.contract = contract; }
    }
}
