package com.location.location_voitures.api.dto;

import com.location.location_voitures.api.enums.ContractState;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ContractDTO {
    private String id;

    @NotBlank
    private String clientId;

    @NotBlank
    private String vehicleId;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    private ContractState etat;
}
