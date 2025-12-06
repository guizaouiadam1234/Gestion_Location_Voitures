package com.location.location_voitures.api.events;

import com.location.location_voitures.api.enums.ContractState;
import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractEventListener {

    private final ContractRepository contractRepository;

    @EventListener
    public void onVehicleStateChanged(VehicleStateChangedEvent event) {
        if (event.getNewState() == null) return;
        if (event.getNewState().name().equals("EN_PANNE")) {
            List<Contract> toCancel = contractRepository.findByVehicleIdAndEtat(event.getVehicleId(), ContractState.EN_ATTENTE);
            if (!toCancel.isEmpty()) {
                for (Contract c : toCancel) {
                    c.setEtat(ContractState.ANNULE);
                }
                contractRepository.saveAll(toCancel);
            }
        }
    }
}
