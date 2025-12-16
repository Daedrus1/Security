package mate.academy.security.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import mate.academy.security.security.status.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private OrderStatus status;
    private BigDecimal total;
    private String shippingAddress;
    private Set<OrderI>
}
