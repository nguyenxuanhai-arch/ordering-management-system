package org.oms.orderingmanagementsystem.services.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.oms.orderingmanagementsystem.dtos.response.*;
import org.oms.orderingmanagementsystem.entities.Order;
import org.oms.orderingmanagementsystem.repositories.*;
import org.oms.orderingmanagementsystem.services.interfaces.DashboardServiceInterface;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService implements DashboardServiceInterface {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboardStatistics() {
        Long totalUsers = userRepository.count();
        Long totalOrders = orderRepository.count();
        Long totalProducts = 0L; // TODO: Từ ProductRepository nếu có
        Double totalRevenue = orderRepository.calculateTotalRevenue();

        List<RecentOrderResponse> recentOrders = orderRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(order -> new RecentOrderResponse(
                        order.getId(),
                        "ORD-" + order.getId(),
                        order.getUser().getName(),
                        order.getId().doubleValue(),
                        order.getStatus().toString(),
                        order.getCreatedAt()
                ))
                .collect(Collectors.toList());

        List<RecentActivityResponse> recentActivities = notificationRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(notification -> new RecentActivityResponse(
                        notification.getId(),
                        "NOTIFICATION",
                        notification.getMessage(),
                        "fas fa-bell",
                        "text-info",
                        notification.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new DashboardResponse(
                totalUsers,
                totalOrders,
                totalProducts,
                totalRevenue != null ? totalRevenue : 0.0,
                recentOrders,
                recentActivities
        );
    }
}