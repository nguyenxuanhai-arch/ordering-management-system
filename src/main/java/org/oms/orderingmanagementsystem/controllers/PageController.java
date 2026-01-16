package org.oms.orderingmanagementsystem.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.dtos.response.DashboardResponse;
import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.oms.orderingmanagementsystem.services.impls.UserService;
import org.oms.orderingmanagementsystem.services.interfaces.DashboardServiceInterface;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor // Tự động kết nối với DashboardService
public class PageController {

    // Khai báo Service để lấy dữ liệu cho Task 1
    private final DashboardServiceInterface dashboardService;
    private final UserService userService;
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        // 1. Lấy dữ liệu thực tế từ Database thông qua Service
        DashboardResponse stats = dashboardService.getDashboardStatistics();

        // 2. Truyền dữ liệu sang file dashboard.html
        model.addAttribute("dashboard", stats);

        // 3. Giữ nguyên các thông tin tiêu đề trang của bạn
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activePage", "dashboard");

        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("pageTitle", "Orders");
        model.addAttribute("activePage", "orders");
        return "orders";
    }

    @GetMapping("/users")
    public String users(HttpServletRequest request, Model model) {
        Map<String, String[]> params = new HashMap<>(request.getParameterMap());
        params.putIfAbsent("page", new String[]{"1"});

        Slice<UserResponse> userList = userService.pagination(params);

        model.addAttribute("users", userList);
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("activePage", "users");

        return "users";
    }



}