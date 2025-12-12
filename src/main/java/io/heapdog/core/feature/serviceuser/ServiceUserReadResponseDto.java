package io.heapdog.core.feature.serviceuser;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class ServiceUserReadResponseDto {
    private Long id;
    private String name;
    private String apiKey;
    private Boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
    private User createdBy;
    private Set<ServiceUserPermission> permissions;
    private Instant lastAccessedAt;

    @Data
    @Builder
    public static class User {
        private Long id;
        private String username;
    }
}
