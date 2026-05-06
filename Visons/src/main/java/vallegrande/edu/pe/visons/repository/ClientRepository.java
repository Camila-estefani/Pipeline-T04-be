package vallegrande.edu.pe.visons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vallegrande.edu.pe.visons.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
}