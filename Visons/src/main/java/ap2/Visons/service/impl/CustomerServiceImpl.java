package ap2.Visons.service.impl;

import ap2.Visons.model.Product;
import ap2.Visons.repository.CustomerRepository;
import ap2.Visons.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    // ✅ Inyección del repository
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 🛠️🔍 Implementación del método Listar Todos
    @Override
    public List<Product> findAll() {
        log.info("Listando productos");
        return customerRepository.findAll();
    }

    // 🛠️🔍 Implementación del método Listar por Estado
    @Override
    public List<Product> findByState(String state) {
        log.info("Listando productos por estado: {}", state);
        boolean active = "A".equalsIgnoreCase(state) || "1".equals(state) || "true".equalsIgnoreCase(state);
        return customerRepository.findByIsActive(active);
    }

    // 🛠️🔍 Implementación del método Listar por ID
    @Override
    public Optional<Product> findById(Integer id) {
        log.info("Listando producto por ID: {}", id);
        return customerRepository.findById(id);
    }

    // 🛠️✅ Implementación del método Registrar
    @Override
    public Product save(Product product) {
        log.info("Registrando producto: {}", product);
        LocalDateTime now = LocalDateTime.now();
        product.setProductId(null);
        product.setIsActive(true);
        product.setCreatedAt(now);
        product.setUpdatedAt(null);
        product.setDeletedAt(null);
        product.setRestoredAt(null);
        if (product.getUnitMeasure() == null || product.getUnitMeasure().isBlank()) {
            product.setUnitMeasure("KG");
        }
        if (product.getIsOwnProduction() == null) {
            product.setIsOwnProduction(false);
        }
        return customerRepository.save(product);
    }

    // 🛠️✏️ Implementación del método Actualizar
    @Override
    public Product update(Integer id, Product product) {
        log.info("Editando producto: {}", id);
        Product existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        existing.setCategoryId(product.getCategoryId());
        existing.setName(product.getName());
        existing.setVariety(product.getVariety());
        existing.setCaliber(product.getCaliber());
        existing.setUnitMeasure(product.getUnitMeasure());
        existing.setBoxWeightKg(product.getBoxWeightKg());
        existing.setIsOwnProduction(product.getIsOwnProduction());
        existing.setUpdatedAt(LocalDateTime.now());

        return customerRepository.save(existing);
    }

    // 🛠️❌ Implementación del método Eliminar (Cambio de Estado) por ID
    @Override
    public Product delete(Integer id) {
        log.info("Eliminando lógico producto: {}", id);
        Product product = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        product.setIsActive(false);
        product.setDeletedAt(LocalDateTime.now());
        return customerRepository.save(product);
    }

    // 🛠️♻️ Implementación del método Restaurar (Cambio de Estado) por ID
    @Override
    public Product restore(Integer id) {
        log.info("Restaurando lógico producto: {}", id);
        Product product = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
        product.setIsActive(true);
        product.setRestoredAt(LocalDateTime.now());
        return customerRepository.save(product);
    }

}
