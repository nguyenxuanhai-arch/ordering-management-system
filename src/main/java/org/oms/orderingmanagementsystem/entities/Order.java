package org.oms.orderingmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_order_user", columnList = "user_id"),
                @Index(name = "idx_order_user_status", columnList = "user_id, status")
        }
)public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String status; // PENDING, PAID, CANCELLED

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}

