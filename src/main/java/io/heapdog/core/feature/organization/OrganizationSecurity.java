package io.heapdog.core.feature.organization;


import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("organizationSecurity")
@RequiredArgsConstructor
public class OrganizationSecurity {

    private final MembershipRepository membershipRepository;

    public boolean isAdmin(String organizationSlug, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser heapDogUser = securityUser.getUser();
        return membershipRepository.findByOrganizationSlugAndUserId(organizationSlug, heapDogUser.getId())
                .map(membership -> membership.getRole() == OrganizationRole.ADMIN)
                .orElse(false);
    }

    public boolean isAtLeastStaff(String organizationSlug, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser heapDogUser = securityUser.getUser();
        return membershipRepository.findByOrganizationSlugAndUserId(organizationSlug, heapDogUser.getId())
                .map(membership -> membership.getRole() == OrganizationRole.ADMIN || membership.getRole() == OrganizationRole.STAFF)
                .orElse(false);
    }

    public boolean isAtLeastMember(String organizationSlug, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser heapDogUser = securityUser.getUser();
        return membershipRepository.findByOrganizationSlugAndUserId(organizationSlug, heapDogUser.getId())
                .isPresent();
    }

}
