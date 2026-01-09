package org.oms.orderingmanagementsystem.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name = "user_notification",
        indexes = {
                @Index(name = "idx_user_notification_user", columnList = "user_id"),
                @Index(name = "idx_user_notification_user_read", columnList = "user_id, isRead")
        }
)
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
