
import com.location.location_voitures.api.dto.ClientDTO;
import com.location.location_voitures.api.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientDTO dto);

    ClientDTO toDto(Client c);
}

