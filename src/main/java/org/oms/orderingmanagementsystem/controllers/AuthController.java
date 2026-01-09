package org.oms.orderingmanagementsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.services.interfaces.UserServiceInterface;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceInterface userService;
}
