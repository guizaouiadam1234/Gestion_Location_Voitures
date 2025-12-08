package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.ContractDTO;
import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.service.ContractService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContractControllerUnitTest {

    @Mock
    private ContractService contractService;

    @Mock
    private com.location.location_voitures.api.service.mapper.ContractMapper contractMapper;

    @InjectMocks
    private ContractController contractController;

    @Test
    void create_shouldReturnDto() {
        Contract saved = new Contract();
        saved.setId("ctr1");
        saved.setClientId("c1");
        saved.setVehicleId("v1");
        saved.setDateDebut(LocalDate.now());
        saved.setDateFin(LocalDate.now().plusDays(1));

        when(contractService.createContract(any(Contract.class))).thenReturn(saved);
        when(contractMapper.toEntity(any(ContractDTO.class))).thenReturn(new Contract());

        ContractDTO dto = new ContractDTO();
        dto.setId("ctr1");
        dto.setClientId("c1");
        dto.setVehicleId("v1");
        dto.setDateDebut(LocalDate.now());
        dto.setDateFin(LocalDate.now().plusDays(1));
        when(contractMapper.toDto(saved)).thenReturn(dto);

        var response = contractController.create(dto);
        ContractDTO result = response.getBody();
        assertEquals("ctr1", result.getId());
    }

    @Test
    void getAll_shouldReturnList() {
        Contract c = new Contract();
        c.setId("ctr1");
        when(contractService.getAllContracts()).thenReturn(List.of(c));
        ContractDTO dto = new ContractDTO();
        dto.setId("ctr1");
        when(contractMapper.toDto(c)).thenReturn(dto);

        var listResponse = contractController.getAll();
        var list = listResponse.getBody();
        assertEquals(1, list.size());
    }

    @Test
    void delete_shouldReturnNoContent_andCallService() {
        contractController.delete("ctr1");
        org.mockito.Mockito.verify(contractService).deleteContract("ctr1");
    }
}
