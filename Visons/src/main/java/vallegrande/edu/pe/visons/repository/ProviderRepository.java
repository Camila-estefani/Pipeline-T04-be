package vallegrande.edu.pe.visons.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import vallegrande.edu.pe.visons.model.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    List<Provider> findByIsActive(Boolean isActive);
}