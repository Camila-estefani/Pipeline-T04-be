package ap2.Visons.rest;

import ap2.Visons.model.Product;
import ap2.Visons.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/v1/api/product")
@Tag(name = "Product API", description = "API for Product management")
public class ProductRest {

    // ✅ Inyección del service
    private final ProductService productService;

    @Autowired
    public ProductRest(ProductService productService) {
        this.productService = productService;
    }

    // 🌐🔍 Mapear Endpoint Listar Todos - tipo GET en POSTMAN
    @GetMapping({"/", ""})
    @Operation(summary = "Get All Products", description = "Get All Products")
    public List<Product> findAll() {
        return productService.findAll();
    }

    // 🌐🔍 Mapear Endpoint Listar por Estado - tipo GET en POSTMAN
    @GetMapping("/state/{state}")
    @Operation(summary = "Get Product By STATE (A/1/true for active)", description = "Get Product By STATE")
    public List<Product> findByState(@PathVariable String state) {
        return productService.findByState(state);
    }

    // 🌐🔍 Mapear Endpoint Listar por ID - tipo GET en POSTMAN
    @GetMapping("/{id}")
    @Operation(summary = "Get Product By ID", description = "Get Product By ID")
    public Optional<Product> findById(@PathVariable Integer id) {
        return productService.findById(id);
    }

    // 🌐✅ Mapear Endpoint Registrar - tipo POST en POSTMAN
    @PostMapping("/save")
    @Operation(summary = "Save Product", description = "Save Product")
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    // 🌐✏️ Mapear Endpoint Actualizar (Edición Parcial) - tipo PATCH en POSTMAN
    @PatchMapping("/update/{id}")
    @Operation(summary = "Update Product", description = "Update Product")
    public Product update(@PathVariable Integer id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    // 🌐❌ Mapear Endpoint Eliminar (Cambio de Estado) por ID - tipo DELETE en POSTMAN
    @DeleteMapping("/{id}")
    @Operation(summary = "Logical Delete Product", description = "Logical Delete Product")
    public Product delete(@PathVariable Integer id) {
        return productService.delete(id);
    }

    // 🌐♻️ Mapear Endpoint Restaurar (Cambio de Estado) por ID - tipo POST en POSTMAN
    @PostMapping("/restore/{id}")
    @Operation(summary = "Logical Restore Product", description = "Logical Restore Product")
    public Product restore(@PathVariable Integer id) {
        return productService.restore(id);
    }

}
