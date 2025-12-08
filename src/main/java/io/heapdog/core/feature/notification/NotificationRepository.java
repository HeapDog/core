package io.heapdog.core.feature.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


    Page<NotificationResponseDto> findByRecipientId(Long userId, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipient.id = :userId AND n.read = false")
    Long findUnreadCountByUserId(Long userId);

    Long countByRecipientId(Long userId);

}
