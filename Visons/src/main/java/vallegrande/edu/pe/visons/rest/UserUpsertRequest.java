package vallegrande.edu.pe.visons.rest;

import lombok.Data;

@Data
public class UserUpsertRequest {
    private Integer roleId;
    private String userType;
    private String username;
    private String password;
    private Boolean active;
    private WorkerForm worker;
    private ClientForm client;
}