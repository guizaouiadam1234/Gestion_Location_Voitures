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

    @Test
    void getAllClients_shouldReturnList() {
        when(clientRepository.findAll()).thenReturn(java.util.List.of(new Client(), new Client()));

        java.util.List<Client> list = clientService.getAllClients();
        assertEquals(2, list.size());
    }

    @Test
    void getClientById_shouldReturnOptional() {
        Client c = new Client();
        c.setId("cx");
        when(clientRepository.findById("cx")).thenReturn(Optional.of(c));

        Optional<Client> found = clientService.getClientById("cx");
        assertTrue(found.isPresent());
        assertEquals("cx", found.get().getId());
    }

    @Test
    void deleteClient_shouldCallRepository() {
        // verify delete call
        clientService.deleteClient("to-delete");
        org.mockito.Mockito.verify(clientRepository).deleteById("to-delete");
    }

    @Test
    void updateClient_shouldSave_whenValid() {
        Client existing = new Client();
        existing.setId("c-1");
        when(clientRepository.findById("c-1")).thenReturn(Optional.of(existing));
        when(clientRepository.findByNomAndPrenomAndDateNaissance(any(), any(), any())).thenReturn(Optional.empty());
        when(clientRepository.findByNumeroPermis(any())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArgument(0));

        Client in = new Client();
        in.setId("c-1");
        in.setNom("New");
        in.setPrenom("Name");
        in.setNumeroPermis("PERM-OK");
        Client out = clientService.updateClient(in);
        assertEquals("c-1", out.getId());
        assertEquals("New", out.getNom());
    }

    @Test
    void updateClient_shouldThrow_whenConflictNumeroPermis() {
        Client existing = new Client();
        existing.setId("c-1");
        when(clientRepository.findById("c-1")).thenReturn(Optional.of(existing));

        Client other = new Client();
        other.setId("c-2");
        when(clientRepository.findByNumeroPermis("PERM-X")).thenReturn(Optional.of(other));

        Client in = new Client();
        in.setId("c-1");
        in.setNumeroPermis("PERM-X");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> clientService.updateClient(in));
        assertEquals("Ce numéro de permis est déjà utilisé.", ex.getMessage());
    }
}
