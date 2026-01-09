package org.oms.orderingmanagementsystem.dtos.response;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private String status;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String productName;
}
