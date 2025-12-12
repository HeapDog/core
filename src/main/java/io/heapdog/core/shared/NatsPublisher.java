package io.heapdog.core.shared;


import io.nats.client.Connection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class NatsPublisher {

    private final Connection natsConnection;

    @Value("${nats.notificationSubject}")
    private String NOTIFICATION_SUBJECT;


    private void publish(String subject, String message) {
        natsConnection.publish(subject, message.getBytes(StandardCharsets.UTF_8));
    }

    public void publishNotificationEvent(Long notificationId) {
        log.info("Publishing notification event for ID: {} in subject: {}", notificationId, NOTIFICATION_SUBJECT);
        publish(NOTIFICATION_SUBJECT, notificationId.toString());
    }
}
