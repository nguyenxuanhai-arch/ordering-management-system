package org.oms.orderingmanagementsystem.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.dtos.response.OrderResponse;
import org.oms.orderingmanagementsystem.entities.Order;
import org.oms.orderingmanagementsystem.mappers.OrderMapper;
import org.oms.orderingmanagementsystem.services.interfaces.OrderServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceInterface orderService;
    private final OrderMapper orderMapper;

    private static final int MAX_PAGE_SIZE = 100;

    @GetMapping("v1")
    ResponseEntity<Slice<OrderResponse>> getAll(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();

        // FIXED: Validate page size to prevent DoS
        if (params.containsKey("perPage")) {
            try {
                int size = Integer.parseInt(params.get("perPage")[0]);
                if (size > MAX_PAGE_SIZE) {
                    params.put("perPage", new String[]{String.valueOf(MAX_PAGE_SIZE)});
                }
            } catch (NumberFormatException e) {
                // Invalid size, let service handle with default
            }
        }

        // Service now returns OrderResponse directly (mapping done within transaction)
        Slice<OrderResponse> orderResponses = orderService.pagination(params);
        return ResponseEntity.ok(orderResponses);
    }
}
