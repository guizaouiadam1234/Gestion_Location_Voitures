package com.location.location_voitures.api.events;

import com.location.location_voitures.api.enums.VehicleState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VehicleStateChangedEvent {
    private final String vehicleId;
    private final VehicleState newState;
}
