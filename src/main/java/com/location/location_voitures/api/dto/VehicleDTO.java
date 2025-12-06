package com.location.location_voitures.api.dto;

import com.location.location_voitures.api.enums.VehicleState;
import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleDTO {
    private String id;
    private String marque;
    private String modele;
    private String motorisation;
    private String couleur;
    private String immatriculation;
    private LocalDate dateAcquisition;
    private VehicleState etat;
}
