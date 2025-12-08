package com.location.location_voitures.api.service.mapper;

import com.location.location_voitures.api.dto.ClientDTO;
import com.location.location_voitures.api.model.Client;

public class ClientMapperImplManual implements ClientMapper {

    @Override
    public Client toEntity(ClientDTO dto) {
        if (dto == null) return null;
        Client c = new Client();
        c.setId(dto.getId());
        c.setNom(dto.getNom());
        c.setPrenom(dto.getPrenom());
        c.setDateNaissance(dto.getDateNaissance());
        c.setNumeroPermis(dto.getNumeroPermis());
        c.setAdresse(dto.getAdresse());
        return c;
    }

    @Override
    public ClientDTO toDto(Client c) {
        if (c == null) return null;
        ClientDTO dto = new ClientDTO();
        dto.setId(c.getId());
        dto.setNom(c.getNom());
        dto.setPrenom(c.getPrenom());
        dto.setDateNaissance(c.getDateNaissance());
        dto.setNumeroPermis(c.getNumeroPermis());
        dto.setAdresse(c.getAdresse());
        return dto;
    }
}
