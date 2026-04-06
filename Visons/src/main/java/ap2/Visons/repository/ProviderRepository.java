package ap2.Visons.repository;

import ap2.Visons.model.Provider;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    List<Provider> findByIsActive(Boolean isActive);

}
