package org.oms.orderingmanagementsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.oms.orderingmanagementsystem.services.interfaces.NotificationServiceInterface;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServiceInterface notificationService;
}
