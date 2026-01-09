package org.oms.orderingmanagementsystem.mappers;

import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.oms.orderingmanagementsystem.dtos.response.OrderResponse;
import org.oms.orderingmanagementsystem.entities.Order;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "address", source = "user.address")
    @Mapping(target = "productName", source = "product.name")
    OrderResponse toResponse(Order order);
    List<OrderResponse> toListResponse(List<Order> orders);
    default Page<OrderResponse> toPageResponse(Page<Order> orders) {
        return orders.map(this::toResponse);
    }
}
