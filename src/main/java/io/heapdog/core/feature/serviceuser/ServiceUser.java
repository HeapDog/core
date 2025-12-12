package io.heapdog.core.feature.serviceuser;


import io.heapdog.core.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Builder
@Entity
@Table(name = "heapdog_service_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceUser extends BaseEntity {

    private String name;
    private String apiKey;
    private boolean enabled;

    @ElementCollection(targetClass = ServiceUserPermission.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "heapdog_service_user_permission",
            joinColumns = @JoinColumn(name = "service_user_id")
    )
    @Column(name = "permission")
    @Enumerated(EnumType.STRING)
    private Set<ServiceUserPermission> permissions;

    @Column(name = "last_accessed_at")
    private Instant lastAccessedAt;
}
