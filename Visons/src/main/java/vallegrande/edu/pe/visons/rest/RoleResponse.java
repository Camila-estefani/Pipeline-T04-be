package vallegrande.edu.pe.visons.rest;

import lombok.Data;

@Data
public class RoleResponse {
    private Integer roleId;
    private String name;
    private String description;
    private Long userCount;
}