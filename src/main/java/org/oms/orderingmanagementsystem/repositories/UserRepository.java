package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
