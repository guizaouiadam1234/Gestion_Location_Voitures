package com.location.location_voitures.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ClientDTO {
    private String id;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotNull
    @Past
    private LocalDate dateNaissance;

    @NotBlank
    private String numeroPermis;

    private String adresse;
}
