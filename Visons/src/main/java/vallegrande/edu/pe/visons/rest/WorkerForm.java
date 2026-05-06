package vallegrande.edu.pe.visons.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WorkerForm {
    private Integer workerId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private Integer ubigeoId;
    private String documentType;
    private String documentNumber;
    private LocalDate hireDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}