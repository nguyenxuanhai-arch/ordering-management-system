package org.oms.orderingmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email", unique = true),
                @Index(name = "idx_user_name", columnList = "name"),
                @Index(name = "idx_user_phone", columnList = "phone"),
                @Index(name = "idx_user_address", columnList = "address")
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

