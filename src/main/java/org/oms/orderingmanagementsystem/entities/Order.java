package org.oms.orderingmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_order_user", columnList = "user_id"),
                @Index(name = "idx_order_user_status", columnList = "user_id, status"),
                @Index(name = "idx_order_created_at", columnList = "created_at"),
                @Index(name = "idx_order_created_pagination", columnList = "created_at DESC, id")
        }
)
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * FIXED: Changed from CascadeType.ALL to PERSIST, MERGE only
     *
     * Problem with CascadeType.ALL:
     * - Every order update cascades to all items (1M+ cascade operations)
     * - orphanRemoval=true requires checking each item individually
     * - Causes 1M+ DELETE/UPDATE queries for simple order update
     *
     * New approach:
     * - PERSIST: items are saved when order is first created
     * - MERGE: items are updated when order is merged
     * - DELETE: require manual deletion, preventing accidental cascades
     * - NO orphanRemoval: prevents auto-delete checks
     */
    @OneToMany(
            mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<OrderItem> items;

    private LocalDateTime createdAt;
}


