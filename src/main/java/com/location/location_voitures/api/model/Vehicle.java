package com.location.location_voitures.api.model;

import com.location.location_voitures.api.enums.VehicleState;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "vehicules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    private String id;

    private String marque;
    private String modele;
    private String motorisation;
    private String couleur;

    @Indexed(unique = true)
    private String immatriculation;

    private LocalDate dateAcquisition;

    private VehicleState etat;

}
