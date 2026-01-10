package org.oms.orderingmanagementsystem.dtos.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private String productName;
    private int quantity;
    private BigDecimal priceAtOrder;
}
