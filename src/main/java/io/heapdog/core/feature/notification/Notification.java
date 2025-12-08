package io.heapdog.core.feature.notification;


import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.shared.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "notification")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private HeapDogUser recipient;

    @Column(name = "message", nullable = false)
    @Size(min = 1, max = 200)
    private String message;

    @Column(name = "link")
    private String link;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private boolean read = false;

    @Column(name = "is_clicked", nullable = false)
    @Builder.Default
    private boolean clicked = false;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;
}
