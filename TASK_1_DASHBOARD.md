# 📝 TASK 1 DETAIL: Dashboard Page - Display Statistics

## 🎯 Mục Tiêu
Hiển thị thống kê tổng quan:
- Tổng số Users
- Tổng số Orders  
- Tổng số Products
- Tổng doanh thu
- 5 đơn hàng gần nhất
- 10 hoạt động gần nhất

---

## 📋 Chi Tiết Công Việc

### Step 1: Tạo Response DTOs

**File**: `src/main/java/org/oms/orderingmanagementsystem/dtos/response/DashboardResponse.java`

```java
package org.oms.orderingmanagementsystem.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Long totalUsers;
    private Long totalOrders;
    private Long totalProducts;
    private Double totalRevenue;
    private List<RecentOrderResponse> recentOrders;
    private List<RecentActivityResponse> recentActivities;
}
```

**File**: `src/main/java/org/oms/orderingmanagementsystem/dtos/response/RecentOrderResponse.java`

```java
package org.oms.orderingmanagementsystem.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentOrderResponse {
    private Long id;
    private String orderNumber;
    private String customerName;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;
}
```

**File**: `src/main/java/org/oms/orderingmanagementsystem/dtos/response/RecentActivityResponse.java`

```java
package org.oms.orderingmanagementsystem.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentActivityResponse {
    private Long id;
    private String type;
    private String description;
    private String icon;
    private String color;
    private LocalDateTime createdAt;
}
```

---

### Step 2: Update Repositories

**File**: `src/main/java/org/oms/orderingmanagementsystem/repositories/OrderRepository.java`

Thêm methods:
```java
import org.springframework.data.jpa.repository.Query;
import java.util.List;

List<Order> findTop5ByOrderByCreatedAtDesc();

@Query("SELECT COALESCE(SUM(o.totalAmount), 0.0) FROM Order o")
Double calculateTotalRevenue();
```

**File**: `src/main/java/org/oms/orderingmanagementsystem/repositories/NotificationRepository.java`

Thêm method:
```java
import java.util.List;

List<Notification> findTop10ByOrderByCreatedAtDesc();
```

---

### Step 3: Tạo Service Interface

**File**: `src/main/java/org/oms/orderingmanagementsystem/services/interfaces/DashboardServiceInterface.java`

```java
package org.oms.orderingmanagementsystem.services.interfaces;

import org.oms.orderingmanagementsystem.dtos.response.DashboardResponse;

public interface DashboardServiceInterface {
    DashboardResponse getDashboardStatistics();
}
```

---

### Step 4: Tạo Service Implementation

**File**: `src/main/java/org/oms/orderingmanagementsystem/services/impls/DashboardService.java`

```java
package org.oms.orderingmanagementsystem.services.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.oms.orderingmanagementsystem.dtos.response.*;
import org.oms.orderingmanagementsystem.entities.Order;
import org.oms.orderingmanagementsystem.repositories.*;
import org.oms.orderingmanagementsystem.services.interfaces.DashboardServiceInterface;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService implements DashboardServiceInterface {
    
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;
    
    @Override
    public DashboardResponse getDashboardStatistics() {
        Long totalUsers = userRepository.count();
        Long totalOrders = orderRepository.count();
        Long totalProducts = 0L; // TODO: Từ ProductRepository nếu có
        Double totalRevenue = orderRepository.calculateTotalRevenue();
        
        List<RecentOrderResponse> recentOrders = orderRepository.findTop5ByOrderByCreatedAtDesc()
            .stream()
            .map(order -> new RecentOrderResponse(
                order.getId(),
                "ORD-" + order.getId(),
                order.getUser().getName(),
                order.getTotalAmount(),
                order.getStatus().toString(),
                order.getCreatedAt()
            ))
            .collect(Collectors.toList());
        
        List<RecentActivityResponse> recentActivities = notificationRepository.findTop10ByOrderByCreatedAtDesc()
            .stream()
            .map(notification -> new RecentActivityResponse(
                notification.getId(),
                "NOTIFICATION",
                notification.getMessage(),
                "fas fa-bell",
                "text-info",
                notification.getCreatedAt()
            ))
            .collect(Collectors.toList());
        
        return new DashboardResponse(
            totalUsers,
            totalOrders,
            totalProducts,
            totalRevenue != null ? totalRevenue : 0.0,
            recentOrders,
            recentActivities
        );
    }
}
```

---

### Step 5: Update PageController

**File**: `src/main/java/org/oms/orderingmanagementsystem/controllers/PageController.java`

Inject service và update method:
```java
@RequiredArgsConstructor
@Controller
public class PageController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DashboardResponse dashboardStats = dashboardService.getDashboardStatistics();
        
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("dashboard", dashboardStats);
        
        return "dashboard";
    }
}
```

---

### Step 6: Update dashboard.html

```html
<div class="row">
    <div class="col-12 col-sm-6 col-md-3">
        <div class="info-box">
            <span class="info-box-icon bg-info elevation-1"><i class="fas fa-users"></i></span>
            <div class="info-box-content">
                <span class="info-box-text">Total Users</span>
                <span class="info-box-number" th:text="${dashboard.totalUsers}">1,234</span>
            </div>
        </div>
    </div>

    <div class="col-12 col-sm-6 col-md-3">
        <div class="info-box mb-3">
            <span class="info-box-icon bg-success elevation-1"><i class="fas fa-shopping-cart"></i></span>
            <div class="info-box-content">
                <span class="info-box-text">Total Orders</span>
                <span class="info-box-number" th:text="${dashboard.totalOrders}">567</span>
            </div>
        </div>
    </div>

    <div class="col-12 col-sm-6 col-md-3">
        <div class="info-box mb-3">
            <span class="info-box-icon bg-warning elevation-1"><i class="fas fa-box"></i></span>
            <div class="info-box-content">
                <span class="info-box-text">Total Products</span>
                <span class="info-box-number" th:text="${dashboard.totalProducts}">89</span>
            </div>
        </div>
    </div>

    <div class="col-12 col-sm-6 col-md-3">
        <div class="info-box mb-3">
            <span class="info-box-icon bg-danger elevation-1"><i class="fas fa-dollar-sign"></i></span>
            <div class="info-box-content">
                <span class="info-box-text">Total Revenue</span>
                <span class="info-box-number" th:text="${#numbers.formatCurrency(dashboard.totalRevenue)}">$45,250</span>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <section class="col-lg-8">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">Recent Orders</h3>
            </div>
            <div class="card-body">
                <table class="table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Customer</th>
                            <th>Amount</th>
                            <th>Status</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr th:each="order : ${dashboard.recentOrders}">
                            <td th:text="${order.id}">#12345</td>
                            <td th:text="${order.customerName}">John Doe</td>
                            <td th:text="${#numbers.formatCurrency(order.totalAmount)}">$1,250.00</td>
                            <td>
                                <span th:if="${order.status == 'COMPLETED'}" class="badge badge-success">Completed</span>
                                <span th:if="${order.status == 'PENDING'}" class="badge badge-warning">Pending</span>
                                <span th:if="${order.status == 'PROCESSING'}" class="badge badge-info">Processing</span>
                            </td>
                            <td th:text="${#dates.format(order.createdAt, 'yyyy-MM-dd HH:mm')}">2026-01-10 14:30</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </section>

    <section class="col-lg-4">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">Recent Activities</h3>
            </div>
            <div class="card-body p-0">
                <ul class="nav flex-column">
                    <li class="nav-item border-bottom" th:each="activity : ${dashboard.recentActivities}">
                        <a href="#" class="nav-link">
                            <i class="fas fa-circle" th:classappend="${activity.color}"></i>
                            <span th:text="${activity.description}">New Order</span>
                            <span class="float-right text-muted text-sm" th:text="${#dates.format(activity.createdAt, 'HH:mm a')}">5 mins ago</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </section>
</div>
```

---

## ✅ Checklist

- [ ] Create 3 Response DTOs in dtos/response folder
- [ ] Update OrderRepository với query methods
- [ ] Update NotificationRepository
- [ ] Create DashboardServiceInterface
- [ ] Create DashboardService implementation
- [ ] Update PageController
- [ ] Update dashboard.html
- [ ] Test: Build & Run
- [ ] Test: http://localhost:8080/dashboard

---

**Priority**: 🔴 **HIGH**
**Estimated Time**: 2-3 hours
**Created**: 2026-01-12

