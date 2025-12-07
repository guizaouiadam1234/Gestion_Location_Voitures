package com.location.location_voitures.api.service.mapper;

import com.location.location_voitures.api.dto.VehicleDTO;
import com.location.location_voitures.api.model.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    Vehicle toEntity(VehicleDTO dto);
    VehicleDTO toDto(Vehicle v);
}
