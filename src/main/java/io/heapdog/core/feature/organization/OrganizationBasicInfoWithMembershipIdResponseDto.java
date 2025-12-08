package io.heapdog.core.feature.organization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class OrganizationBasicInfoWithMembershipIdResponseDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String email;
    private String website;
    private String address;
    private String phone;
    private Long membershipId;
    private Instant createdAt;
}
