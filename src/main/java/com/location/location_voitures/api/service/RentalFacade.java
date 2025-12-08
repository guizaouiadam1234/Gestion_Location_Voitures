package com.location.location_voitures.api.service;

import com.location.location_voitures.api.model.Client;
import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.model.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalFacade {

    private final ClientService clientService;
    private final VehicleService vehicleService;
    private final ContractService contractService;

    public Contract createRentalWithExistingClient(String clientId, String vehicleId, Contract contract) {
        // verify vehicle exists and is rentable
        Optional<Vehicle> vOpt = vehicleService.getVehicleById(vehicleId);
        if (vOpt.isEmpty()) {
            throw new IllegalArgumentException("Véhicule introuvable.");
        }

        Vehicle v = vOpt.get();
        if (v.getEtat() != null && v.getEtat().name().equals("EN_PANNE")) {
            throw new IllegalArgumentException("Impossible de louer : véhicule en panne.");
        }

        contract.setClientId(clientId);
        contract.setVehicleId(vehicleId);

        return contractService.createContract(contract);
    }

    public Contract createRentalAndClient(Client client, Contract contract) {
        Client saved = clientService.createClient(client);
        contract.setClientId(saved.getId());
        if (contract.getVehicleId() == null) {
            throw new IllegalArgumentException("vehicleId must be provided in contract.");
        }
        return createRentalWithExistingClient(saved.getId(), contract.getVehicleId(), contract);
    }

    public Client updateClient(Client client) {
        return clientService.updateClient(client);
    }

    public Vehicle updateVehicle(Vehicle vehicle) {
        return vehicleService.updateVehicle(vehicle);
    }

    public Contract updateContract(Contract contract) {
        return contractService.updateContract(contract);
    }
}
