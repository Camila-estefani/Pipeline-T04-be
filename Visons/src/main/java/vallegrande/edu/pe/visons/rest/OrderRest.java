package vallegrande.edu.pe.visons.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import vallegrande.edu.pe.visons.dto.OrderDTO;
import vallegrande.edu.pe.visons.dto.OrderResponseDTO;
import vallegrande.edu.pe.visons.dto.UserResponse;
import vallegrande.edu.pe.visons.model.Customer;
import vallegrande.edu.pe.visons.model.Order;
import vallegrande.edu.pe.visons.repository.CustomerRepository;
import vallegrande.edu.pe.visons.repository.OrderRepository;
import vallegrande.edu.pe.visons.service.OrderPdfService;
import vallegrande.edu.pe.visons.service.UserService;

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

    @Autowired
    private UserService userService;

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        return orders.stream().map(order -> new OrderResponseDTO(
                order.getOrderId(),
                order.getCustomer().getClientId(),
                order.getCustomer().getCompanyName(),
                order.getOrderCode(),
                order.getOrderDate(),
                order.getIncoterm(),
                order.getStatus())).collect(Collectors.toList());
    }

    @GetMapping("/my")
    public List<OrderResponseDTO> getMyOrders(HttpSession session) {
        List<Order> accessibleOrders = resolveAccessibleOrders(session);
        return accessibleOrders.stream().map(order -> new OrderResponseDTO(
                order.getOrderId(),
                order.getCustomer().getClientId(),
                order.getCustomer().getCompanyName(),
                order.getOrderCode(),
                order.getOrderDate(),
                order.getIncoterm(),
                order.getStatus())).collect(Collectors.toList());
    }

    @GetMapping("/pending")
    public List<OrderResponseDTO> getPendingOrders(HttpSession session) {
        return resolveAccessibleOrders(session).stream()
                .filter(order -> "pending".equalsIgnoreCase(order.getStatus()))
                .map(order -> new OrderResponseDTO(
                        order.getOrderId(),
                        order.getCustomer().getClientId(),
                        order.getCustomer().getCompanyName(),
                        order.getOrderCode(),
                        order.getOrderDate(),
                        order.getIncoterm(),
                        order.getStatus()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Integer id, HttpSession session) {
        Order order = findAccessibleOrder(id, session);
        return ResponseEntity.ok(new OrderResponseDTO(
                order.getOrderId(),
                order.getCustomer().getClientId(),
                order.getCustomer().getCompanyName(),
                order.getOrderCode(),
                order.getOrderDate(),
                order.getIncoterm(),
                order.getStatus()));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<OrderResponseDTO> acceptOrder(@PathVariable Integer id, HttpSession session) {
        Order updated = updateOrderStatus(id, session, "Processing");
        return ResponseEntity.ok(toResponse(updated));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<OrderResponseDTO> rejectOrder(@PathVariable Integer id, HttpSession session) {
        Order updated = updateOrderStatus(id, session, "Cancelled");
        return ResponseEntity.ok(toResponse(updated));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
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

            if (orderRepository.findByOrderCodeIgnoreCase(orderDTO.getOrderCode().trim()).isPresent()) {
                throw new RuntimeException("El orderCode ya existe");
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
    public ResponseEntity<?> updateOrder(@PathVariable Integer id, @Valid @RequestBody OrderDTO orderDTO) {
        try {
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (orderOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
            }

            Optional<Customer> customerOpt = customerRepository.findById(orderDTO.getClientId());
            if (customerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cliente no existe");
            }

            if (orderDTO.getOrderCode() != null && !orderDTO.getOrderCode().isBlank()) {
                orderRepository.findByOrderCodeIgnoreCase(orderDTO.getOrderCode().trim())
                        .filter(existing -> !existing.getOrderId().equals(id))
                        .ifPresent(existing -> {
                            throw new RuntimeException("El orderCode ya existe");
                        });
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
    public ResponseEntity<byte[]> downloadOrderPdf(@PathVariable Integer id, HttpSession session) throws Exception {
        findAccessibleOrder(id, session);
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

    private Order findAccessibleOrder(Integer orderId, HttpSession session) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        UserResponse currentUser = userService.currentSessionUser(session);
        if (isClient(currentUser) && currentUser.getClientId() != null) {
            Integer orderClientId = order.getCustomer() != null ? order.getCustomer().getClientId() : null;
            if (orderClientId == null || !orderClientId.equals(currentUser.getClientId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para ver este pedido");
            }
        }

        return order;
    }

    private Order updateOrderStatus(Integer orderId, HttpSession session, String newStatus) {
        UserResponse currentUser = userService.currentSessionUser(session);
        if (isClient(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para procesar pedidos");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    private List<Order> resolveAccessibleOrders(HttpSession session) {
        UserResponse currentUser = userService.currentSessionUser(session);
        if (!isClient(currentUser) || currentUser.getClientId() == null) {
            return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"));
        }
        return orderRepository.findByCustomer_ClientIdOrderByOrderDateDesc(currentUser.getClientId());
    }

    private OrderResponseDTO toResponse(Order order) {
        return new OrderResponseDTO(
                order.getOrderId(),
                order.getCustomer().getClientId(),
                order.getCustomer().getCompanyName(),
                order.getOrderCode(),
                order.getOrderDate(),
                order.getIncoterm(),
                order.getStatus());
    }

    private boolean isClient(UserResponse user) {
        String role = user == null ? null : user.getUserTypeName();
        return role != null && role.equalsIgnoreCase("CLIENT");
    }
}