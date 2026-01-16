package org.oms.orderingmanagementsystem.commons;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class UserFethchSpecification {
    public static <T> Specification<T> hasRole(String roleName) {
        return (root, query, cb) -> {
            Join<?, ?> userRoleJoin = root.join("userRoles", JoinType.INNER);
            Join<?, ?> roleJoin = userRoleJoin.join("role", JoinType.INNER);
            return cb.equal(cb.lower(roleJoin.get("name")), roleName.toLowerCase());
        };
    }


    public static <T> Specification<T> fetchUserRoles() {
        return (root, query, cb) -> {
            return cb.conjunction();
        };
    }

}
