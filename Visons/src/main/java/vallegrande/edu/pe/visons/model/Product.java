package vallegrande.edu.pe.visons.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "PRODUCTS")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "variety", length = 100)
    private String variety;

    @Column(name = "caliber", length = 50)
    private String caliber;

    @Column(name = "unit_measure", nullable = false, length = 20)
    private String unitMeasure;

    @Column(name = "box_weight_kg", precision = 10, scale = 2)
    private BigDecimal boxWeightKg;

    @Column(name = "is_own_production", nullable = false)
    private Boolean isOwnProduction;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;
}