package org.oms.orderingmanagementsystem.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.dtos.response.DashboardResponse;
import org.oms.orderingmanagementsystem.dtos.response.OrderResponse;
import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.oms.orderingmanagementsystem.services.impls.UserService;
import org.oms.orderingmanagementsystem.services.interfaces.DashboardServiceInterface;
import org.oms.orderingmanagementsystem.services.interfaces.OrderServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final DashboardServiceInterface dashboardService;
    private final UserService userService;
    private final OrderServiceInterface orderService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        DashboardResponse stats = dashboardService.getDashboardStatistics();

        model.addAttribute("dashboard", stats);
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activePage", "dashboard");

        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/orders")
    public String orders(HttpServletRequest request, Model model) {
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        params.putIfAbsent("page", new String[]{"1"});

        Page<OrderResponse> orderList = orderService.pagination(params);

        model.addAttribute("orders", orderList);
        model.addAttribute("pageTitle", "Orders");
        model.addAttribute("activePage", "orders");
        return "orders";
    }

    @GetMapping("/users")
    public String users(HttpServletRequest request, Model model) {
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        params.putIfAbsent("page", new String[]{"1"});

        Page<UserResponse> userList = userService.pagination(params);

        model.addAttribute("users", userList);
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("activePage", "users");

        return "users";
    }
}