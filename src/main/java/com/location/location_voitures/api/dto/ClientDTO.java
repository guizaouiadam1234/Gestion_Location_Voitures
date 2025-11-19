package com.location.location_voitures.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClientDTO {
    private String id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String numeroPermis;
    private String adresse;
}
