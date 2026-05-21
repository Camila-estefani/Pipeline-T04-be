package vallegrande.edu.pe.visons.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class OrderDTO {

    @NotNull(message = "clientId es requerido")
    private Integer clientId;

    @NotBlank(message = "orderCode es requerido")
    @Size(min = 3, max = 50, message = "orderCode debe tener entre 3 y 50 caracteres")
    private String orderCode;

    @NotNull(message = "orderDate es requerido")
    private LocalDate orderDate;

    @Pattern(regexp = "^(EXW|FOB|CIF)$", message = "incoterm debe ser EXW, FOB o CIF")
    private String incoterm;

    @NotBlank(message = "status es requerido")
    @Size(max = 50, message = "status debe tener como máximo 50 caracteres")
    private String status;

    public OrderDTO() {
    }

    public OrderDTO(Integer clientId, String orderCode, LocalDate orderDate, String incoterm, String status) {
        this.clientId = clientId;
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.incoterm = incoterm;
        this.status = status;
    }

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