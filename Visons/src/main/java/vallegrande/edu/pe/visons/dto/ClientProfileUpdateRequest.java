package vallegrande.edu.pe.visons.dto;

import lombok.Data;

@Data
public class ClientProfileUpdateRequest {
    private String companyName;
    private String phone;
    private String address;
    private String profileImageUrl;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}