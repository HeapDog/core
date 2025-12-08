package io.heapdog.core.feature.organization;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "orgName", source = "name")
    @Mapping(target = "memberships", ignore = true)
    Organization toEntity(OrganizationBasicInfoCreateRequestDto dto);

    @Mapping(target = "name", source = "organization.orgName")
    @Mapping(target = "slug", source = "organization.slug")
    @Mapping(target = "description", source = "organization.description")
    @Mapping(target = "email", source = "organization.email")
    @Mapping(target = "website", source = "organization.website")
    @Mapping(target = "address", source = "organization.address")
    @Mapping(target = "phone", source = "organization.phone")
    @Mapping(target = "id", source = "organization.id")
    @Mapping(target = "createdAt", source = "organization.createdAt")
    @Mapping(target = "membershipId", source = "membership.id")
    OrganizationBasicInfoWithMembershipIdResponseDto toBasicInfoWithMembershipIdDto(Organization organization, Membership membership);

    @Mapping(target = "name", source = "orgName")
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "website", source = "website")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    OrganizationBasicInfoResponseDto toBasicInfoDto(Organization organization);
}
