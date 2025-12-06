package com.location.location_voitures.api.model;

import com.location.location_voitures.api.enums.ContractState;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "contrats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    private String id;

    private String clientId;
    private String vehicleId;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    private ContractState etat;
}
