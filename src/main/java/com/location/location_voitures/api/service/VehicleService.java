package com.location.location_voitures.api.service;

import com.location.location_voitures.api.model.Vehicle;
import com.location.location_voitures.api.repository.VehicleRepository;
import com.location.location_voitures.api.events.VehicleStateChangedEvent;
import org.springframework.context.ApplicationEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ApplicationEventPublisher publisher;

    public Vehicle createVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByImmatriculation(vehicle.getImmatriculation())) {
            throw new IllegalArgumentException("Un véhicule avec cette immatriculation existe déjà.");
        }
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(String id) {
        return vehicleRepository.findById(id);
    }

    public Optional<Vehicle> getByImmatriculation(String immatriculation) {
        return vehicleRepository.findByImmatriculation(immatriculation);
    }

    public Vehicle updateVehicle(Vehicle vehicle) {
        // Load current vehicle to detect state changes
        Optional<Vehicle> currentOpt = Optional.empty();
        if (vehicle.getId() != null) {
            currentOpt = vehicleRepository.findById(vehicle.getId());
        }

        // Ensure immatriculation uniqueness if changed
        if (vehicle.getImmatriculation() != null) {
            Optional<Vehicle> existing = vehicleRepository.findByImmatriculation(vehicle.getImmatriculation());
            if (existing.isPresent() && !existing.get().getId().equals(vehicle.getId())) {
                throw new IllegalArgumentException("Immatriculation déjà utilisée par un autre véhicule.");
            }
        }

        Vehicle saved = vehicleRepository.save(vehicle);

        // If vehicle changed to EN_PANNE, publish event to cancel related contracts
        if (currentOpt.isPresent()) {
            Vehicle current = currentOpt.get();
            if (current.getEtat() != saved.getEtat() && saved.getEtat() != null && saved.getEtat().name().equals("EN_PANNE")) {
                // publish event handled by ContractEventListener
                publisher.publishEvent(new VehicleStateChangedEvent(saved.getId(), saved.getEtat()));
            }
        }

        return saved;
    }

    public void deleteVehicle(String id) {
        vehicleRepository.deleteById(id);
    }
}
