# ✅ UPDATED TASKS - Theo Đúng Cấu Trúc Dự Án

## 🔄 Cập Nhật Theo Quy Tắc Dự Án

Tất cả tasks đã được sửa lại để tuân theo:
- ✅ **Cấu trúc thư mục** của dự án hiện tại
- ✅ **Quy tắc đặt tên** (Response DTOs, Mappers, Services)
- ✅ **Thư viện sử dụng** (Lombok, MapStruct, Spring Data JPA)
- ✅ **Kiến trúc phần mềm** (Interface + Implementation)

---

## 📋 Updated Task Files

### 📝 TASK 1: Dashboard - UPDATED ✅
**File**: `TASK_1_DASHBOARD_UPDATED.md`

**Tạo**:
- `DashboardResponse.java` (dtos/response)
- `RecentOrderResponse.java` (dtos/response)
- `RecentActivityResponse.java` (dtos/response)
- `DashboardServiceInterface.java` (services/interfaces)
- `DashboardService.java` (services/impls)

**Update**:
- `OrderRepository.java` - Thêm query methods
- `NotificationRepository.java` - Thêm query methods
- `PageController.java` - Dashboard method
- `dashboard.html` - Thymeleaf binding

---

### 📝 TASK 2: Users List - UPDATED ✅
**File**: `TASK_2_USERS_UPDATED.md`

**Tạo**:
- `UserResponse.java` (dtos/response)
- `UserResponseMapper.java` (mappers)

**Update**:
- `UserRepository.java` - Add methods (nếu cần)
- `UserService.java` - Thêm getAllUsers()
- `PageController.java` - Users method
- `users.html` - Thymeleaf binding

---

### 📝 TASK 3: Orders List - UPDATED ✅
**File**: `TASK_3_ORDERS_UPDATED.md`

**Update**:
- `OrderResponse.java` (mở rộng existing) - dtos/response
- `OrderRepository.java` - Add query methods
- `OrderMapper.java` - Update @Mapping
- `OrderService.java` - Thêm getAllOrders()
- `PageController.java` - Orders method
- `orders.html` - Thymeleaf binding

---

### 📝 TASK 4: Products List - UPDATED ✅
**File**: `TASK_4_PRODUCTS_UPDATED.md`

**Tạo**:
- `ProductResponse.java` (dtos/response)
- `ProductRepository.java` (repositories) - **NẾU CHƯA CÓ**
- `ProductMapper.java` (mappers)
- `ProductServiceInterface.java` (services/interfaces)
- `ProductService.java` (services/impls)

**Update**:
- `PageController.java` - Products method
- `products.html` - Thymeleaf binding

---

## 🏗️ Cấu Trúc Tuân Theo

### DTOs Structure
```
dtos/
├── request/
│   ├── LoginRequest.java
│   ├── OrderRequest.java
│   └── ...
└── response/
    ├── OrderResponse.java ✅ (Existing)
    ├── DashboardResponse.java (NEW)
    ├── RecentOrderResponse.java (NEW)
    ├── RecentActivityResponse.java (NEW)
    ├── UserResponse.java (NEW)
    └── ProductResponse.java (NEW)
```

### Services Structure
```
services/
├── interfaces/
│   ├── UserServiceInterface.java
│   ├── OrderServiceInterface.java
│   └── DashboardServiceInterface.java (NEW)
└── impls/
    ├── UserService.java
    ├── OrderService.java
    └── DashboardService.java (NEW)
```

### Mappers Structure
```
mappers/
├── UserMapper.java ✅ (Existing)
├── OrderMapper.java ✅ (Existing)
├── UserResponseMapper.java (NEW)
└── ProductMapper.java (NEW)
```

### Repositories
```
repositories/
├── UserRepository.java ✅
├── OrderRepository.java ✅ (Update methods)
├── NotificationRepository.java ✅ (Add methods)
└── ProductRepository.java (NEW)
```

---

## 🔧 Key Technology Stack

### Dùng Đúng:
- ✅ **@RequiredArgsConstructor** - Constructor injection
- ✅ **@Data** - Lombok for DTOs
- ✅ **MapStruct** - For data mapping
- ✅ **JpaRepository** + **JpaSpecificationExecutor**
- ✅ **@Query** - For custom queries
- ✅ **JpaRepository.findAll()** - Built-in method

### Không Dùng:
- ❌ `@Autowired` - Dùng constructor injection
- ❌ Manual mapping - Dùng MapStruct
- ❌ Custom DTOs names - Dùng Response/Request format

---

## 📝 Naming Conventions

### DTOs
- Response: `UserResponse.java`, `OrderResponse.java`
- Request: `OrderRequest.java`, `RegisterRequest.java`
- Mapper: `UserResponseMapper.java`, `ProductMapper.java`

### Services
- Interface: `UserServiceInterface.java`
- Implementation: `UserService.java` (với @RequiredArgsConstructor)

### Repositories
- `UserRepository.java` extends `JpaRepository<User, Long>`

---

## ✨ Chi Tiết Từng Task

### TASK 1: Dashboard (2-3 hours)
- Create 3 Response DTOs
- Create Dashboard Service
- Update Repositories (add query methods)
- Update Controller & Template

### TASK 2: Users (1.5-2 hours)
- Create UserResponse DTO
- Create UserResponseMapper
- Update UserService
- Update Controller & Template

### TASK 3: Orders (1.5-2 hours)
- Expand OrderResponse DTO
- Update OrderMapper
- Update OrderService
- Update Controller & Template

### TASK 4: Products (1.5-2 hours)
- Create ProductResponse DTO
- Create ProductRepository (if not exists)
- Create ProductMapper
- Create ProductService interface + implementation
- Update Controller & Template

---

## 🚀 Implementation Order

1. **Start**: TASK_1_DASHBOARD_UPDATED.md
2. **Then**: TASK_2_USERS_UPDATED.md
3. **Then**: TASK_3_ORDERS_UPDATED.md
4. **Then**: TASK_4_PRODUCTS_UPDATED.md

**Total Time**: 7-9 hours for all 4 tasks

---

## ✅ Checklist Before Starting

- [ ] Read each UPDATED task file carefully
- [ ] Follow folder structure exactly
- [ ] Use Response DTOs (not custom names)
- [ ] Use MapStruct for mapping
- [ ] Use @RequiredArgsConstructor for injection
- [ ] Use @Data for DTOs
- [ ] Use @Query for complex queries
- [ ] Test each task after implementation

---

## 📚 Resources in Updated Tasks

Mỗi task file bao gồm:
✅ Exact file paths
✅ Complete code examples
✅ Proper annotations
✅ Correct imports
✅ Step-by-step instructions
✅ Checklist to verify
✅ Testing guidance

---

## 🎯 Summary

**Old Tasks**: Generic, không tuân theo cấu trúc dự án
**New Updated Tasks**: Chính xác, tuân theo tất cả quy tắc dự án

**Ready to implement!** 🚀

---

**Updated**: 2026-01-12
**Status**: ✅ Ready for implementation
**Quality**: Production-ready code examples

