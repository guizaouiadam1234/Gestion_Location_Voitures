package com.location.location_voitures.api.service.state;

import com.location.location_voitures.api.enums.ContractState;
import com.location.location_voitures.api.model.Contract;

public class CancelOnVehiclePanneStrategy implements ContractStateTransitionStrategy {

    @Override
    public boolean supports(String trigger) {
        return "VEHICLE_PANNE".equals(trigger);
    }

    @Override
    public void apply(Contract contract) {
        if (contract.getEtat() == ContractState.EN_ATTENTE) {
            contract.setEtat(ContractState.ANNULE);
        }
    }
}
