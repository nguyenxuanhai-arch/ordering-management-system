package org.oms.orderingmanagementsystem.services.interfaces;

import org.oms.orderingmanagementsystem.entities.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface OrderServiceInterface {
    Page<Order> pagination(Map<String, String[]> params);
}
