package ap2.Visons.service.impl;

import ap2.Visons.model.Product;
import ap2.Visons.repository.ProductRepository;
import ap2.Visons.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    // ✅ Inyección del repository
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 🛠️🔍 Implementación del método Listar Todos (Solo Activos)
    @Override
    public List<Product> findAll() {
        log.info("Listando productos activos");
        return productRepository.findByIsActive(true);
    }

    // 🛠️🔍 Implementación del método Listar por Estado
    @Override
    public List<Product> findByState(String state) {
        log.info("Listando productos por estado: {}", state);
        boolean active = "A".equalsIgnoreCase(state) || "1".equals(state) || "true".equalsIgnoreCase(state);
        return productRepository.findByIsActive(active);
    }

    // 🛠️🔍 Implementación del método Listar por ID
    @Override
    public Optional<Product> findById(Integer id) {
        log.info("Buscando producto por ID: {}", id);
        return productRepository.findById(id);
    }

    // 🛠️✅ Implementación del método Registrar
    @Override
    public Product save(Product product) {
        log.info("Registrando nuevo producto: {}", product.getName());
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
        return productRepository.save(product);
    }

    // 🛠️✏️ Implementación del método Actualizar
    @Override
    public Product update(Integer id, Product productDetails) {
        log.info("Actualizando producto con ID: {}", id);
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            if (productDetails.getCategoryId() != null) {
                product.setCategoryId(productDetails.getCategoryId());
            }
            if (productDetails.getName() != null) {
                product.setName(productDetails.getName());
            }
            if (productDetails.getVariety() != null) {
                product.setVariety(productDetails.getVariety());
            }
            if (productDetails.getCaliber() != null) {
                product.setCaliber(productDetails.getCaliber());
            }
            if (productDetails.getUnitMeasure() != null) {
                product.setUnitMeasure(productDetails.getUnitMeasure());
            }
            if (productDetails.getBoxWeightKg() != null) {
                product.setBoxWeightKg(productDetails.getBoxWeightKg());
            }
            if (productDetails.getIsOwnProduction() != null) {
                product.setIsOwnProduction(productDetails.getIsOwnProduction());
            }
            product.setUpdatedAt(LocalDateTime.now());

            return productRepository.save(product);
        } else {
            log.warn("Producto con ID {} no encontrado", id);
            throw new RuntimeException("Producto no encontrado");
        }
    }

    // 🛠️❌ Implementación del método Eliminar (Eliminación Lógica)
    @Override
    public Product delete(Integer id) {
        log.info("Eliminando producto con ID: {}", id);
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setIsActive(false);
            product.setDeletedAt(LocalDateTime.now());
            return productRepository.save(product);
        } else {
            log.warn("Producto con ID {} no encontrado", id);
            throw new RuntimeException("Producto no encontrado");
        }
    }

    // 🛠️♻️ Implementación del método Restaurar (Restauración Lógica)
    @Override
    public Product restore(Integer id) {
        log.info("Restaurando producto con ID: {}", id);
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setIsActive(true);
            product.setRestoredAt(LocalDateTime.now());
            return productRepository.save(product);
        } else {
            log.warn("Producto con ID {} no encontrado", id);
            throw new RuntimeException("Producto no encontrado");
        }
    }
}
