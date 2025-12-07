package com.location.location_voitures.api.service.state;

import com.location.location_voitures.api.enums.ContractState;
import com.location.location_voitures.api.model.Contract;

public class MarkLateStrategy implements ContractStateTransitionStrategy {

    @Override
    public boolean supports(String trigger) {
        return "MARK_LATE".equals(trigger);
    }

    @Override
    public void apply(Contract contract) {
        if (contract.getEtat() != ContractState.EN_RETARD && contract.getEtat() != ContractState.TERMINE) {
            contract.setEtat(ContractState.EN_RETARD);
        }
    }
}
