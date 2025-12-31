package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
