package ap2.Visons.rest;

import ap2.Visons.model.Order;
import ap2.Visons.model.Customer;
import ap2.Visons.repository.OrderRepository;
import ap2.Visons.repository.CustomerRepository;
import ap2.Visons.dto.OrderDTO;
import ap2.Visons.dto.OrderResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200") 
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    // GET: Obtener todos los pedidos
    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        System.out.println("\n📥 GET /api/orders - Obtener todos los pedidos");
        List<Order> orders = orderRepository.findAll();
        System.out.println("✓ Pedidos en BD: " + orders.size());
        
        // Convertir Order a OrderResponseDTO
        List<OrderResponseDTO> response = orders.stream().map(order -> {
            System.out.println("  - Orden: " + order.getOrderCode() + " | Cliente: " + order.getCustomer().getCompanyName());
            return new OrderResponseDTO(
                order.getOrderId(),
                order.getCustomer().getClientId(),
                order.getCustomer().getCompanyName(),
                order.getOrderCode(),
                order.getOrderDate(),
                order.getIncoterm(),
                order.getStatus()
            );
        }).collect(Collectors.toList());
        
        System.out.println("✓ Retornando " + response.size() + " pedidos formateados\n");
        return response;
    }

    // POST: Guardar un nuevo pedido (versión simplificada para debugging)
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        System.out.println("\n========================================");
        System.out.println("📥 POST /api/orders - INICIANDO");
        System.out.println("========================================");
        System.out.println("✓ Punto 1: Método llamado");
        System.out.println("✓ OrderDTO recibido: " + (orderDTO != null ? "SI" : "NULL"));
        
        if (orderDTO != null) {
            System.out.println("  - clientId: " + orderDTO.getClientId());
            System.out.println("  - orderCode: " + orderDTO.getOrderCode());
            System.out.println("  - orderDate: " + orderDTO.getOrderDate());
            System.out.println("  - status: " + orderDTO.getStatus());
            System.out.println("  - incoterm: " + orderDTO.getIncoterm());
        }
        
        try {
            // PASO 1: Validaciones básicas
            System.out.println("\n✓ Punto 2: Validando clientId");
            if (orderDTO == null || orderDTO.getClientId() == null) {
                throw new RuntimeException("clientId es NULL");
            }
            
            // PASO 2: Buscar cliente
            System.out.println("✓ Punto 3: Buscando cliente con ID: " + orderDTO.getClientId());
            Optional<Customer> customerOpt = customerRepository.findById(orderDTO.getClientId());
            
            if (!customerOpt.isPresent()) {
                throw new RuntimeException("Cliente con ID " + orderDTO.getClientId() + " NO existe");
            }
            
            Customer customer = customerOpt.get();
            System.out.println("✓ Cliente encontrado: " + customer.getCompanyName());
            
            // PASO 3: Validar orderCode
            System.out.println("✓ Punto 4: Validando orderCode");
            if (orderDTO.getOrderCode() == null || orderDTO.getOrderCode().isEmpty()) {
                throw new RuntimeException("orderCode no puede estar vacío");
            }
            
            // PASO 4: Validar orderDate
            System.out.println("✓ Punto 5: Validando orderDate");
            if (orderDTO.getOrderDate() == null) {
                throw new RuntimeException("orderDate no puede ser NULL");
            }
            
            // PASO 5: Crear Order
            System.out.println("✓ Punto 6: Creando objeto Order");
            Order order = new Order();
            order.setCustomer(customer);
            order.setOrderCode(orderDTO.getOrderCode());
            order.setOrderDate(orderDTO.getOrderDate());
            order.setIncoterm(orderDTO.getIncoterm());
            order.setStatus(orderDTO.getStatus());
            
            // PASO 6: Guardar
            System.out.println("✓ Punto 7: Guardando en BD");
            Order savedOrder = orderRepository.save(order);
            
            System.out.println("✓✓✓ ¡ÉXITO COMPLETO!");
            System.out.println("✓✓✓ Pedido guardado con ID: " + savedOrder.getOrderId());
            
            // Convertir respuesta a OrderResponseDTO
            OrderResponseDTO responseDTO = new OrderResponseDTO(
                savedOrder.getOrderId(),
                savedOrder.getCustomer().getClientId(),
                savedOrder.getCustomer().getCompanyName(),
                savedOrder.getOrderCode(),
                savedOrder.getOrderDate(),
                savedOrder.getIncoterm(),
                savedOrder.getStatus()
            );
            
            System.out.println("========================================\n");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
            
        } catch (Exception e) {
            System.out.println("\n❌ EXCEPCIÓN EN PUNTO DE FALLA:");
            System.out.println("Tipo: " + e.getClass().getSimpleName());
            System.out.println("Mensaje: " + e.getMessage());
            System.out.println("Stack trace:");
            e.printStackTrace();
            
            // Relanzar la excepción para que GlobalExceptionHandler la maneje
            throw e;
        }
    }
}