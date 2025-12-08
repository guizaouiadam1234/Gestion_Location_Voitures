package com.location.location_voitures.api.service.mapper;

import com.location.location_voitures.api.dto.ContractDTO;
import com.location.location_voitures.api.model.Contract;

public class ContractMapperImplManual implements ContractMapper {

    @Override
    public Contract toEntity(ContractDTO dto) {
        if (dto == null) return null;
        Contract c = new Contract();
        c.setId(dto.getId());
        c.setClientId(dto.getClientId());
        c.setVehicleId(dto.getVehicleId());
        c.setDateDebut(dto.getDateDebut());
        c.setDateFin(dto.getDateFin());
        c.setEtat(dto.getEtat());
        return c;
    }

    @Override
    public ContractDTO toDto(Contract c) {
        if (c == null) return null;
        ContractDTO dto = new ContractDTO();
        dto.setId(c.getId());
        dto.setClientId(c.getClientId());
        dto.setVehicleId(c.getVehicleId());
        dto.setDateDebut(c.getDateDebut());
        dto.setDateFin(c.getDateFin());
        dto.setEtat(c.getEtat());
        return dto;
    }
}
