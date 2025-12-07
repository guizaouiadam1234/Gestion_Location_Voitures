package com.location.location_voitures.api.events;

import com.location.location_voitures.api.enums.ContractState;
import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractEventListenerTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private com.location.location_voitures.api.service.state.StateTransitionService stateTransitionService;

    @InjectMocks
    private ContractEventListener listener;

    @Test
    void onVehicleStateChanged_shouldCancelPendingContracts() {
        Contract c = new Contract();
        c.setId("c1");
        c.setVehicleId("v1");
        c.setClientId("client1");
        c.setDateDebut(LocalDate.now());
        c.setDateFin(LocalDate.now().plusDays(2));
        c.setEtat(ContractState.EN_ATTENTE);

        // listener delegates to StateTransitionService when vehicle is in panne
        listener.onVehicleStateChanged(new VehicleStateChangedEvent("v1", com.location.location_voitures.api.enums.VehicleState.EN_PANNE));

        verify(stateTransitionService, times(1)).applyForVehiclePanne("v1");
    }
}
