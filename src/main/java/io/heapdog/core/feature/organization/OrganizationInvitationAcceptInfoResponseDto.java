package io.heapdog.core.feature.organization;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrganizationInvitationAcceptInfoResponseDto {

    private String organizationName;
    private String invitedEmail;


}
