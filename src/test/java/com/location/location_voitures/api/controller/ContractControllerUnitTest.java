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

        ContractDTO dto = new ContractDTO();
        dto.setClientId("c1");
        dto.setVehicleId("v1");
        dto.setDateDebut(LocalDate.now());
        dto.setDateFin(LocalDate.now().plusDays(1));

        ContractDTO result = contractController.create(dto);
        assertEquals("ctr1", result.getId());
    }

    @Test
    void getAll_shouldReturnList() {
        Contract c = new Contract();
        c.setId("ctr1");
        when(contractService.getAllContracts()).thenReturn(List.of(c));

        var list = contractController.getAll();
        assertEquals(1, list.size());
    }
}
