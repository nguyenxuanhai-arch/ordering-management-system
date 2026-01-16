package org.oms.orderingmanagementsystem.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.dtos.response.UserResponse;
import org.oms.orderingmanagementsystem.services.impls.UserService;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Slice<UserResponse>> getUsers(HttpServletRequest request) {
        var params = request.getParameterMap();
        Slice<UserResponse> userList = userService.pagination(params);
        return ResponseEntity.ok(userList);
    }
}
