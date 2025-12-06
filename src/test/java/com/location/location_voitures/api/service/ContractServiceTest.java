package com.location.location_voitures.api.service;

import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.model.Vehicle;
import com.location.location_voitures.api.repository.ContractRepository;
import com.location.location_voitures.api.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private ContractService contractService;

    @Test
    void createContract_shouldThrow_whenVehicleEnPanne() {
        Vehicle v = new Vehicle();
        v.setId("v1");
        v.setEtat(null);
        // set enum value as String to avoid import complexities
        v.setEtat(null);

        // use a vehicle with EN_PANNE via setting its enum name using reflection not necessary; instead create vehicle with getEtat().name() check bypassed by setting a simple object
        Vehicle panne = new Vehicle();
        panne.setId("v-p");
        // set enum by name via valueOf
        panne.setEtat(com.location.location_voitures.api.enums.VehicleState.EN_PANNE);

        when(vehicleRepository.findById("v-p")).thenReturn(Optional.of(panne));

        Contract c = new Contract();
        c.setVehicleId("v-p");
        c.setClientId("c1");
        c.setDateDebut(LocalDate.now());
        c.setDateFin(LocalDate.now().plusDays(3));

        assertThrows(IllegalArgumentException.class, () -> contractService.createContract(c));
    }

    @Test
    void createContract_shouldSave_whenVehicleAvailable() {
        Vehicle v = new Vehicle();
        v.setId("v-ok");
        v.setEtat(com.location.location_voitures.api.enums.VehicleState.DISPONIBLE);

        when(vehicleRepository.findById("v-ok")).thenReturn(Optional.of(v));

        Contract c = new Contract();
        c.setVehicleId("v-ok");
        c.setClientId("c1");
        c.setDateDebut(LocalDate.now());
        c.setDateFin(LocalDate.now().plusDays(3));

        when(contractRepository.save(any(Contract.class))).thenAnswer(i -> i.getArgument(0));

        Contract saved = contractService.createContract(c);

        assertEquals(c.getVehicleId(), saved.getVehicleId());
    }
}
