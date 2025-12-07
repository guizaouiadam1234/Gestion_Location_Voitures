package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.VehicleDTO;
import com.location.location_voitures.api.model.Vehicle;
import com.location.location_voitures.api.service.VehicleService;
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
public class VehicleControllerUnitTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    @Test
    void create_shouldReturnDtoWithId() {
        Vehicle saved = new Vehicle();
        saved.setId("v1");
        saved.setMarque("Peugeot");
        saved.setModele("308");
        saved.setImmatriculation("AA-111-BB");
        saved.setDateAcquisition(LocalDate.of(2023,5,1));

        when(vehicleService.createVehicle(any(Vehicle.class))).thenReturn(saved);

        VehicleDTO dto = new VehicleDTO();
        BeanUtils.copyProperties(saved, dto);

        VehicleDTO result = vehicleController.create(dto);
        assertEquals("v1", result.getId());
    }
}
