package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("""
        SELECT u FROM User u
        WHERE (:spec IS NULL OR 1=1)
    """)
    Page<User> findAllSlice(Specification<User> spec, Pageable pageable);
}
