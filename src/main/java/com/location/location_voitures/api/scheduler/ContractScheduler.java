package com.location.location_voitures.api.scheduler;

import com.location.location_voitures.api.service.state.StateTransitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractScheduler {

    private final StateTransitionService stateTransitionService;

    @Scheduled(fixedDelayString = "${app.scheduler.contracts-ms:60000}")
    public void checkLateContracts() {
        stateTransitionService.markLateContracts();
    }
}
