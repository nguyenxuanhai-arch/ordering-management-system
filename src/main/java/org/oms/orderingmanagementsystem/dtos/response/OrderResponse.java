package org.oms.orderingmanagementsystem.dtos.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String status;
    private String username;
    private LocalDateTime createdAt;
}
