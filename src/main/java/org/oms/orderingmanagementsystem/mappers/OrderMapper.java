package org.oms.orderingmanagementsystem.mappers;

import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.oms.orderingmanagementsystem.dtos.response.OrderItemResponse;
import org.oms.orderingmanagementsystem.dtos.response.OrderResponse;
import org.oms.orderingmanagementsystem.entities.Order;
import org.oms.orderingmanagementsystem.entities.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "subtotal", expression = "java(calculateSubtotal(order))")
    OrderResponse toResponse(Order order);

    @Mapping(target = "productName", source = "product.name")
    OrderItemResponse toItemResponse(OrderItem item);

    List<OrderItemResponse> toItemResponses(List<OrderItem> items);

    List<OrderResponse> toListResponse(List<Order> orders);

    default Page<OrderResponse> toPageResponse(Page<Order> orders) {
        return orders.map(this::toResponse);
    }

    default BigDecimal calculateSubtotal(Order order) {
        return order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
