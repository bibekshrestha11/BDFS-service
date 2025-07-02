package com.bibek.bdfs.notification.controller;

import com.bibek.bdfs.common.BaseController;
import com.bibek.bdfs.notification.dto.NotificationResponse;
import com.bibek.bdfs.notification.service.NotificationService;
import com.bibek.bdfs.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController extends BaseController {
    private final NotificationService notificationService;

    @GetMapping("/my-notifications")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getMyNotifications(Pageable pageable) {
        return successResponse(notificationService.getNotificationsByLoginUser(pageable),
                "Fetched notifications successfully");
    }
}
