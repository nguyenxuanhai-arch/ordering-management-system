package org.oms.orderingmanagementsystem.entities;

import jakarta.persistence.*;

@Entity
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Notification notification;

    private Boolean isRead;
}
