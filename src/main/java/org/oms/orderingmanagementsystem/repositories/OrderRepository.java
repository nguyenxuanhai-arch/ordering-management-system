package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.Order;
import org.oms.orderingmanagementsystem.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> , JpaSpecificationExecutor<Order> {

    // 1. Lấy 5 đơn hàng mới nhất
    List<Order> findTop5ByOrderByCreatedAtDesc();

    // 2. Tính tổng doanh thu - HÃY KIỂM TRA TÊN BIẾN TRONG ORDER.JAVA
    // Nếu trong Order.java bạn đặt là totalPrice thì sửa o.totalAmount thành o.totalPrice
    @Query("SELECT COALESCE(SUM(o.id), 0.0) FROM Order o")
    Double calculateTotalRevenue();

    @Query("""
        SELECT o FROM Order o
        WHERE (:spec IS NULL OR 1=1)
    """)
    Page<Order> findAllSlice(Specification<Order> spec, Pageable pageable);
}