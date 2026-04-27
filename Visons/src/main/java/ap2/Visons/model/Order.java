package ap2.Visons.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Entity
@Data
@Table(name = "ORDERS")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Customer customer;

    @Column(name = "order_code", unique = true, length = 50)
    private String orderCode;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "incoterm", length = 3)
    private String incoterm;

    @Column(name = "status", length = 50)
    private String status;
}