package com.location.location_voitures.api.dto;

import com.location.location_voitures.api.enums.VehicleState;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class VehicleDTO {
    private String id;

    @NotBlank
    private String marque;

    @NotBlank
    private String modele;

    private String motorisation;

    private String couleur;

    @NotBlank
    private String immatriculation;

    @NotNull
    private LocalDate dateAcquisition;

    @NotNull
    private VehicleState etat;
}
