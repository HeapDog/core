package io.heapdog.core.feature.notification;


import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.security.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    Page<NotificationResponseDto>
    getNotifications(Authentication authentication,
                     @RequestParam(defaultValue = "1") Long page,
                     @RequestParam(defaultValue = "10") Long size) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser user = securityUser.getUser();
        return notificationService.getNotifications(user.getId(), page, size);
    }

    @GetMapping("/unread-count")
    NotificationStatsResponseDto
    getUnreadCount(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser user = securityUser.getUser();
        return notificationService.getUnreadCount(user.getId());
    }

    @PatchMapping("/read")
    NotificationStatsResponseDto
    markAsRead(@Valid @RequestBody NotificationMarkReadRequestDto dto,
               Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser user = securityUser.getUser();
        return notificationService.markAsRead(dto, user.getId());
    }


}
