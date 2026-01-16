package org.oms.orderingmanagementsystem.services.interfaces;

import org.oms.orderingmanagementsystem.dtos.response.OrderResponse;
import org.oms.orderingmanagementsystem.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Map;

public interface OrderServiceInterface {
    Page<OrderResponse> pagination(Map<String, String[]> params);
}
