package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.Notification; // Nhớ import entity Notification
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Thêm dòng này vào đây
    List<Notification> findTop10ByOrderByCreatedAtDesc();
}