package io.heapdog.core.feature.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationStatsResponseDto {

    private Long unread;
    private Long total;

}
