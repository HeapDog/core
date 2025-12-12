package io.heapdog.core.feature.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
public class InternalNotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{id}")
    InternalNotificationResponseDto
    getNotifications(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

}
