package io.heapdog.core.feature.organization;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationSwitchRequestDto {
    @NotNull(message = "Membership ID cannot be null")
    private Long membershipId;
}
