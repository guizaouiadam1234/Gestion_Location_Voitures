package com.location.location_voitures.api.controller;

import com.location.location_voitures.api.dto.VehicleDTO;
import com.location.location_voitures.api.model.Vehicle;
import com.location.location_voitures.api.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final com.location.location_voitures.api.service.mapper.VehicleMapper vehicleMapper;

    @PostMapping
    public ResponseEntity<VehicleDTO> create(@Valid @RequestBody VehicleDTO dto) {
        Vehicle vehicle = vehicleMapper.toEntity(dto);

        Vehicle saved = vehicleService.createVehicle(vehicle);

        VehicleDTO out = vehicleMapper.toDto(saved);
        return ResponseEntity.status(201).body(out);
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAll() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable String id) {
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> update(@PathVariable String id, @Valid @RequestBody VehicleDTO dto) {
        Vehicle vehicle = vehicleMapper.toEntity(dto);
        vehicle.setId(id);

        Vehicle updated = vehicleService.updateVehicle(vehicle);

        VehicleDTO out = vehicleMapper.toDto(updated);
        return ResponseEntity.ok(out);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}
