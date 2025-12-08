
import com.location.location_voitures.api.enums.ContractState;
import com.location.location_voitures.api.enums.VehicleState;
import com.location.location_voitures.api.model.Client;
import com.location.location_voitures.api.model.Contract;
import com.location.location_voitures.api.model.Vehicle;
import com.location.location_voitures.api.service.ClientService;
import com.location.location_voitures.api.service.ContractService;
import com.location.location_voitures.api.service.VehicleService;
import com.location.location_voitures.api.repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.data.mongodb.uri=mongodb://localhost:27017/location-voitures-test")
@DirtiesContext
public class EmbeddedMongoIntegrationTest {

    @Autowired
    VehicleService vehicleService;

    @Autowired
    ClientService clientService;

    @Autowired
    ContractService contractService;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    com.location.location_voitures.api.repository.ClientRepository clientRepository;

    @Autowired
    com.location.location_voitures.api.repository.VehicleRepository vehicleRepository;

    @Test
    public void whenVehicleGoesToPanne_pendingContractsAreCancelled() throws InterruptedException {
        // cleanup collections to avoid duplicate key errors from previous runs
        contractRepository.deleteAll();
        clientRepository.deleteAll();
        vehicleRepository.deleteAll();

        // create client
        Client client = new Client();
        client.setNom("Dupont");
        client.setPrenom("Jean");
        client.setDateNaissance(LocalDate.of(1985,1,1));
        client.setNumeroPermis("PERM12345");
        client = clientService.createClient(client);

        // create vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setMarque("Renault");
        vehicle.setModele("Clio");
        vehicle.setImmatriculation("ZZ-123-AA");
        vehicle.setDateAcquisition(LocalDate.now());
        vehicle.setEtat(VehicleState.DISPONIBLE);
        vehicle = vehicleService.createVehicle(vehicle);

        // create contract (EN_ATTENTE expected)
        Contract contract = new Contract();
        contract.setClientId(client.getId());
        contract.setVehicleId(vehicle.getId());
        contract.setDateDebut(LocalDate.now());
        contract.setDateFin(LocalDate.now().plusDays(3));
        contract = contractService.createContract(contract);

        assertThat(contract.getEtat()).isEqualTo(ContractState.EN_ATTENTE);

        // change vehicle to EN_PANNE
        vehicle.setEtat(VehicleState.EN_PANNE);
        vehicleService.updateVehicle(vehicle);

        // wait a short time for event processing
        Thread.sleep(500);

        Contract reloaded = contractRepository.findById(contract.getId()).orElseThrow();
        assertThat(reloaded.getEtat()).isEqualTo(ContractState.ANNULE);
    }
}
