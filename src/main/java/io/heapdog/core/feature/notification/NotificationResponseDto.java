package io.heapdog.core.feature.notification;


import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class NotificationResponseDto {

    private Long id;
    private String message;
    private String link;
    private boolean read;
    private boolean clicked;
    private NotificationType type;
    private Instant createdAt;

}
