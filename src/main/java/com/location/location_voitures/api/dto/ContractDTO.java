package com.location.location_voitures.api.dto;

import com.location.location_voitures.api.enums.ContractState;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractDTO {
    private String id;
    private String clientId;
    private String vehicleId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private ContractState etat;
}
