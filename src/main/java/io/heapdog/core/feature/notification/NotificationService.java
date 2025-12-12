package io.heapdog.core.feature.notification;


import io.heapdog.core.shared.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification createNotification(String message, String link) {
        Notification notification = Notification.builder()
                .message(message)
                .link(link)
                .build();
        return notificationRepository.save(notification);
    }

    @PreAuthorize("hasAuthority('read:notification')")
    InternalNotificationResponseDto getNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId.toString()));
        return InternalNotificationResponseDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .link(notification.getLink())
                .read(notification.isRead())
                .clicked(notification.isClicked())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt())
                .userId(notification.getRecipient().getId())
                .build();
    }

    Page<NotificationResponseDto>
    getNotifications(Long userId, Long page, Long size) {
        return notificationRepository.findByRecipientId(
                userId,
                PageRequest.of(
                        Math.toIntExact(page - 1),
                        Math.toIntExact(size),
                        Sort.by(Sort.Direction.DESC, "createdAt")
                )
        );
    }

    NotificationStatsResponseDto
    getUnreadCount(Long recipientId) {
        Long unread = notificationRepository.findUnreadCountByUserId(recipientId);
        Long total = notificationRepository.countByRecipientId(recipientId);

        return NotificationStatsResponseDto.builder()
                .unread(unread)
                .total(total)
                .build();
    }

    NotificationStatsResponseDto
    markAsRead(NotificationMarkReadRequestDto dto, Long recipientId) {
        List<Notification> notificationList = notificationRepository.findAllById(dto.getNotificationIds());

        notificationList.stream()
                .filter(notification -> !notification.getRecipient().getId().equals(recipientId))
                .findAny()
                .ifPresent(notification -> {
                    throw new ResourceNotFoundException("Notification", "id", notification.getId().toString());
                });

        Set<Long> notificationIds = notificationList.stream()
                .map(Notification::getId)
                .collect(Collectors.toSet());
        for (Long id : dto.getNotificationIds()) {
            if (!notificationIds.contains(id)) {
                throw new ResourceNotFoundException("Notification", "id", id.toString());
            }
        }
        for (Notification notification : notificationList) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notificationList);

        Long unread = notificationRepository.findUnreadCountByUserId(recipientId);
        Long total = notificationRepository.countByRecipientId(recipientId);

        return NotificationStatsResponseDto.builder()
                .unread(unread)
                .total(total)
                .build();
    }

}
