package io.heapdog.core.feature.organization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationMemberResponseDto {
    private Long id;
    private String username;
    private String email;
    private OrganizationRole role;
}
