package io.heapdog.core.feature.notification;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponseDto toDto(Notification notification);

}
