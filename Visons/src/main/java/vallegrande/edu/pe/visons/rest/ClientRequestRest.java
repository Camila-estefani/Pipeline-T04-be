package vallegrande.edu.pe.visons.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import vallegrande.edu.pe.visons.model.ClientRequest;
import vallegrande.edu.pe.visons.service.ClientRequestService;

@RestController
@RequestMapping("/v1/api/client-request")
@Tag(name = "Client Request API", description = "API for Client Registration Requests")
public class ClientRequestRest {

    private final ClientRequestService clientRequestService;

    @Autowired
    public ClientRequestRest(ClientRequestService clientRequestService) {
        this.clientRequestService = clientRequestService;
    }

    @GetMapping({"", "/"})
    @Operation(summary = "Get All Client Requests", description = "Get All Client Requests")
    public List<ClientRequest> findAll() {
        return clientRequestService.findAll();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get Client Requests By Status", description = "Get Client Requests By Status (Pending, Approved, Rejected)")
    public List<ClientRequest> findByStatus(@PathVariable String status) {
        return clientRequestService.findByStatus(status);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Client Request By ID", description = "Get Client Request By ID")
    public Optional<ClientRequest> findById(@PathVariable Integer id) {
        return clientRequestService.findById(id);
    }

    @PostMapping("/save")
    @Operation(summary = "Submit Client Request (POST)", description = "Submit a new client registration request from the landing page")
    public ClientRequest save(@RequestBody ClientRequest clientRequest) {
        return clientRequestService.save(clientRequest);
    }
}
