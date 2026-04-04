package ap2.Visons.rest;

import ap2.Visons.model.Product;
import ap2.Visons.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")          // ✅ Permitir Conexión con Angular
@RestController
@Tag(name = "Product API", description = "API for Product management")
public class CustomerRest {

    // ✅ Inyección del service
    private final CustomerService customerService;

    @Autowired
    public CustomerRest(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    // 🌐🔍 Mapear Endpoint Listar Todos - tipo GET en POSTMAN
    @GetMapping({"/", "/v1/api/product"})
    @Operation(summary = "Get All Product", description = "Get All Product")
    public List<Product> findAll(){
        return customerService.findAll();
    }

    // 🌐🔍 Mapear Endpoint Listar por Estado - tipo GET en POSTMAN
    @GetMapping("/v1/api/product/state/{state}")
    @Operation(summary = "Get Product By STATE", description = "Get Product By STATE")
    public List<Product> findByState(@PathVariable String state) {
        return customerService.findByState(state);
    }

    // 🌐🔍 Mapear Endpoint Listar por ID - tipo GET en POSTMAN
    @GetMapping("/v1/api/product/{id}")
    @Operation(summary = "Get Product By ID", description = "Get Product By ID")
    public Optional<Product> findById(@PathVariable Integer id) {
        return customerService.findById(id);
    }

    // 🌐✅ Mapear Endpoint Registrar - tipo POST en POSTMAN
    @PostMapping("/v1/api/product/save")
    @Operation(summary = "Save Product", description = "Save Product")
    public Product save(@RequestBody Product product) {
        return customerService.save(product);
    }

    // 🌐✏️ Mapear Endpoint Actualizar - tipo PUT en POSTMAN
    @PutMapping("/v1/api/product/update/{id}")
    @Operation(summary = "Update Product", description = "Update Product")
    public Product update(@PathVariable Integer id, @RequestBody Product product) {
        return customerService.update(id, product);
    }

    // 🌐❌ Mapear Endpoint Eliminar (Cambio de Estado) por ID - tipo PATCH en POSTMAN
    @PatchMapping("/v1/api/product/delete/{id}")
    @Operation(summary = "Logical Delete Product", description = "Logical Delete Product")
    public Product delete(@PathVariable Integer id) {
        return customerService.delete(id);
    }

    // 🌐♻️ Mapear Endpoint Restaurar (Cambio de Estado) por ID - tipo PATCH en POSTMAN
    @PatchMapping("/v1/api/product/restore/{id}")
    @Operation(summary = "Logical Restore Product", description = "Logical Restore Product")
    public Product restore(@PathVariable Integer id) {
        return customerService.restore(id);
    }

}
