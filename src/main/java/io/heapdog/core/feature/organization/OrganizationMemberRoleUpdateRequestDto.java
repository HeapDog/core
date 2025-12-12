package io.heapdog.core.feature.organization;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrganizationMemberRoleUpdateRequestDto {

    private OrganizationRole role;

}
