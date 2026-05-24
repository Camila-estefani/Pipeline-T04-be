package vallegrande.edu.pe.visons.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import vallegrande.edu.pe.visons.model.CurrentInventory;
import vallegrande.edu.pe.visons.model.Product;
import vallegrande.edu.pe.visons.repository.CurrentInventoryRepository;
import vallegrande.edu.pe.visons.repository.ProductRepository;
import vallegrande.edu.pe.visons.service.ProductService;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CurrentInventoryRepository currentInventoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CurrentInventoryRepository currentInventoryRepository) {
        this.productRepository = productRepository;
        this.currentInventoryRepository = currentInventoryRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream().map(this::withInventory).toList();
    }

    @Override
    public List<Product> findByState(String state) {
        boolean active = "A".equalsIgnoreCase(state) || "1".equals(state) || "true".equalsIgnoreCase(state);
        return productRepository.findByIsActive(active).stream().map(this::withInventory).toList();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id).map(this::withInventory);
    }

    @Override
    @Transactional
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
        Product savedProduct = productRepository.save(product);
        ensureInventory(savedProduct, product.getInitialStockKg());
        return withInventory(savedProduct);
    }

    @Override
    @Transactional
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
            return withInventory(productRepository.save(product));
        }
        throw new RuntimeException("Producto no encontrado");
    }

    private void ensureInventory(Product product, BigDecimal initialStockKg) {
        if (product == null || product.getProductId() == null) {
            return;
        }

        BigDecimal normalizedInitialStock = initialStockKg == null ? BigDecimal.ZERO : initialStockKg;
        if (normalizedInitialStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El stock inicial no puede ser negativo");
        }

        currentInventoryRepository.findReadOnlyByProductId(product.getProductId()).orElseGet(() -> {
            CurrentInventory inventory = new CurrentInventory();
            inventory.setProductId(product.getProductId());
            inventory.setTotalStockKg(normalizedInitialStock);
            inventory.setReservedStockKg(BigDecimal.ZERO);
            inventory.setAvailableStockKg(normalizedInitialStock);
            return currentInventoryRepository.save(inventory);
        });
    }

    private Product withInventory(Product product) {
        if (product == null || product.getProductId() == null) {
            return product;
        }

        currentInventoryRepository.findReadOnlyByProductId(product.getProductId()).ifPresentOrElse(inventory -> {
            product.setTotalStockKg(inventory.getTotalStockKg());
            product.setReservedStockKg(inventory.getReservedStockKg());
            product.setAvailableStockKg(inventory.getAvailableStockKg());
        }, () -> {
            product.setTotalStockKg(BigDecimal.ZERO);
            product.setReservedStockKg(BigDecimal.ZERO);
            product.setAvailableStockKg(BigDecimal.ZERO);
        });
        return product;
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
            return withInventory(productRepository.save(product));
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
            return withInventory(productRepository.save(product));
        }
        throw new RuntimeException("Producto no encontrado");
    }
}
