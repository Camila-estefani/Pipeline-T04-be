package ap2.Visons.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class OrderDTO {
    
    @NotNull(message = "clientId es requerido")
    private Integer clientId;
    
    @NotBlank(message = "orderCode es requerido")
    private String orderCode;
    
    @NotNull(message = "orderDate es requerido")
    private LocalDate orderDate;
    
    private String incoterm;
    private String status;

    // Constructores
    public OrderDTO() {}

    public OrderDTO(Integer clientId, String orderCode, LocalDate orderDate, 
                    String incoterm, String status) {
        this.clientId = clientId;
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.incoterm = incoterm;
        this.status = status;
    }

    // Getters y Setters
    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getIncoterm() {
        return incoterm;
    }

    public void setIncoterm(String incoterm) {
        this.incoterm = incoterm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "clientId=" + clientId +
                ", orderCode='" + orderCode + '\'' +
                ", orderDate=" + orderDate +
                ", incoterm='" + incoterm + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

