package vallegrande.edu.pe.visons.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vallegrande.edu.pe.visons.model.ClientRequest;
import vallegrande.edu.pe.visons.repository.ClientRequestRepository;
import vallegrande.edu.pe.visons.service.ClientRequestService;

@Slf4j
@Service
public class ClientRequestServiceImpl implements ClientRequestService {

    private final ClientRequestRepository clientRequestRepository;

    @Autowired
    public ClientRequestServiceImpl(ClientRequestRepository clientRequestRepository) {
        this.clientRequestRepository = clientRequestRepository;
    }

    @Override
    public List<ClientRequest> findAll() {
        return clientRequestRepository.findAll();
    }

    @Override
    public List<ClientRequest> findByStatus(String status) {
        return clientRequestRepository.findByStatus(status);
    }

    @Override
    public Optional<ClientRequest> findById(Integer id) {
        return clientRequestRepository.findById(id);
    }

    @Override
    public ClientRequest save(ClientRequest clientRequest) {
        clientRequest.setRequestId(null);
        clientRequest.setStatus("Pending");
        clientRequest.setRequestDate(LocalDateTime.now());
        clientRequest.setReviewedBy(null);
        clientRequest.setComments(null);
        return clientRequestRepository.save(clientRequest);
    }
}
