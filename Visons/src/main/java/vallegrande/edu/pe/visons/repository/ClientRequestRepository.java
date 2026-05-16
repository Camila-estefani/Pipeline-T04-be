package vallegrande.edu.pe.visons.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vallegrande.edu.pe.visons.model.ClientRequest;

@Repository
public interface ClientRequestRepository extends JpaRepository<ClientRequest, Integer> {

    List<ClientRequest> findByStatus(String status);
}
