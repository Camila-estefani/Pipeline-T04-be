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

import ap2.Visons.model.Customer;
import ap2.Visons.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")          // ✅ Permitir Conexión con Angular
@RestController
@RequestMapping("/v1/api/customer")
@Tag(name = "Customer API", description = "API for Customer management")
public class CustomerRest {

    // ✅ Inyección del service
    private final CustomerService customerService;

    @Autowired
    public CustomerRest(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 🌐🔍 Mapear Endpoint Listar Todos - tipo GET en POSTMAN
    @GetMapping({"/", ""})
    @Operation(summary = "Get All Customers", description = "Get All Customers")
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    // 🌐🔍 Mapear Endpoint Listar por Estado - tipo GET en POSTMAN
    @GetMapping("/state/{state}")
    @Operation(summary = "Get Customer By STATE (A/1/true for active)", description = "Get Customer By STATE")
    public List<Customer> findByState(@PathVariable String state) {
        return customerService.findByState(state);
    }

    // 🌐🔍 Mapear Endpoint Listar por ID - tipo GET en POSTMAN
    @GetMapping("/{id}")
    @Operation(summary = "Get Customer By ID", description = "Get Customer By ID")
    public Optional<Customer> findById(@PathVariable Integer id) {
        return customerService.findById(id);
    }

    // 🌐✅ Mapear Endpoint Registrar - tipo POST en POSTMAN
    @PostMapping("/save")
    @Operation(summary = "Save Customer", description = "Save Customer")
    public Customer save(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    // 🌐✏️ Mapear Endpoint Actualizar - tipo PUT en POSTMAN
    @PutMapping("/update/{id}")
    @Operation(summary = "Update Customer", description = "Update Customer")
    public Customer update(@PathVariable Integer id, @RequestBody Customer customer) {
        return customerService.update(id, customer);
    }

    // 🌐❌ Mapear Endpoint Eliminar (Cambio de Estado) por ID - tipo DELETE en POSTMAN
    @DeleteMapping("/{id}")
    @Operation(summary = "Logical Delete Customer", description = "Logical Delete Customer")
    public Customer delete(@PathVariable Integer id) {
        return customerService.delete(id);
    }

    // 🌐♻️ Mapear Endpoint Restaurar (Cambio de Estado) por ID - tipo PATCH en POSTMAN
    @PatchMapping("/restore/{id}")
    @Operation(summary = "Logical Restore Customer", description = "Logical Restore Customer")
    public Customer restore(@PathVariable Integer id) {
        return customerService.restore(id);
    }

}
