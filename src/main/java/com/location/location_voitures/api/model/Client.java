package com.location.location_voitures.api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    private String id;

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;

    @Indexed(unique = true)
    private String numeroPermis;

    private String adresse;
}
