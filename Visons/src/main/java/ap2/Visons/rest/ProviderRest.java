package ap2.Visons.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ap2.Visons.model.Provider;
import ap2.Visons.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")          // ✅ Permitir Conexión con Angular
@RestController
@RequestMapping("/v1/api/provider")
@Tag(name = "Provider API", description = "API for Provider management")
public class ProviderRest {

    // ✅ Inyección del service
    private final ProviderService providerService;

    @Autowired
    public ProviderRest(ProviderService providerService) {
        this.providerService = providerService;
    }

    // 🌐🔍 Mapear Endpoint Listar Todos - tipo GET en POSTMAN
    @GetMapping({"", "/"})
    @Operation(summary = "Get All Providers", description = "Get All Providers")
    public List<Provider> findAll() {
        return providerService.findAll();
    }

    // 🌐🔍 Mapear Endpoint Listar por Estado - tipo GET en POSTMAN
    @GetMapping("/state/{state}")
    @Operation(summary = "Get Provider By STATE", description = "Get Provider By STATE")
    public List<Provider> findByState(@PathVariable String state) {
        return providerService.findByState(state);
    }

    // 🌐🔍 Mapear Endpoint Listar por ID - tipo GET en POSTMAN
    @GetMapping("/{id}")
    @Operation(summary = "Get Provider By ID", description = "Get Provider By ID")
    public Optional<Provider> findById(@PathVariable Integer id) {
        return providerService.findById(id);
    }

    // 🌐✅ Mapear Endpoint Registrar - tipo POST en POSTMAN
    @PostMapping("/save")
    @Operation(summary = "Save Provider", description = "Save Provider")
    public Provider save(@RequestBody Provider provider) {
        return providerService.save(provider);
    }

    // 🌐✏️ Mapear Endpoint Actualizar (Edición Parcial) - tipo PATCH en POSTMAN
    @PatchMapping("/update/{id}")
    @Operation(summary = "Update Provider", description = "Update Provider")
    public Provider update(@PathVariable Integer id, @RequestBody Provider provider) {
        return providerService.update(id, provider);
    }

    // 🌐❌ Mapear Endpoint Eliminar (Cambio de Estado) por ID - tipo DELETE en POSTMAN
    @DeleteMapping("/{id}")
    @Operation(summary = "Logical Delete Provider", description = "Logical Delete Provider")
    public Provider delete(@PathVariable Integer id) {
        return providerService.delete(id);
    }

    // 🌐♻️ Mapear Endpoint Restaurar (Cambio de Estado) por ID - tipo POST en POSTMAN
    @PostMapping("/restore/{id}")
    @Operation(summary = "Logical Restore Provider", description = "Logical Restore Provider")
    public Provider restore(@PathVariable Integer id) {
        return providerService.restore(id);
    }

}
