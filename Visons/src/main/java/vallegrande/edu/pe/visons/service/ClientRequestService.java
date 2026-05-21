package vallegrande.edu.pe.visons.service;

import java.util.List;
import java.util.Optional;

import vallegrande.edu.pe.visons.dto.ClientRequestActionRequest;
import vallegrande.edu.pe.visons.dto.ClientRequestActionResponse;
import vallegrande.edu.pe.visons.model.ClientRequest;

public interface ClientRequestService {

    List<ClientRequest> findAll();

    List<ClientRequest> findByStatus(String status);

    Optional<ClientRequest> findById(Integer id);

    ClientRequest save(ClientRequest clientRequest);

    ClientRequestActionResponse approveRequest(Integer id, ClientRequestActionRequest request);

    ClientRequestActionResponse rejectRequest(Integer id, ClientRequestActionRequest request);
}
