package ap2.Visons.rest;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:4200")
public class TestController {

    @GetMapping("/health")
    public String health() {
        System.out.println("✓ GET /api/test/health - OK");
        return "{\"status\": \"Backend is running\", \"timestamp\": \"" + LocalDateTime.now() + "\"}";
    }

    @PostMapping("/echo")
    public Object echo(@RequestBody Object body) {
        System.out.println("POST /api/test/echo - Recibido: " + body.toString());
        return body;
    }
}
