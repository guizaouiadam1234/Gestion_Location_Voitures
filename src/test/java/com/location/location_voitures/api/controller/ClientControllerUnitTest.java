package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.ClientDTO;
import com.location.location_voitures.api.model.Client;
import com.location.location_voitures.api.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientControllerUnitTest {

    @Mock
    private ClientService clientService;

    @Mock
    private com.location.location_voitures.api.service.mapper.ClientMapper clientMapper;

    @InjectMocks
    private ClientController clientController;

    @Test
    void create_shouldReturnDtoWithId() {
        Client saved = new Client();
        saved.setId("c1");
        saved.setNom("Dupont");
        saved.setPrenom("Paul");
        saved.setDateNaissance(LocalDate.of(1980,1,1));

        when(clientService.createClient(any(Client.class))).thenReturn(saved);
        when(clientMapper.toEntity(any(ClientDTO.class))).thenReturn(new Client());

        ClientDTO dto = new ClientDTO();
        BeanUtils.copyProperties(saved, dto);
        when(clientMapper.toDto(saved)).thenReturn(dto);

        var response = clientController.create(dto);
        ClientDTO result = response.getBody();
        assertEquals("c1", result.getId());
    }

    @Test
    void delete_shouldReturnNoContent_andCallService() {
        clientController.delete("c1");
        org.mockito.Mockito.verify(clientService).deleteClient("c1");
    }
}
