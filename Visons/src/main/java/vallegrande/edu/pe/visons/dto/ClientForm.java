package vallegrande.edu.pe.visons.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ClientForm {
    private Integer clientId;
    private String companyName;
    private String taxId;
    private String country;
    private String phone;
    private String address;
    private String email;
    private String profileImageUrl;
    private BigDecimal creditLimit;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;
}