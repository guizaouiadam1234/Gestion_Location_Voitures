package com.location.location_voitures.api.service.mapper;

import com.location.location_voitures.api.dto.ContractDTO;
import com.location.location_voitures.api.model.Contract;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    Contract toEntity(ContractDTO dto);
    ContractDTO toDto(Contract c);
}
