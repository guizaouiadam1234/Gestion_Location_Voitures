package com.location.location_voitures.api.events;

 
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractEventListener {

    private final com.location.location_voitures.api.service.state.StateTransitionService stateTransitionService;

    @EventListener
    public void onVehicleStateChanged(VehicleStateChangedEvent event) {
        if (event.getNewState() == null) return;
        if (event.getNewState().name().equals("EN_PANNE")) {
            stateTransitionService.applyForVehiclePanne(event.getVehicleId());
        }
    }
}
