package ap2.Visons.repository;

import ap2.Visons.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Product, Integer> {

    List<Product> findByIsActive(Boolean isActive);
    
}
