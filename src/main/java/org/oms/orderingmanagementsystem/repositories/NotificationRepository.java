package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
