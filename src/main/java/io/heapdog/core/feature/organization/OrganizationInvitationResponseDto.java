package io.heapdog.core.feature.organization;


import io.heapdog.core.shared.MaskData;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class OrganizationInvitationResponseDto {
    private Long id;
    private String username;
    private String email;
    @MaskData(keepLeft = 1, keepRight = 1)
    private String code;
    private boolean accepted;
    private Boolean revoked;
    private Instant invitationDate;
}
