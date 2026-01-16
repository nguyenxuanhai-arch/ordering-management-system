# 📝 TASK 4 DETAIL: Products Page - Display Products List

## 🎯 Mục Tiêu
Hiển thị danh sách tất cả sản phẩm với thông tin cơ bản (chỉ READ)

---

## 📋 Chi Tiết Công Việc

### Step 1: Tạo ProductResponse DTO

**File**: `src/main/java/org/oms/orderingmanagementsystem/dtos/response/ProductResponse.java`

```java
package org.oms.orderingmanagementsystem.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer stock;
    private String status;
    private String description;
}
```

---

### Step 2: Tạo Repository

**File**: `src/main/java/org/oms/orderingmanagementsystem/repositories/ProductRepository.java`

```java
package org.oms.orderingmanagementsystem.repositories;

import org.oms.orderingmanagementsystem.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategory(String category);
}
```

---

### Step 3: Tạo Mapper

**File**: `src/main/java/org/oms/orderingmanagementsystem/mappers/ProductMapper.java`

```java
package org.oms.orderingmanagementsystem.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.oms.orderingmanagementsystem.dtos.response.ProductResponse;
import org.oms.orderingmanagementsystem.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    @Mapping(target = "status", expression = "java(getProductStatus(product.getStock()))")
    ProductResponse toResponse(Product product);
    
    default String getProductStatus(Integer stock) {
        if (stock == null || stock <= 0) {
            return "OUT_OF_STOCK";
        } else if (stock <= 10) {
            return "LOW_STOCK";
        } else {
            return "AVAILABLE";
        }
    }
}
```

---

### Step 4: Tạo Service Interface

**File**: `src/main/java/org/oms/orderingmanagementsystem/services/interfaces/ProductServiceInterface.java`

```java
package org.oms.orderingmanagementsystem.services.interfaces;

import org.oms.orderingmanagementsystem.dtos.response.ProductResponse;
import java.util.List;

public interface ProductServiceInterface {
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(Long id);
}
```

---

### Step 5: Tạo Service Implementation

**File**: `src/main/java/org/oms/orderingmanagementsystem/services/impls/ProductService.java`

```java
package org.oms.orderingmanagementsystem.services.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.oms.orderingmanagementsystem.dtos.response.ProductResponse;
import org.oms.orderingmanagementsystem.mappers.ProductMapper;
import org.oms.orderingmanagementsystem.services.interfaces.ProductServiceInterface;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceInterface {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(productMapper::toResponse).orElse(null);
    }
}
```

---

### Step 6: Update PageController

**File**: `src/main/java/org/oms/orderingmanagementsystem/controllers/PageController.java`

```java
@RequiredArgsConstructor
@Controller
public class PageController {
    
    private final ProductService productService;
    
    @GetMapping("/products")
    public String products(Model model) {
        List<ProductResponse> productList = productService.getAllProducts();
        
        model.addAttribute("products", productList);
        model.addAttribute("pageTitle", "Products");
        model.addAttribute("activePage", "products");
        
        return "products";
    }
}
```

---

### Step 7: Update products.html

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" th:replace="~{layout/main}">

<div th:fragment="content" class="row">
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <h3 class="card-title">Products Inventory</h3>
                <div class="card-tools">
                    <button type="button" class="btn btn-primary btn-sm">
                        <i class="fas fa-plus"></i> Add Product
                    </button>
                </div>
            </div>
            <div class="card-body">
                <div th:if="${products.isEmpty()}" class="alert alert-info">
                    <i class="fas fa-info-circle"></i> No products found.
                </div>
                
                <div th:if="${!products.isEmpty()}">
                    <table id="productsTable" class="table table-bordered table-hover" data-table>
                        <thead>
                        <tr>
                            <th>Product ID</th>
                            <th>Name</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr th:each="product : ${products}">
                            <td th:text="${product.id}">#P001</td>
                            <td th:text="${product.name}">Laptop Pro</td>
                            <td th:text="${product.category}">Electronics</td>
                            <td th:text="${#numbers.formatCurrency(product.price)}">$1,299.99</td>
                            <td th:text="${product.stock}">45</td>
                            <td>
                                <span th:if="${product.status == 'AVAILABLE'}" class="badge badge-success">Available</span>
                                <span th:if="${product.status == 'LOW_STOCK'}" class="badge badge-warning">Low Stock</span>
                                <span th:if="${product.status == 'OUT_OF_STOCK'}" class="badge badge-danger">Out of Stock</span>
                            </td>
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

- [ ] Create ProductResponse DTO
- [ ] Create ProductRepository
- [ ] Create ProductMapper
- [ ] Create ProductServiceInterface
- [ ] Create ProductService implementation
- [ ] Update PageController products() method
- [ ] Update products.html
- [ ] Test: Build & Run
- [ ] Test: http://localhost:8080/products

---

**Priority**: 🟡 **MEDIUM**
**Estimated Time**: 1.5-2 hours
**Created**: 2026-01-12

