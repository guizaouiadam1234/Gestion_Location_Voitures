package com.location.location_voitures.api.service;

import com.location.location_voitures.api.model.Client;
import com.location.location_voitures.api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client createClient(Client client) {

        Optional<Client> existing = clientRepository
                .findByNomAndPrenomAndDateNaissance(
                        client.getNom(),
                        client.getPrenom(),
                        client.getDateNaissance()
                );

        if (existing.isPresent()) {
            throw new IllegalArgumentException("Le client existe déjà.");
        }

        // Règle métier : numéro de permis unique
        if (clientRepository.existsByNumeroPermis(client.getNumeroPermis())) {
            throw new IllegalArgumentException("Ce numéro de permis est déjà utilisé.");
        }

        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }

    public void deleteClient(String id) {
        clientRepository.deleteById(id);
    }
}