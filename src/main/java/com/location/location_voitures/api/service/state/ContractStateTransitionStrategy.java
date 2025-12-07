package com.location.location_voitures.api.service.state;

import com.location.location_voitures.api.model.Contract;

public interface ContractStateTransitionStrategy {

    boolean supports(String trigger);

    void apply(Contract contract);
}
