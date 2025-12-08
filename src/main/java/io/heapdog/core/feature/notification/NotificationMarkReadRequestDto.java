package io.heapdog.core.feature.notification;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class NotificationMarkReadRequestDto {

    @NotNull
    private List<Long> notificationIds;

}
