package io.heapdog.core.feature.auth;

import io.heapdog.core.feature.notification.NotificationResponseDto;
import io.heapdog.core.feature.organization.OrganizationRole;
import io.heapdog.core.feature.user.HeapDogUser;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WhoamiResponseDto {
    private Long id;
    private String username;
    private String email;
    private HeapDogUser.Role role;
    private Long currentOrganizationId;
    private List<Organization> organizations;
    private List<NotificationResponseDto> notifications;

    @Data
    @Builder
    static class Organization {
        private Long id;
        private String orgName;
        private String slug;
        private OrganizationRole role;
        private Long membershipId;
    }
}
