package org.oms.orderingmanagementsystem.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Long totalUsers;
    private Long totalOrders;
    private Long totalProducts;
    private Double totalRevenue;
    private List<RecentOrderResponse> recentOrders;
    private List<RecentActivityResponse> recentActivities;
}