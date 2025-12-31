package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
