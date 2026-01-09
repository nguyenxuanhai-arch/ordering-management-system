package org.oms.orderingmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email", unique = true)
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String name;

    private String password;
    private String phone;
    private String address;
}

