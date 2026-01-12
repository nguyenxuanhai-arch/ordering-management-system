# 📝 TASK 3 DETAIL: Orders Page - Display Orders List

## 🎯 Mục Tiêu
Hiển thị danh sách tất cả đơn hàng với thông tin cơ bản (chỉ READ)

---

## 📋 Chi Tiết Công Việc

### Step 1: Mở rộng OrderResponse DTO

**File**: `src/main/java/org/oms/orderingmanagementsystem/dtos/response/OrderResponse.java`

```java
package org.oms.orderingmanagementsystem.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String customerName;
    private Double totalAmount;
    private String status;
    private Integer itemCount;
    private LocalDateTime createdAt;
}
```

---

### Step 2: Update Repository

**File**: `src/main/java/org/oms/orderingmanagementsystem/repositories/OrderRepository.java`

```java
package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findTop5ByOrderByCreatedAtDesc();
    
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0.0) FROM Order o")
    Double calculateTotalRevenue();
}
```

---

### Step 3: Update Mapper

**File**: `src/main/java/org/oms/orderingmanagementsystem/mappers/OrderMapper.java`

```java
package org.oms.orderingmanagementsystem.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.oms.orderingmanagementsystem.dtos.response.OrderResponse;
import org.oms.orderingmanagementsystem.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    @Mapping(target = "customerName", source = "user.name")
    @Mapping(target = "orderNumber", expression = "java(\"ORD-\" + order.getId())")
    @Mapping(target = "itemCount", expression = "java(order.getOrderItems() != null ? order.getOrderItems().size() : 0)")
    OrderResponse toResponse(Order order);
}
```

---

### Step 4: Update Service

**File**: `src/main/java/org/oms/orderingmanagementsystem/services/impls/OrderService.java`

```java
package org.oms.orderingmanagementsystem.services.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.oms.orderingmanagementsystem.dtos.response.OrderResponse;
import org.oms.orderingmanagementsystem.mappers.OrderMapper;
import org.oms.orderingmanagementsystem.repositories.OrderRepository;
import org.oms.orderingmanagementsystem.services.interfaces.OrderServiceInterface;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface {
    
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
            .stream()
            .map(orderMapper::toResponse)
            .collect(Collectors.toList());
    }
}
```

---

### Step 5: Update PageController

**File**: `src/main/java/org/oms/orderingmanagementsystem/controllers/PageController.java`

```java
@RequiredArgsConstructor
@Controller
public class PageController {
    
    private final OrderService orderService;
    
    @GetMapping("/orders")
    public String orders(Model model) {
        List<OrderResponse> orderList = orderService.getAllOrders();
        
        model.addAttribute("orders", orderList);
        model.addAttribute("pageTitle", "Orders");
        model.addAttribute("activePage", "orders");
        
        return "orders";
    }
}
```

---

### Step 6: Update orders.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" th:replace="~{layout/main}">

<div th:fragment="content" class="row">
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">Orders List</h3>
                <div class="card-tools">
                    <button type="button" class="btn btn-primary btn-sm">
                        <i class="fas fa-plus"></i> New Order
                    </button>
                </div>
            </div>
            <div class="card-body">
                <div th:if="${orders.isEmpty()}" class="alert alert-info">
                    <i class="fas fa-info-circle"></i> No orders found.
                </div>
                
                <div th:if="${!orders.isEmpty()}">
                    <table id="ordersTable" class="table table-bordered table-hover" data-table>
                        <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Customer</th>
                            <th>Items</th>
                            <th>Total Amount</th>
                            <th>Status</th>
                            <th>Date</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr th:each="order : ${orders}">
                            <td th:text="${order.orderNumber}">#ORD-12345</td>
                            <td th:text="${order.customerName}">John Doe</td>
                            <td th:text="${order.itemCount}">3</td>
                            <td th:text="${#numbers.formatCurrency(order.totalAmount)}">$1,250.00</td>
                            <td>
                                <span th:if="${order.status == 'PENDING'}" class="badge badge-warning">Pending</span>
                                <span th:if="${order.status == 'PROCESSING'}" class="badge badge-info">Processing</span>
                                <span th:if="${order.status == 'COMPLETED'}" class="badge badge-success">Completed</span>
                                <span th:if="${order.status == 'CANCELLED'}" class="badge badge-danger">Cancelled</span>
                            </td>
                            <td th:text="${#dates.format(order.createdAt, 'yyyy-MM-dd')}">2026-01-10</td>
                            <td>
                                <button class="btn btn-info btn-sm" title="View"><i class="fas fa-eye"></i></button>
                                <button class="btn btn-warning btn-sm" title="Edit"><i class="fas fa-edit"></i></button>
                                <button class="btn btn-danger btn-sm" title="Delete"><i class="fas fa-trash"></i></button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

</html>
```

---

## ✅ Checklist

- [ ] Mở rộng OrderResponse DTO
- [ ] Update OrderRepository
- [ ] Update OrderMapper
- [ ] Update OrderService với getAllOrders() method
- [ ] Update PageController orders() method
- [ ] Update orders.html
- [ ] Test: Build & Run
- [ ] Test: http://localhost:8080/orders

---

**Priority**: 🟡 **MEDIUM**
**Estimated Time**: 1.5-2 hours
**Created**: 2026-01-12

