package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.VehicleDTO;
import com.location.location_voitures.api.model.Vehicle;
import com.location.location_voitures.api.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public VehicleDTO create(@RequestBody VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(dto, vehicle);

        Vehicle saved = vehicleService.createVehicle(vehicle);

        BeanUtils.copyProperties(saved, dto);
        return dto;
    }

    @GetMapping
    public List<Vehicle> getAll() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable String id) {
        return vehicleService.getVehicleById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public VehicleDTO update(@PathVariable String id, @RequestBody VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(dto, vehicle);
        vehicle.setId(id);

        Vehicle updated = vehicleService.updateVehicle(vehicle);

        BeanUtils.copyProperties(updated, dto);
        return dto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        vehicleService.deleteVehicle(id);
    }
}
