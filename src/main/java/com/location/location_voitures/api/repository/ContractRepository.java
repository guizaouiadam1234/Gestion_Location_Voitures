package com.location.location_voitures.api.repository;

import com.location.location_voitures.api.enums.ContractState;
import com.location.location_voitures.api.model.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContractRepository extends MongoRepository<Contract, String> {

    List<Contract> findByVehicleIdAndEtat(String vehicleId, ContractState etat);

}
