package org.oms.orderingmanagementsystem.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentActivityResponse {
    private Long id;
    private String type;
    private String description;
    private String icon;
    private String color;
    private LocalDateTime createdAt;
}