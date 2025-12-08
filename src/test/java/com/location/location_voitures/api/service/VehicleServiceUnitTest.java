package com.location.location_voitures.api.service;

import com.location.location_voitures.api.enums.VehicleState;
import com.location.location_voitures.api.model.Vehicle;
import com.location.location_voitures.api.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceUnitTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void createVehicle_shouldThrow_whenImmatriculationExists() {
        when(vehicleRepository.existsByImmatriculation("AA-111-BB")).thenReturn(true);

        Vehicle v = new Vehicle();
        v.setImmatriculation("AA-111-BB");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> vehicleService.createVehicle(v));
        assertEquals("Un véhicule avec cette immatriculation existe déjà.", ex.getMessage());
    }

    @Test
    void createVehicle_shouldSave_whenValid() {
        when(vehicleRepository.existsByImmatriculation(any())).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> {
            Vehicle vv = i.getArgument(0);
            vv.setId("v-saved");
            return vv;
        });

        Vehicle v = new Vehicle();
        v.setImmatriculation("BB-222-CC");

        Vehicle saved = vehicleService.createVehicle(v);
        assertNotNull(saved.getId());
        assertEquals("v-saved", saved.getId());
    }

    @Test
    void updateVehicle_shouldThrow_whenImmatriculationUsedByOther() {
        Vehicle other = new Vehicle();
        other.setId("other-id");
        other.setImmatriculation("ZZ-999-YY");

        when(vehicleRepository.findByImmatriculation("ZZ-999-YY")).thenReturn(Optional.of(other));

        Vehicle toUpdate = new Vehicle();
        toUpdate.setId("mine");
        toUpdate.setImmatriculation("ZZ-999-YY");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> vehicleService.updateVehicle(toUpdate));
        assertEquals("Immatriculation déjà utilisée par un autre véhicule.", ex.getMessage());
    }

    @Test
    void updateVehicle_shouldPublishEvent_whenStateChangesToEnPanne() {
        Vehicle current = new Vehicle();
        current.setId("v1");
        current.setEtat(VehicleState.DISPONIBLE);

        Vehicle updated = new Vehicle();
        updated.setId("v1");
        updated.setEtat(VehicleState.EN_PANNE);
        updated.setImmatriculation("IM-1");

        when(vehicleRepository.findById("v1")).thenReturn(Optional.of(current));
        when(vehicleRepository.findByImmatriculation(any())).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> i.getArgument(0));

        Vehicle result = vehicleService.updateVehicle(updated);

        assertEquals(VehicleState.EN_PANNE, result.getEtat());
        // capture published event and assert its type and payload
        org.mockito.ArgumentCaptor<Object> captor = org.mockito.ArgumentCaptor.forClass(Object.class);
        verify(publisher, times(1)).publishEvent(captor.capture());
        Object ev = captor.getValue();
        assertTrue(ev instanceof com.location.location_voitures.api.events.VehicleStateChangedEvent);
        com.location.location_voitures.api.events.VehicleStateChangedEvent vse = (com.location.location_voitures.api.events.VehicleStateChangedEvent) ev;
        assertEquals("v1", vse.getVehicleId());
        assertEquals(VehicleState.EN_PANNE, vse.getNewState());
    }

    @Test
    void getAllVehicles_shouldReturnList() {
        when(vehicleRepository.findAll()).thenReturn(java.util.List.of(new Vehicle(), new Vehicle(), new Vehicle()));
        java.util.List<Vehicle> list = vehicleService.getAllVehicles();
        assertEquals(3, list.size());
    }

    @Test
    void getVehicleById_shouldReturnOptional() {
        Vehicle v = new Vehicle();
        v.setId("vid");
        when(vehicleRepository.findById("vid")).thenReturn(Optional.of(v));

        Optional<Vehicle> found = vehicleService.getVehicleById("vid");
        assertTrue(found.isPresent());
        assertEquals("vid", found.get().getId());
    }

    @Test
    void getByImmatriculation_shouldReturnOptional() {
        Vehicle v = new Vehicle();
        v.setId("v2");
        when(vehicleRepository.findByImmatriculation("IM-1")).thenReturn(Optional.of(v));

        Optional<Vehicle> found = vehicleService.getByImmatriculation("IM-1");
        assertTrue(found.isPresent());
        assertEquals("v2", found.get().getId());
    }

    @Test
    void deleteVehicle_shouldCallRepository() {
        vehicleService.deleteVehicle("del-id");
        verify(vehicleRepository).deleteById("del-id");
    }

    @Test
    void updateVehicle_shouldNotPublish_whenNoStateChange() {
        Vehicle current = new Vehicle();
        current.setId("v3");
        current.setEtat(com.location.location_voitures.api.enums.VehicleState.DISPONIBLE);

        Vehicle updated = new Vehicle();
        updated.setId("v3");
        updated.setEtat(com.location.location_voitures.api.enums.VehicleState.DISPONIBLE);
        updated.setImmatriculation("IM-3");

        when(vehicleRepository.findById("v3")).thenReturn(Optional.of(current));
        when(vehicleRepository.findByImmatriculation(any())).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> i.getArgument(0));

        Vehicle result = vehicleService.updateVehicle(updated);
        assertEquals(com.location.location_voitures.api.enums.VehicleState.DISPONIBLE, result.getEtat());
        verify(publisher, never()).publishEvent(any());
    }
}
