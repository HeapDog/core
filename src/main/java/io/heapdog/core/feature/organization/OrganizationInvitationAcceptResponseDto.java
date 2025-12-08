package io.heapdog.core.feature.organization;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrganizationInvitationAcceptResponseDto {
    private Long membershipId;
    private OrganizationRole role;
    private Organization organization;
    private User user;

    @Builder
    @Data
    static class Organization {
        private String name;
        private String slug;
    }


    @Builder
    @Data
    static class User {
        private String username;
        private String email;
    }
}
