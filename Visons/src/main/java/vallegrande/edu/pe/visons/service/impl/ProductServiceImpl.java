package vallegrande.edu.pe.visons.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vallegrande.edu.pe.visons.model.Product;
import vallegrande.edu.pe.visons.repository.ProductRepository;
import vallegrande.edu.pe.visons.service.ProductService;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByState(String state) {
        boolean active = "A".equalsIgnoreCase(state) || "1".equals(state) || "true".equalsIgnoreCase(state);
        return productRepository.findByIsActive(active);
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        ensureUniqueProduct(null, product);
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

    @Override
    public Product update(Integer id, Product productDetails) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            ensureUniqueProduct(id, productDetails);
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
        }
        throw new RuntimeException("Producto no encontrado");
    }

    private void ensureUniqueProduct(Integer currentId, Product product) {
        if (product == null || product.getCategoryId() == null || product.getName() == null || product.getName().isBlank()) {
            return;
        }

        productRepository.findByCategoryIdAndNameIgnoreCase(product.getCategoryId(), product.getName().trim())
                .filter(existing -> currentId == null || !existing.getProductId().equals(currentId))
                .ifPresent(existing -> { throw new RuntimeException("Ya existe un producto con ese nombre en la categoría seleccionada"); });
    }

    @Override
    public Product delete(Integer id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setIsActive(false);
            product.setDeletedAt(LocalDateTime.now());
            return productRepository.save(product);
        }
        throw new RuntimeException("Producto no encontrado");
    }

    @Override
    public Product restore(Integer id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setIsActive(true);
            product.setRestoredAt(LocalDateTime.now());
            return productRepository.save(product);
        }
        throw new RuntimeException("Producto no encontrado");
    }
}