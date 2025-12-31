package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
