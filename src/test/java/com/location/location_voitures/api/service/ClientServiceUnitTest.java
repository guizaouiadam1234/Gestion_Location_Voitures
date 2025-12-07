package com.location.location_voitures.api.service;

import com.location.location_voitures.api.model.Client;
import com.location.location_voitures.api.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceUnitTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void createClient_shouldThrow_whenClientExistsByNameAndDob() {
        Client existing = new Client();
        existing.setId("c1");
        existing.setNom("Dupont");
        existing.setPrenom("Paul");
        existing.setDateNaissance(LocalDate.of(1980,1,1));

        when(clientRepository.findByNomAndPrenomAndDateNaissance("Dupont","Paul",LocalDate.of(1980,1,1)))
                .thenReturn(Optional.of(existing));

        Client toCreate = new Client();
        toCreate.setNom("Dupont");
        toCreate.setPrenom("Paul");
        toCreate.setDateNaissance(LocalDate.of(1980,1,1));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> clientService.createClient(toCreate));
        assertEquals("Le client existe déjà.", ex.getMessage());
    }

    @Test
    void createClient_shouldThrow_whenNumeroPermisAlreadyUsed() {
        when(clientRepository.findByNomAndPrenomAndDateNaissance(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(clientRepository.existsByNumeroPermis("PERM-1")).thenReturn(true);

        Client toCreate = new Client();
        toCreate.setNom("Durand");
        toCreate.setPrenom("Alice");
        toCreate.setDateNaissance(LocalDate.of(1990,5,5));
        toCreate.setNumeroPermis("PERM-1");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> clientService.createClient(toCreate));
        assertEquals("Ce numéro de permis est déjà utilisé.", ex.getMessage());
    }

    @Test
    void createClient_shouldSave_whenValid() {
        when(clientRepository.findByNomAndPrenomAndDateNaissance(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(clientRepository.existsByNumeroPermis("PERM-2")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> {
            Client c = i.getArgument(0);
            c.setId("saved-1");
            return c;
        });

        Client toCreate = new Client();
        toCreate.setNom("Martin");
        toCreate.setPrenom("Jean");
        toCreate.setDateNaissance(LocalDate.of(1985,2,2));
        toCreate.setNumeroPermis("PERM-2");

        Client saved = clientService.createClient(toCreate);
        assertNotNull(saved.getId());
        assertEquals("saved-1", saved.getId());
    }
}
