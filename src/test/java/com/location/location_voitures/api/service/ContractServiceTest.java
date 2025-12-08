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
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    void createContract_shouldThrow_whenVehicleMissing() {
        when(vehicleRepository.findById("missing")).thenReturn(Optional.empty());

        Contract c = new Contract();
        c.setVehicleId("missing");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> contractService.createContract(c));
        assertEquals("Véhicule introuvable.", ex.getMessage());
    }

    @Test
    void createContract_shouldSetDefaultState_whenNull() {
        Vehicle v = new Vehicle();
        v.setId("v-def");
        v.setEtat(com.location.location_voitures.api.enums.VehicleState.DISPONIBLE);
        when(vehicleRepository.findById("v-def")).thenReturn(Optional.of(v));

        when(contractRepository.save(any(Contract.class))).thenAnswer(i -> i.getArgument(0));

        Contract c = new Contract();
        c.setVehicleId("v-def");
        Contract saved = contractService.createContract(c);
        assertEquals(com.location.location_voitures.api.enums.ContractState.EN_ATTENTE, saved.getEtat());
    }

    @Test
    void getAllContracts_shouldReturnList() {
        when(contractRepository.findAll()).thenReturn(java.util.List.of(new Contract(), new Contract()));
        java.util.List<Contract> list = contractService.getAllContracts();
        assertEquals(2, list.size());
    }

    @Test
    void getById_shouldReturnOptional() {
        Contract c = new Contract();
        c.setId("ctr1");
        when(contractRepository.findById("ctr1")).thenReturn(Optional.of(c));

        Optional<Contract> found = contractService.getById("ctr1");
        assertTrue(found.isPresent());
        assertEquals("ctr1", found.get().getId());
    }

    @Test
    void updateContract_shouldSave() {
        Contract c = new Contract();
        c.setId("u1");
        when(contractRepository.save(any(Contract.class))).thenAnswer(i -> i.getArgument(0));

        Contract out = contractService.updateContract(c);
        assertEquals("u1", out.getId());
    }
}
