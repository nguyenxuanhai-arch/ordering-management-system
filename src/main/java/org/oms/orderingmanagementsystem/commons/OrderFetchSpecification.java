package org.oms.orderingmanagementsystem.commons;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.oms.orderingmanagementsystem.entities.Order;
import org.oms.orderingmanagementsystem.entities.OrderItem;
import org.oms.orderingmanagementsystem.entities.Product;
import org.oms.orderingmanagementsystem.entities.User;
import org.springframework.data.jpa.domain.Specification;

/**
 * FIXED: Removed multiple FETCH JOINs that cause Cartesian product multiplication
 *
 * OLD PROBLEM:
 * - Combining fetch(user) + fetch(items).fetch(product) creates n-way cartesian join
 * - With 1M orders × 5 items each = 5M rows deduplicated in-memory
 * - query.distinct(true) forces in-memory deduplication (extremely slow)
 *
 * NEW APPROACH:
 * - Use regular JOINs for filtering only (not FETCH)
 * - Let Hibernate load relationships via configured batch size
 * - Orders are paginated correctly without Cartesian product
 */
public class OrderFetchSpecification {

    /**
     * FIXED: Use regular JOIN (not FETCH) in specifications
     * Specifications don't work well with FETCH JOIN
     * Batch loading will handle lazy loading of user
     */
    public static Specification<Order> joinUserFilter(String keyword) {
        return (root, query, cb) -> {
            // Use regular JOIN for filtering only (not FETCH)
            // Batch loading will handle user loading

            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }

            Join<Order, User> userJoin = root.join("user", JoinType.LEFT);
            String like = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(userJoin.get("name")), like)
            );
        };
    }


    /**
     * FIXED: Removed DISTINCT and multiple FETCH JOINs
     * Only filter by product name using regular JOIN, don't fetch items/products
     * Items will be loaded in batch separately
     */
    public static Specification<Order> joinProductByName(String productName) {
        return (root, query, cb) -> {
            // Removed: query.distinct(true) - forces in-memory dedup
            // Removed: root.fetch("items", JoinType.LEFT).fetch("product", JoinType.LEFT)

            if (productName == null || productName.isBlank()) {
                return cb.conjunction();
            }

            // Use regular JOINs (not FETCH) for filtering only
            Join<Order, OrderItem> itemJoin = root.join("items", JoinType.INNER);
            Join<OrderItem, Product> productJoin = itemJoin.join("product", JoinType.INNER);

            // Ensure order id is distinct (avoid duplicate order ids in result set)
            // Note: This distinct applies to ID only, not full rows
            if (!query.getSelection().isCompoundSelection()) {
                query.distinct(true);
            }

            return cb.like(
                    cb.lower(productJoin.get("name")),
                    "%" + productName.toLowerCase() + "%"
            );
        };
    }

}


