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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractEventListenerTest {

    @Mock
    private ContractRepository contractRepository;

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

        when(contractRepository.findByVehicleIdAndEtat("v1", ContractState.EN_ATTENTE)).thenReturn(List.of(c));

        listener.onVehicleStateChanged(new VehicleStateChangedEvent("v1", com.location.location_voitures.api.enums.VehicleState.EN_PANNE));

        verify(contractRepository, times(1)).saveAll(anyList());
        // The listener modifies the returned contract objects before saving, so the original object should be updated
        assertEquals(ContractState.ANNULE, c.getEtat());
    }
}
