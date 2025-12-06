package com.location.location_voitures.api.repository;

import com.location.location_voitures.api.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    boolean existsByImmatriculation(String immatriculation);

    Optional<Vehicle> findByImmatriculation(String immatriculation);
}
