package io.heapdog.core.feature.organization;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrganizationInvitationRevokeResponseDto {
    private Long id;
    private Boolean accepted;
    private Boolean revoked;
}
