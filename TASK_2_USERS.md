# 📝 TASK 2 DETAIL: Users Page - Display User List

## 🎯 Mục Tiêu
Hiển thị danh sách tất cả users với thông tin cơ bản (chỉ READ)

---

## 📋 Chi Tiết Công Việc

### Step 1: Tạo UserResponse DTO

**File**: `src/main/java/org/oms/orderingmanagementsystem/dtos/response/UserResponse.java`

```java
package org.oms.orderingmanagementsystem.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String status;
    private LocalDateTime createdAt;
}
```

---

### Step 2: Update Repository

**File**: `src/main/java/org/oms/orderingmanagementsystem/repositories/UserRepository.java`

```java
package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // findAll() có sẵn từ JpaRepository
}
```

---

### Step 3: Tạo Mapper

**File**: `src/main/java/org/oms/orderingmanagementsystem/mappers/UserResponseMapper.java`

```java
package org.oms.orderingmanagementsystem.mappers;

import org.mapstruct.Mapper;
import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.oms.orderingmanagementsystem.entities.User;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {
    UserResponse toResponse(User user);
}
```

---

### Step 4: Update Service

**File**: `src/main/java/org/oms/orderingmanagementsystem/services/impls/UserService.java`

```java
package org.oms.orderingmanagementsystem.services.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.oms.orderingmanagementsystem.mappers.UserResponseMapper;
import org.oms.orderingmanagementsystem.repositories.UserRepository;
import org.oms.orderingmanagementsystem.services.interfaces.UserServiceInterface;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(userResponseMapper::toResponse)
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
    
    private final UserService userService;
    
    @GetMapping("/users")
    public String users(Model model) {
        List<UserResponse> userList = userService.getAllUsers();
        
        model.addAttribute("users", userList);
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("activePage", "users");
        
        return "users";
    }
}
```

---

### Step 6: Update users.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" th:replace="~{layout/main}">

<div th:fragment="content" class="row">
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">Users Management</h3>
                <div class="card-tools">
                    <button type="button" class="btn btn-primary btn-sm">
                        <i class="fas fa-plus"></i> Add User
                    </button>
                </div>
            </div>
            <div class="card-body">
                <div th:if="${users.isEmpty()}" class="alert alert-info">
                    <i class="fas fa-info-circle"></i> No users found.
                </div>
                
                <div th:if="${!users.isEmpty()}">
                    <table id="usersTable" class="table table-bordered table-hover" data-table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Joined Date</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr th:each="user : ${users}">
                            <td th:text="${user.id}">1</td>
                            <td th:text="${user.name}">John Doe</td>
                            <td th:text="${user.email}">john@example.com</td>
                            <td>
                                <span th:if="${user.role == 'ADMIN'}" class="badge badge-primary">Admin</span>
                                <span th:if="${user.role == 'USER'}" class="badge badge-secondary">User</span>
                            </td>
                            <td>
                                <span th:if="${user.status == 'ACTIVE'}" class="badge badge-success">Active</span>
                                <span th:if="${user.status == 'INACTIVE'}" class="badge badge-danger">Inactive</span>
                            </td>
                            <td th:text="${#dates.format(user.createdAt, 'yyyy-MM-dd')}">2025-06-15</td>
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

- [ ] Create UserResponse DTO in dtos/response folder
- [ ] Create UserResponseMapper
- [ ] Update UserService với getAllUsers() method
- [ ] Update PageController users() method
- [ ] Update users.html
- [ ] Test: Build & Run
- [ ] Test: http://localhost:8080/users

---

**Priority**: 🟡 **MEDIUM**
**Estimated Time**: 1.5-2 hours
**Created**: 2026-01-12

