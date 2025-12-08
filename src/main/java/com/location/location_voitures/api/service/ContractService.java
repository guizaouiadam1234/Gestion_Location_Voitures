package com.location.location_voitures.api.service;

import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.enums.ContractState;
import com.location.location_voitures.api.model.Vehicle;
import com.location.location_voitures.api.repository.ContractRepository;
import com.location.location_voitures.api.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;

    public Contract createContract(Contract contract) {
        // Business rule: cannot create contract if vehicle is EN_PANNE
        Optional<Vehicle> v = vehicleRepository.findById(contract.getVehicleId());
        if (v.isEmpty()) {
            throw new IllegalArgumentException("Véhicule introuvable.");
        }

        if (v.get().getEtat() != null && v.get().getEtat().name().equals("EN_PANNE")) {
            throw new IllegalArgumentException("Impossible de créer un contrat : véhicule en panne.");
        }

        if (contract.getEtat() == null) {
            contract.setEtat(ContractState.EN_ATTENTE);
        }

        return contractRepository.save(contract);
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Optional<Contract> getById(String id) {
        return contractRepository.findById(id);
    }

    public Contract updateContract(Contract contract) {
        return contractRepository.save(contract);
    }

    public void deleteContract(String id) {
        contractRepository.deleteById(id);
    }
}
