package io.heapdog.core.feature.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@Builder
public class InternalNotificationResponseDto {
    private Long id;
    private String message;
    private String link;
    private boolean read;
    private boolean clicked;
    private NotificationType type;
    private Instant createdAt;
    private Long userId;
}
