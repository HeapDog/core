package io.heapdog.core.feature.organization;


import io.heapdog.core.feature.notification.Notification;
import io.heapdog.core.feature.notification.NotificationRepository;
import io.heapdog.core.feature.notification.NotificationType;
import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.feature.user.HeapDogUserRepository;
import io.heapdog.core.shared.ConstraintToFieldMapper;
import io.heapdog.core.shared.DuplicateResourceException;
import io.heapdog.core.shared.ResourceNotFoundException;
import io.heapdog.core.shared.util.OtpGenerator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;
    private final MembershipRepository membershipRepository;
    private final HeapDogUserRepository userRepository;
    private final OrganizationMapper mapper;
    private final ConstraintToFieldMapper constraintToFieldMapper;
    private final OrganizationInvitationRepository organizationInvitationRepository;
    private final OrganizationInvitationMapper organizationInvitationMapper;

    private final NotificationRepository notificationRepository;

    OrganizationBasicInfoResponseDto getBasicInfo(String slug) {
        Organization organization = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "slug", slug));
        return mapper.toBasicInfoDto(organization);
    }

    @Transactional
    OrganizationBasicInfoWithMembershipIdResponseDto createOrganization(OrganizationBasicInfoCreateRequestDto dto) {
        try {
            Organization organization = mapper.toEntity(dto);
            Organization savedOrganization = repository.save(organization);

            Membership membership = Membership.builder()
                    .user(organization.getCreatedBy())
                    .organization(savedOrganization)
                    .role(OrganizationRole.ADMIN)
                    .build();
            membershipRepository.save(membership);

            // Check if the user's currentMembership is set, if not, set it to the newly created organization
            var user = organization.getCreatedBy();
            if (user.getCurrentMembership() == null) {
                user.setCurrentMembership(membership);
                userRepository.save(user);
            }
            return mapper.toBasicInfoWithMembershipIdDto(organization, membership);
        } catch (DataIntegrityViolationException ex) {
            Throwable root = ex.getCause();
            if (root instanceof ConstraintViolationException cve) {
                String constraintName = cve.getConstraintName();
                String field = constraintToFieldMapper.map(constraintName);
                throw new DuplicateResourceException(field, "Organization with the same " + field + " already exists.");
            } else {
                throw ex;
            }
        }
    }

    Page<OrganizationMemberResponseDto>
    getOrganizationWithMembers(String slug, Long page, Long size) {
        Organization organization = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "slug", slug));
//        return membershipRepository.findByOrganizationId(organization.getId(), PageRequest.of(page.intValue() - 1, size.intValue(), Sort.by("createdAt").ascending()));
        Page<Membership> memberships = membershipRepository.findMembershipsByOrganizationId(
                organization.getId(),
                PageRequest.of(page.intValue() - 1, size.intValue(), Sort.by("createdAt").ascending())
        );
        return memberships.map(m -> {
            var user = m.getUser();
            return OrganizationMemberResponseDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(m.getRole())
                    .membershipId(m.getId())
                    .build();
        });
    }

    @Transactional
    public OrganizationSwitchResponseDto switchOrganization(OrganizationSwitchRequestDto dto, Long requestingUserId) {
        Membership membership = membershipRepository.findById(dto.getMembershipId())
                .orElseThrow(() -> new ResourceNotFoundException("Membership", "id", dto.getMembershipId().toString()));
        var user = membership.getUser();
        // Check if the membership belongs to the requesting user
        if (!user.getId().equals(requestingUserId)) {
            throw new ResourceNotFoundException("Membership", "id", dto.getMembershipId().toString());
        }
        user.setCurrentMembership(membership);
        userRepository.save(user);
        return OrganizationSwitchResponseDto.builder()
                .message("Switched current organization successfully.")
                .build();
    }

    OrganizationBasicInfoResponseDto updateOrganization(String slug, OrganizationBasicInfoUpdateRequestDto dto) {
        Organization organization = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "slug", slug));
        organization.setOrgName(dto.getName() != null ? dto.getName() : organization.getOrgName());
        organization.setSlug(dto.getSlug() != null ? dto.getSlug() : organization.getSlug());
        organization.setDescription(dto.getDescription() != null ? dto.getDescription() : organization.getDescription());
        organization.setEmail(dto.getEmail() != null ? dto.getEmail() : organization.getEmail());
        organization.setWebsite(dto.getWebsite() != null ? dto.getWebsite() : organization.getWebsite());
        organization.setAddress(dto.getAddress() != null ? dto.getAddress() : organization.getAddress());
        organization.setPhone(dto.getPhone() != null ? dto.getPhone() : organization.getPhone());
        try {
            Organization updatedOrganization = repository.save(organization);
            // send notification to all members about the update
            var memberships = membershipRepository.findByOrganizationId(organization.getId());
            for (var membership : memberships) {
                Notification notification = Notification.builder()
                        .message("The organization " + organization.getOrgName() + " has been updated.")
                        .link("/organizations/" + organization.getSlug() + "/basic-info")
                        .recipient(membership.getUser())
                        .type(NotificationType.ORGANIZATION_UPDATED)
                        .build();
                Notification savedNotification = notificationRepository.save(notification);
                natsPublisher.publishNotificationEvent(savedNotification.getId());
            }
            return mapper.toBasicInfoDto(updatedOrganization);
        } catch (DataIntegrityViolationException ex) {
            Throwable root = ex.getCause();
            if (root instanceof ConstraintViolationException cve) {
                String constraintName = cve.getConstraintName();
                String field = constraintToFieldMapper.map(constraintName);
                throw new DuplicateResourceException(field, "Organization with the same " + field + " already exists.");
            } else {
                throw ex;
            }
        }
    }

    OrganizationSlugCheckResponseDto checkSlugAvailability(String slug) {
        boolean exists = repository.existsBySlug(slug);
        return OrganizationSlugCheckResponseDto.builder()
                .slug(slug)
                .isAvailable(!exists)
                .build();
    }

    @Transactional
    OrganizationInvitationCreateResponseDto
    createInvitation(OrganizationInvitationCreateRequestDto requestDto) {
        OrganizationInvitation invitation =
                organizationInvitationMapper.toEntity(requestDto);
        invitation.setCode(OtpGenerator.generateOtp());
        // check if already member
        HeapDogUser user = invitation.getUser();
        Organization organization = invitation.getOrganization();
        Membership existingMembership = membershipRepository
                .findByOrganizationSlugAndUserId(organization.getSlug(), user.getId())
                .orElse(null);
        if (existingMembership != null) {
            throw new IllegalArgumentException("User is already a member of the organization.");
        }
        if (organizationInvitationRepository.existsByOrganizationIdAndUserId(
                organization.getId(), user.getId())) {
            throw new IllegalArgumentException("An invitation has already been sent to this user for the organization.");
        }
        OrganizationInvitation savedInvitation =
                organizationInvitationRepository.save(invitation);

        Notification notification = Notification.builder()
                .message("You have been invited to join the organization: " + organization.getOrgName())
                .link("/invitations/accept?code=" + savedInvitation.getCode() + "&org=" + organization.getSlug())
                .recipient(user)
                .type(NotificationType.INVITATION)
                .build();
        notificationRepository.save(notification);

        return organizationInvitationMapper.toDto(savedInvitation);
    }

    OrganizationMembershipStatusResponseDto
    getMembershipStatus(String slug, String email) {
        Organization organization = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "slug", slug));
        HeapDogUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        Membership membership = membershipRepository.findByOrganizationSlugAndUserId(slug, user.getId())
                .orElse(null);
        boolean isMember = membership != null;

        var u = OrganizationMembershipStatusResponseDto.User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
        var o = OrganizationMembershipStatusResponseDto.Organization.builder()
                .id(organization.getId())
                .slug(organization.getSlug())
                .name(organization.getOrgName())
                .build();

        return OrganizationMembershipStatusResponseDto.builder()
                .user(u)
                .organization(o)
                .member(isMember)
                .invited(organizationInvitationRepository.existsByOrganizationIdAndUserId(organization.getId(), user.getId()))
                .memberSince(isMember ? membership.getCreatedAt() : null)
                .build();
    }

    Page<OrganizationInvitationResponseDto> getOrganizationInvitations(String slug, Long page, Long size) {
        Organization organization = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "slug", slug));
        Pageable pageable = PageRequest.of(page.intValue() - 1, size.intValue(), Sort.by("createdAt").ascending());
        log.info("Getting organization invitations for slug {}", slug);
//        var invitations = organizationInvitationRepository.findOrganizationInvitationByOrganizationId(organization.getId(), pageable);
//        var i = invitations.stream().map(organizationInvitationMapper::toOrganizationInvitationResponseDto)
//                .toList();
//        return  OrganizationInvitationsResponseDto.builder()
//                .invitations(i)
//                .build();
//        return invitations.map(organizationInvitationMapper::toOrganizationInvitationResponseDto);
        return organizationInvitationRepository.findOrganizationInvitationsByOrganizationId(organization.getId(), pageable);
    }


    OrganizationInvitationRevokeResponseDto
    revokeInvitation(String slug, Long invitationId) {
        OrganizationInvitation invitation = organizationInvitationRepository.findOrganizationInvitationById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation", "id", invitationId.toString()));
        if (!invitation.getOrganization().getSlug().equals(slug)) {
            throw new ResourceNotFoundException("Invitation", "id", invitationId.toString());
        }

        if (invitation.isAccepted()) {
            throw new IllegalArgumentException("Cannot revoke an accepted invitation.");
        }
        invitation.setRevoked(true);
        OrganizationInvitation saved = organizationInvitationRepository.save(invitation);
        return organizationInvitationMapper.toOrganizationInvitationRevokeResponseDto(saved);
    }

    @Transactional
    OrganizationInvitationAcceptResponseDto acceptInvitation(String slug,
                                                             OrganizationInvitationAcceptRequestDto dto,
                                                             Long id) {
        OrganizationInvitation invitation = organizationInvitationRepository
                .findByOrganizationSlugAndCode(slug, dto.getCode())
                .orElseThrow(() -> new ResourceNotFoundException("Invitation", "code", dto.getCode()));
        if (invitation.getRevoked()) {
            throw new IllegalArgumentException("This invitation has been revoked.");
        }
        if (invitation.isAccepted()) {
            throw new IllegalArgumentException("This invitation has already been accepted.");
        }
        if (!invitation.getUser().getId().equals(id)) {
            throw new IllegalArgumentException("This invitation is not for the current user.");
        }
        // Create membership
        Membership membership = Membership.builder()
                .user(invitation.getUser())
                .organization(invitation.getOrganization())
                .role(OrganizationRole.MEMBER)
                .build();
        membershipRepository.save(membership);
        // Mark invitation as accepted
        invitation.setAccepted(true);
        organizationInvitationRepository.save(invitation);
        // Update user's current membership
        var user = invitation.getUser();
        if (user.getCurrentMembership() == null) {
            user.setCurrentMembership(membership);
            userRepository.save(user);
        }
        return organizationInvitationMapper.toOrganizationInvitationAcceptResponseDto(membership);
    }

    public OrganizationInvitationAcceptInfoResponseDto getInvitationAcceptInfo(String slug,
                                                                               @NotNull @Size(max = 6) String code,
                                                                               Long userId) {
        OrganizationInvitation invitation = organizationInvitationRepository
                .findByOrganizationSlugAndCode(slug, code)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation", "code", code));
        if (invitation.getRevoked()) {
            throw new IllegalArgumentException("This invitation has been revoked.");
        }
        if (invitation.isAccepted()) {
            throw new IllegalArgumentException("This invitation has already been accepted.");
        }
        if (!invitation.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("This invitation is not for the current user.");
        }
        return organizationInvitationMapper.toOrganizationInvitationAcceptInfoResponseDto(invitation);
    }

    public OrganizationMemberResponseDto updateOrganizationMemberRole(String slug, Long membershipId, @Valid OrganizationMemberRoleUpdateRequestDto dto) {
        Organization organization = repository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Organization", "slug", slug));
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new ResourceNotFoundException("Membership", "id", membershipId.toString()));
        if (!membership.getOrganization().getId().equals(organization.getId())) {
            throw new ResourceNotFoundException("Membership", "id", membershipId.toString());
        }
        membership.setRole(dto.getRole());
        Membership savedMembership = membershipRepository.save(membership);

        // create notification to the user about role change
        Notification notification = Notification.builder()
                .message("Your role in the organization " + organization.getOrgName() + " has been changed to " + dto.getRole())
                .link("/organizations/" + organization.getSlug() + "/members")
                .recipient(savedMembership.getUser())
                .type(NotificationType.ORGANIZATION_MEMBER_ROLE_UPDATED)
                .build();
        Notification savedNotification = notificationRepository.save(notification);
        natsPublisher.publishNotificationEvent(savedNotification.getId());

        var user = savedMembership.getUser();
        return OrganizationMemberResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(savedMembership.getRole())
                .membershipId(savedMembership.getId())
                .build();
    }
}
