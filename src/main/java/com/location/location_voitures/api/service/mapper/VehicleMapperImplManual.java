package com.location.location_voitures.api.service.mapper;

import com.location.location_voitures.api.dto.VehicleDTO;
import com.location.location_voitures.api.model.Vehicle;

public class VehicleMapperImplManual implements VehicleMapper {

    @Override
    public Vehicle toEntity(VehicleDTO dto) {
        if (dto == null) return null;
        Vehicle v = new Vehicle();
        v.setId(dto.getId());
        v.setMarque(dto.getMarque());
        v.setModele(dto.getModele());
        v.setMotorisation(dto.getMotorisation());
        v.setCouleur(dto.getCouleur());
        v.setImmatriculation(dto.getImmatriculation());
        v.setDateAcquisition(dto.getDateAcquisition());
        v.setEtat(dto.getEtat());
        return v;
    }

    @Override
    public VehicleDTO toDto(Vehicle v) {
        if (v == null) return null;
        VehicleDTO dto = new VehicleDTO();
        dto.setId(v.getId());
        dto.setMarque(v.getMarque());
        dto.setModele(v.getModele());
        dto.setMotorisation(v.getMotorisation());
        dto.setCouleur(v.getCouleur());
        dto.setImmatriculation(v.getImmatriculation());
        dto.setDateAcquisition(v.getDateAcquisition());
        dto.setEtat(v.getEtat());
        return dto;
    }
}
