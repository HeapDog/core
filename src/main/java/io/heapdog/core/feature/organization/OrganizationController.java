package io.heapdog.core.feature.organization;

import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.security.SecurityUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/{slug}/basic-info")
    OrganizationBasicInfoResponseDto
    getOrganizationBasicInfo(@PathVariable String slug) {
        return organizationService.getBasicInfo(slug);
    }

    @PostMapping
    OrganizationBasicInfoWithMembershipIdResponseDto
    createOrganization(@Valid @RequestBody OrganizationBasicInfoCreateRequestDto dto) {
        return organizationService.createOrganization(dto);
    }

    @GetMapping("/{slug}/members")
    Page<OrganizationMemberResponseDto>
    getOrganizationMembers(@PathVariable String slug,
                           @RequestParam(defaultValue = "1") Long page,
                           @RequestParam(defaultValue = "1") Long size) {
        return organizationService.getOrganizationWithMembers(slug, page, size);
    }

    @PatchMapping("/switch")
    OrganizationSwitchResponseDto
    switchOrganization(@Valid @RequestBody OrganizationSwitchRequestDto dto, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser user = securityUser.getUser();
        return organizationService.switchOrganization(dto, user.getId());
    }

    @PreAuthorize("@organizationSecurity.isAdmin(#slug, authentication)")
    @PatchMapping("/{slug}/basic-info")
    OrganizationBasicInfoResponseDto
    updateOrganization(@PathVariable String slug, @Valid @RequestBody OrganizationBasicInfoUpdateRequestDto dto) {
        return organizationService.updateOrganization(slug, dto);
    }

    @GetMapping("/check-slug")
    OrganizationSlugCheckResponseDto
    checkSlugAvailability(@NotBlank(message = "Slug is required") @RequestParam String slug) {
        return organizationService.checkSlugAvailability(slug);
    }

    @PostMapping("/invite")
    @PreAuthorize("@organizationSecurity.isAdmin(#dto.getOrganizationSlug(), authentication)")
    OrganizationInvitationCreateResponseDto
    inviteMember(@Valid @RequestBody OrganizationInvitationCreateRequestDto dto) {
        return organizationService.createInvitation(dto);
    }

    @PreAuthorize("@organizationSecurity.isAdmin(#slug, authentication)")
    @GetMapping("/{slug}/membership-status")
    OrganizationMembershipStatusResponseDto
    getInviteeStatus(@PathVariable String slug, @RequestParam String email) {
        return organizationService.getMembershipStatus(slug, email);
    }

    @PreAuthorize("@organizationSecurity.isAtLeastMember(#slug, authentication)")
    @GetMapping("/{slug}/invitations")
    Page<OrganizationInvitationResponseDto>
    getOrganizationInvitations(@PathVariable String slug,
                               @RequestParam(defaultValue = "1") Long page,
                               @RequestParam(defaultValue = "10") Long size) {
        return organizationService.getOrganizationInvitations(slug, page, size);
    }

    @PatchMapping("/{slug}/invitations/{invitationId}/revoke")
    @PreAuthorize("@organizationSecurity.isAdmin(#slug, authentication)")
    OrganizationInvitationRevokeResponseDto revokeInvitation(@PathVariable String slug, @PathVariable Long invitationId) {
        return organizationService.revokeInvitation(slug, invitationId);
    }

    @GetMapping("/{slug}/invitations/accept-info")
    OrganizationInvitationAcceptInfoResponseDto getInvitationAcceptInfo(@PathVariable String slug,
                                                                        @RequestParam @NotNull @Size(max = 6) String code,
                                                                        Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser user = securityUser.getUser();
        return organizationService.getInvitationAcceptInfo(slug, code, user.getId());
    }

    @PostMapping("/{slug}/invitations/accept")
    OrganizationInvitationAcceptResponseDto acceptInvitation(@PathVariable String slug,
                                                             @Valid @RequestBody OrganizationInvitationAcceptRequestDto dto,
                                                             Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser user = securityUser.getUser();
        return organizationService.acceptInvitation(slug, dto, user.getId());
    }

    @PreAuthorize("@organizationSecurity.isAdmin(#slug, authentication)")
    @PatchMapping("/{slug}/membership/{membershipId}/role")
    OrganizationMemberResponseDto updateOrganizationMemberRole(@PathVariable String slug,
                                                             @PathVariable Long membershipId,
                                                             @Valid @RequestBody OrganizationMemberRoleUpdateRequestDto dto) {
        return organizationService.updateOrganizationMemberRole(slug, membershipId, dto);
    }

}
