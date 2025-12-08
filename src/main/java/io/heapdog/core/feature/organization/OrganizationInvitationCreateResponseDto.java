package io.heapdog.core.feature.organization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationInvitationCreateResponseDto {
    private Long id;
    private Long organizationId;
    private String email;
    private String invitationCode;
}
