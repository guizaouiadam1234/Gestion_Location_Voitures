package com.location.location_voitures.api.service.state;

import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.repository.ContractRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StateTransitionService {

    private final ContractRepository contractRepository;

    private final List<ContractStateTransitionStrategy> strategies = new ArrayList<>();

    public StateTransitionService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
        // register default strategies
        strategies.add(new CancelOnVehiclePanneStrategy());
        strategies.add(new MarkLateStrategy());
    }

    public void applyForVehiclePanne(String vehicleId) {
        List<Contract> contracts = contractRepository.findByVehicleIdAndEtat(vehicleId, com.location.location_voitures.api.enums.ContractState.EN_ATTENTE);
        for (Contract c : contracts) {
            apply("VEHICLE_PANNE", c);
        }
        if (!contracts.isEmpty()) contractRepository.saveAll(contracts);
    }

    public void markLateContracts() {
        List<Contract> toCheck = contractRepository.findAll();
        LocalDate now = LocalDate.now();
        List<Contract> changed = new ArrayList<>();
        for (Contract c : toCheck) {
            if (c.getDateFin() != null && c.getDateFin().isBefore(now)) {
                apply("MARK_LATE", c);
                changed.add(c);
            }
        }
        if (!changed.isEmpty()) contractRepository.saveAll(changed);
    }

    private void apply(String trigger, Contract c) {
        for (ContractStateTransitionStrategy s : strategies) {
            if (s.supports(trigger)) {
                s.apply(c);
            }
        }
    }
}
