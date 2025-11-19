package com.location.location_voitures.api.repository;

import com.location.location_voitures.api.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {

    Optional<Client> findByNomAndPrenomAndDateNaissance(String nom, String prenom, LocalDate dateNaissance);

    boolean existsByNumeroPermis(String numeroPermis);
}