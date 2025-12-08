package com.location.location_voitures.api.service;

import com.location.location_voitures.api.model.Client;
import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.model.Vehicle;
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
public class RentalFacadeUnitTest {

    @Mock
    private ClientService clientService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private ContractService contractService;

    @InjectMocks
    private RentalFacade rentalFacade;

    @Test
    void createRentalWithExistingClient_shouldThrow_whenVehicleMissing() {
        when(vehicleService.getVehicleById("v1")).thenReturn(Optional.empty());

        Contract c = new Contract();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> rentalFacade.createRentalWithExistingClient("client1","v1", c));
        assertEquals("Véhicule introuvable.", ex.getMessage());
    }

    @Test
    void createRentalWithExistingClient_shouldThrow_whenVehicleEnPanne() {
        Vehicle v = new Vehicle();
        v.setId("v1");
        v.setEtat(com.location.location_voitures.api.enums.VehicleState.EN_PANNE);
        when(vehicleService.getVehicleById("v1")).thenReturn(Optional.of(v));

        Contract c = new Contract();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> rentalFacade.createRentalWithExistingClient("client1","v1", c));
        assertEquals("Impossible de louer : véhicule en panne.", ex.getMessage());
    }

    @Test
    void createRentalAndClient_shouldCreateClientAndContract() {
        Client saved = new Client();
        saved.setId("c-saved");
        when(clientService.createClient(any(Client.class))).thenReturn(saved);

        Vehicle v = new Vehicle();
        v.setId("v1");
        v.setEtat(com.location.location_voitures.api.enums.VehicleState.DISPONIBLE);
        when(vehicleService.getVehicleById("v1")).thenReturn(Optional.of(v));

        Contract contract = new Contract();
        contract.setVehicleId("v1");
        contract.setDateDebut(LocalDate.now());
        contract.setDateFin(LocalDate.now().plusDays(2));

        when(contractService.createContract(any(Contract.class))).thenAnswer(i -> i.getArgument(0));

        Contract result = rentalFacade.createRentalAndClient(new Client(), contract);
        assertEquals("v1", result.getVehicleId());
        assertEquals("c-saved", result.getClientId());
    }

    @Test
    void updateDelegates_shouldCallUnderlyingServices() {
        Client c = new Client(); c.setId("c1");
        when(clientService.updateClient(any(Client.class))).thenReturn(c);
        Client out = rentalFacade.updateClient(c);
        assertEquals("c1", out.getId());

        Vehicle v = new Vehicle(); v.setId("v1");
        when(vehicleService.updateVehicle(any(Vehicle.class))).thenReturn(v);
        Vehicle vo = rentalFacade.updateVehicle(v);
        assertEquals("v1", vo.getId());

        Contract ctr = new Contract(); ctr.setId("ctr1");
        when(contractService.updateContract(any(Contract.class))).thenReturn(ctr);
        Contract co = rentalFacade.updateContract(ctr);
        assertEquals("ctr1", co.getId());
    }
}
