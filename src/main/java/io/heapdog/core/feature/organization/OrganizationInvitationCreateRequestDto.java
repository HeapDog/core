package io.heapdog.core.feature.organization;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationInvitationCreateRequestDto {
    @NotNull
    private String organizationSlug;
    @NotBlank
    private String userEmail;
}
