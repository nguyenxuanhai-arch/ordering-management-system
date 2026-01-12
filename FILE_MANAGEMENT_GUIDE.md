# 📁 File Management Guide

## 📂 Where to Create/Update Files

### DTOs (Data Transfer Objects)
**Location**: `src/main/java/org/oms/orderingmanagementsystem/dtos/`

Files to create:
- `DashboardDTO.java`
- `RecentOrderDTO.java`
- `RecentActivityDTO.java`
- `UserDTO.java`
- `OrderDTO.java`
- `ProductDTO.java`

---

### Repositories
**Location**: `src/main/java/org/oms/orderingmanagementsystem/repositories/`

Files to create:
- `ProductRepository.java` (if not exists)

Files to update:
- `UserRepository.java` - Add count() method
- `OrderRepository.java` - Add methods for recent orders
- `NotificationRepository.java` - Add method for recent activities

---

### Services - Interfaces
**Location**: `src/main/java/org/oms/orderingmanagementsystem/services/interfaces/`

Files to create:
- `DashboardServiceInterface.java`
- `UserListServiceInterface.java`
- `OrderListServiceInterface.java`
- `ProductListServiceInterface.java`

---

### Services - Implementations
**Location**: `src/main/java/org/oms/orderingmanagementsystem/services/impls/`

Files to create:
- `DashboardService.java`
- `UserListService.java`
- `OrderListService.java`
- `ProductListService.java`

---

### Controllers
**Location**: `src/main/java/org/oms/orderingmanagementsystem/controllers/`

Files to update:
- `PageController.java` - Add methods for all pages

---

### Templates (HTML)
**Location**: `src/main/resources/templates/`

Files to update:
- `dashboard.html`
- `users.html`
- `orders.html`
- `products.html`

---

## 📋 Creating/Updating Workflow

### Step 1: Create DTOs First
```
src/main/java/org/oms/orderingmanagementsystem/dtos/
├── DashboardDTO.java
├── RecentOrderDTO.java
├── RecentActivityDTO.java
├── UserDTO.java
├── OrderDTO.java
└── ProductDTO.java
```

### Step 2: Create Repositories (if needed)
```
src/main/java/org/oms/orderingmanagementsystem/repositories/
├── ProductRepository.java (new)
└── Update existing ones
```

### Step 3: Create Service Interfaces
```
src/main/java/org/oms/orderingmanagementsystem/services/interfaces/
├── DashboardServiceInterface.java
├── UserListServiceInterface.java
├── OrderListServiceInterface.java
└── ProductListServiceInterface.java
```

### Step 4: Create Service Implementations
```
src/main/java/org/oms/orderingmanagementsystem/services/impls/
├── DashboardService.java
├── UserListService.java
├── OrderListService.java
└── ProductListService.java
```

### Step 5: Update Controllers
```
src/main/java/org/oms/orderingmanagementsystem/controllers/
└── PageController.java (update)
```

### Step 6: Update Templates
```
src/main/resources/templates/
├── dashboard.html (update)
├── users.html (update)
├── orders.html (update)
└── products.html (update)
```

---

## 🔄 Dependency Flow

```
Template (HTML)
    ↓
Controller (PageController)
    ↓
Service (DashboardService, etc)
    ↓
Repository (UserRepository, etc)
    ↓
Entity (User, Order, Product)
    ↓
Database (MySQL/H2)
```

---

## 💾 Recommended Creation Order

### Priority 1 (TASK 1 - Dashboard)
1. Create 3 DTOs (Dashboard, RecentOrder, RecentActivity)
2. Create DashboardServiceInterface
3. Create DashboardService
4. Update PageController - dashboard() method
5. Update dashboard.html

### Priority 2 (TASK 2 - Users)
1. Create UserDTO
2. Create UserListServiceInterface
3. Create UserListService
4. Update PageController - users() method
5. Update users.html

### Priority 3 (TASK 3 - Orders)
1. Create OrderDTO
2. Create OrderListServiceInterface
3. Create OrderListService
4. Update PageController - orders() method
5. Update orders.html

### Priority 4 (TASK 4 - Products)
1. Create ProductRepository (if not exists)
2. Create ProductDTO
3. Create ProductListServiceInterface
4. Create ProductListService
5. Update PageController - products() method
6. Update products.html

---

## 🔍 File Checklist

### For TASK 1 (Dashboard)
- [ ] DashboardDTO.java created
- [ ] RecentOrderDTO.java created
- [ ] RecentActivityDTO.java created
- [ ] DashboardServiceInterface.java created
- [ ] DashboardService.java created
- [ ] UserRepository.count() added
- [ ] OrderRepository methods added
- [ ] NotificationRepository methods added
- [ ] PageController.dashboard() updated
- [ ] dashboard.html updated

### For TASK 2 (Users)
- [ ] UserDTO.java created
- [ ] UserListServiceInterface.java created
- [ ] UserListService.java created
- [ ] PageController.users() updated
- [ ] users.html updated

### For TASK 3 (Orders)
- [ ] OrderDTO.java created
- [ ] OrderListServiceInterface.java created
- [ ] OrderListService.java created
- [ ] PageController.orders() updated
- [ ] orders.html updated

### For TASK 4 (Products)
- [ ] ProductRepository.java created (if needed)
- [ ] ProductDTO.java created
- [ ] ProductListServiceInterface.java created
- [ ] ProductListService.java created
- [ ] PageController.products() updated
- [ ] products.html updated

---

## 📦 Current Project Structure

```
src/
├── main/
│   ├── java/org/oms/orderingmanagementsystem/
│   │   ├── commons/
│   │   ├── controllers/           ← Update PageController.java
│   │   ├── cronjobs/
│   │   ├── dtos/                  ← Create DTOs here
│   │   ├── entities/              ← Already exist (User, Order, Product)
│   │   ├── mappers/
│   │   ├── repositories/          ← Update & create repositories
│   │   ├── securities/
│   │   ├── services/
│   │   │   ├── interfaces/        ← Create service interfaces
│   │   │   └── impls/            ← Create service implementations
│   │   └── OrderingManagementSystemApplication.java
│   └── resources/
│       ├── templates/
│       │   ├── dashboard.html      ← Update
│       │   ├── users.html         ← Update
│       │   ├── orders.html        ← Update
│       │   ├── products.html      ← Update
│       │   └── layout/
│       ├── static/
│       └── application.properties
└── test/
```

---

## ⚙️ Build & Test

### After Creating Files
```bash
# 1. Build to check for compile errors
mvn clean compile

# 2. Run tests (if any)
mvn test

# 3. Package
mvn clean package -DskipTests

# 4. Run application
java -jar target/ordering-management-system-0.0.1-SNAPSHOT.jar
```

---

## 🎯 Summary

1. **Create in order**: DTOs → Repositories → Services → Controllers → Templates
2. **Test after each step**: Compile, Run, Check
3. **Follow the task documents**: Each task has step-by-step instructions
4. **Use the code examples**: Provided in task files
5. **Update existing files**: Controllers and templates

---

**Ready to start?** Pick TASK_1_DASHBOARD.md and follow the steps! 🚀

