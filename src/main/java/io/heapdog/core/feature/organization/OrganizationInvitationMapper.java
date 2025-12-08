package io.heapdog.core.feature.organization;

import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.feature.user.HeapDogUserRepository;
import io.heapdog.core.shared.ResourceNotFoundException;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Setter(onMethod_ = @Autowired)
@Mapper(componentModel = "spring")
public abstract class OrganizationInvitationMapper {
    protected HeapDogUserRepository heapDogUserRepository;
    protected OrganizationRepository organizationRepository;

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "accepted", ignore = true)
    @Mapping(target = "user", source = "userEmail", qualifiedByName = "emailToUser")
    @Mapping(target = "organization", source = "organizationSlug", qualifiedByName = "organizationSlugToOrganization")
    public abstract OrganizationInvitation toEntity(OrganizationInvitationCreateRequestDto dto);

    @Mapping(target = "organizationId", source = "organization.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "invitationCode", source = "code")
    public abstract OrganizationInvitationCreateResponseDto toDto(OrganizationInvitation invitation);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "invitationDate", source = "createdAt")
    public abstract OrganizationInvitationResponseDto toOrganizationInvitationResponseDto(OrganizationInvitation invitation);

    public abstract OrganizationInvitationRevokeResponseDto toOrganizationInvitationRevokeResponseDto(OrganizationInvitation invitation);

    @Named("emailToUser")
    protected HeapDogUser emailToUser(String email) {
        return heapDogUserRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Named("organizationSlugToOrganization")
    protected Organization organizationSlugToOrganization(String slug) {
        return organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "id", slug));
    }

    @Mapping(target = "organizationName", source = "organization.orgName")
    @Mapping(target = "invitedEmail", source = "user.email")
    public abstract OrganizationInvitationAcceptInfoResponseDto toOrganizationInvitationAcceptInfoResponseDto(OrganizationInvitation invitation);

    @Mapping(target = "membershipId", source = "id")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "organization.name", source = "organization.orgName")
    @Mapping(target = "organization.slug", source = "organization.slug")
    @Mapping(target = "user.username", source = "user.username")
    @Mapping(target = "user.email", source = "user.email")
    public abstract OrganizationInvitationAcceptResponseDto toOrganizationInvitationAcceptResponseDto(Membership membership);
}
