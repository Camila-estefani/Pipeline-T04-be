package ap2.Visons.service;

import ap2.Visons.model.Product;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Product> findAll();

    List<Product> findByState(String state);

    Optional<Product> findById(Integer id);

    Product save(Product product);

    Product update(Integer id, Product product);

    Product delete(Integer id);

    Product restore(Integer id);
    
}
