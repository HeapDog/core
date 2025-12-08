package io.heapdog.core.feature.organization;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrganizationInvitationAcceptRequestDto {

    @NotNull
    private String code;
}
