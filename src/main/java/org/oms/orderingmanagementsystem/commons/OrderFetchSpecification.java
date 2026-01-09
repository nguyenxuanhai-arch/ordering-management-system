package org.oms.orderingmanagementsystem.commons;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.oms.orderingmanagementsystem.entities.Order;
import org.oms.orderingmanagementsystem.entities.OrderItem;
import org.oms.orderingmanagementsystem.entities.Product;
import org.oms.orderingmanagementsystem.entities.User;
import org.springframework.data.jpa.domain.Specification;

public class OrderFetchSpecification {

    public static Specification<Order> joinUserFilter(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            Join<Order, User> userJoin = root.join("user", JoinType.INNER);

            return cb.or(
                    cb.like(userJoin.get("email"), "%" + keyword + "%"),
                    cb.like(userJoin.get("name"), "%" + keyword + "%"),
                    cb.like(userJoin.get("phone"), "%" + keyword + "%")
            );
        };
    }

    public static Specification<Order> joinProductByName(String productName) {
        return (root, query, cb) -> {
            if (productName == null || productName.isBlank()) {
                return null;
            }

            Join<Order, OrderItem> orderItemJoin = root.join("orderItems", JoinType.INNER);
            Join<OrderItem, Product> productJoin = orderItemJoin.join("product", JoinType.INNER);

            return cb.like(productJoin.get("name"), "%" + productName + "%");
        };
    }


}

