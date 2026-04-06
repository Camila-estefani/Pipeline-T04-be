package ap2.Visons.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ap2.Visons.model.Customer;
import ap2.Visons.repository.CustomerRepository;
import ap2.Visons.service.CustomerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    // ✅ Inyección del repository
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // 🛠️🔍 Implementación del método Listar Todos (Solo Activos)
    @Override
    public List<Customer> findAll() {
        log.info("Listando clientes activos");
        return customerRepository.findByIsActive(true);
    }

    // 🛠️🔍 Implementación del método Listar por Estado
    @Override
    public List<Customer> findByState(String state) {
        log.info("Listando clientes por estado: {}", state);
        boolean active = "A".equalsIgnoreCase(state) || "1".equals(state) || "true".equalsIgnoreCase(state);
        return customerRepository.findByIsActive(active);
    }

    // 🛠️🔍 Implementación del método Listar por ID
    @Override
    public Optional<Customer> findById(Integer id) {
        log.info("Buscando cliente por ID: {}", id);
        return customerRepository.findById(id);
    }

    // 🛠️✅ Implementación del método Registrar
    @Override
    public Customer save(Customer customer) {
        log.info("Registrando nuevo cliente: {}", customer.getCompanyName());
        customer.setIsActive(true);
        return customerRepository.save(customer);
    }

    // 🛠️✏️ Implementación del método Actualizar
    @Override
    public Customer update(Integer id, Customer customerDetails) {
        log.info("Actualizando cliente con ID: {}", id);
        Optional<Customer> existingCustomer = customerRepository.findById(id);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();

            if (customerDetails.getCompanyName() != null) {
                customer.setCompanyName(customerDetails.getCompanyName());
            }
            if (customerDetails.getTaxId() != null) {
                customer.setTaxId(customerDetails.getTaxId());
            }
            if (customerDetails.getCountry() != null) {
                customer.setCountry(customerDetails.getCountry());
            }
            if (customerDetails.getAddress() != null) {
                customer.setAddress(customerDetails.getAddress());
            }
            if (customerDetails.getEmail() != null) {
                customer.setEmail(customerDetails.getEmail());
            }
            if (customerDetails.getCreditLimit() != null) {
                customer.setCreditLimit(customerDetails.getCreditLimit());
            }

            return customerRepository.save(customer);
        } else {
            log.warn("Cliente con ID {} no encontrado", id);
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    // 🛠️❌ Implementación del método Eliminar (Eliminación Lógica)
    @Override
    public Customer delete(Integer id) {
        log.info("Eliminando cliente con ID: {}", id);
        Optional<Customer> existingCustomer = customerRepository.findById(id);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            customer.setIsActive(false);
            return customerRepository.save(customer);
        } else {
            log.warn("Cliente con ID {} no encontrado", id);
            throw new RuntimeException("Cliente no encontrado");
        }
    }

    // 🛠️♻️ Implementación del método Restaurar (Restauración Lógica)
    @Override
    public Customer restore(Integer id) {
        log.info("Restaurando cliente con ID: {}", id);
        Optional<Customer> existingCustomer = customerRepository.findById(id);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            customer.setIsActive(true);
            return customerRepository.save(customer);
        } else {
            log.warn("Cliente con ID {} no encontrado", id);
            throw new RuntimeException("Cliente no encontrado");
        }
    }

}
