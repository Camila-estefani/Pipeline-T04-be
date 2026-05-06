package vallegrande.edu.pe.visons.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vallegrande.edu.pe.visons.model.Customer;
import vallegrande.edu.pe.visons.model.Order;
import vallegrande.edu.pe.visons.repository.CustomerRepository;
import vallegrande.edu.pe.visons.repository.OrderRepository;
import vallegrande.edu.pe.visons.service.OrderPdfService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderRest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderPdfService orderPdfService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order -> new OrderResponseDTO(
                order.getOrderId(),
                order.getCustomer().getClientId(),
                order.getCustomer().getCompanyName(),
                order.getOrderCode(),
                order.getOrderDate(),
                order.getIncoterm(),
                order.getStatus())).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            if (orderDTO == null || orderDTO.getClientId() == null) {
                throw new RuntimeException("clientId es NULL");
            }

            Optional<Customer> customerOpt = customerRepository.findById(orderDTO.getClientId());
            if (customerOpt.isEmpty()) {
                throw new RuntimeException("Cliente con ID " + orderDTO.getClientId() + " NO existe");
            }

            if (orderDTO.getOrderCode() == null || orderDTO.getOrderCode().isEmpty()) {
                throw new RuntimeException("orderCode no puede estar vacío");
            }
            if (orderDTO.getOrderDate() == null) {
                throw new RuntimeException("orderDate no puede ser NULL");
            }

            Order order = new Order();
            order.setCustomer(customerOpt.get());
            order.setOrderCode(orderDTO.getOrderCode());
            order.setOrderDate(orderDTO.getOrderDate());
            order.setIncoterm(orderDTO.getIncoterm());
            order.setStatus(orderDTO.getStatus());

            Order savedOrder = orderRepository.save(order);
            OrderResponseDTO responseDTO = new OrderResponseDTO(
                    savedOrder.getOrderId(),
                    savedOrder.getCustomer().getClientId(),
                    savedOrder.getCustomer().getCompanyName(),
                    savedOrder.getOrderCode(),
                    savedOrder.getOrderDate(),
                    savedOrder.getIncoterm(),
                    savedOrder.getStatus());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Integer id, @RequestBody OrderDTO orderDTO) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
            }

            Optional<Customer> customerOpt = customerRepository.findById(orderDTO.getClientId());
            if (customerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente no existe");
            }

            Order order = orderOpt.get();
            order.setCustomer(customerOpt.get());
            order.setOrderCode(orderDTO.getOrderCode());
            order.setOrderDate(orderDTO.getOrderDate());
            order.setIncoterm(orderDTO.getIncoterm());
            order.setStatus(orderDTO.getStatus());

            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.ok(new OrderResponseDTO(
                    savedOrder.getOrderId(),
                    savedOrder.getCustomer().getClientId(),
                    savedOrder.getCustomer().getCompanyName(),
                    savedOrder.getOrderCode(),
                    savedOrder.getOrderDate(),
                    savedOrder.getIncoterm(),
                    savedOrder.getStatus()));
        } catch (Exception e) {
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer id) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
            }

            jdbcTemplate.update("DELETE FROM ORDER_DETAILS WHERE order_id = ?", id);
            orderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadOrderPdf(@org.springframework.web.bind.annotation.PathVariable Integer id) throws Exception {
        byte[] pdf = orderPdfService.generateOrderPdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "order_" + id + ".pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/report/pdf")
    public ResponseEntity<byte[]> downloadOrdersReportPdf() throws Exception {
        byte[] pdf = orderPdfService.generateOrdersReportPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "orders_report.pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}