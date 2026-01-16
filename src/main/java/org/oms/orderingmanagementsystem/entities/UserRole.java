package org.oms.orderingmanagementsystem.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "user_role")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    private User user;

    @Getter
    @ManyToOne
    @MapsId("roleId")
    private Role role;

}

