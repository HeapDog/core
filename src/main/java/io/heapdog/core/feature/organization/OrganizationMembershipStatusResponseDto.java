package io.heapdog.core.feature.organization;


import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class OrganizationMembershipStatusResponseDto {
    private User user;
    private Organization organization;
    private boolean member;
    private boolean invited;
    private Instant memberSince;

    @Data
    @Builder
    static class User {
        private Long id;
        private String username;
        private String email;
    }

    @Data
    @Builder
    static class Organization {
        private Long id;
        private String name;
        private String slug;
    }
}
