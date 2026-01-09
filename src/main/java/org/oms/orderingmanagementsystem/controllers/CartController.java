package org.oms.orderingmanagementsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.services.interfaces.CartServiceInterface;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartServiceInterface cartService;
}
