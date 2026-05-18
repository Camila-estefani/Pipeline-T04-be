package vallegrande.edu.pe.visons.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vallegrande.edu.pe.visons.dto.AuthLoginRequest;
import vallegrande.edu.pe.visons.dto.UserResponse;
import vallegrande.edu.pe.visons.service.UserService;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthRest {

    private final UserService userService;

    public AuthRest(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody AuthLoginRequest request) {
        return userService.authenticate(request);
    }
}