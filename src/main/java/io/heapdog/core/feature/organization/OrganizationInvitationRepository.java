package io.heapdog.core.feature.organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrganizationInvitationRepository extends JpaRepository<OrganizationInvitation, Long> {

//    @Query("""
//                SELECT oi, u.username, u.email, oi.code, oi.accepted, oi.createdAt
//                FROM OrganizationInvitation oi
//                JOIN oi.user u
//                WHERE oi.organization.id = :organizationId
//            """)
//    List<OrganizationInvitation> findOrganizationInvitationsByOrganizationId(Long organizationId);

//    @Query(value = """
//            SELECT oi
//            FROM OrganizationInvitation oi
//            LEFT JOIN FETCH oi.user u
//            WHERE oi.organization.id = :organizationId
//            """, countQuery = """
//            SELECT COUNT(oi)
//            FROM OrganizationInvitation oi
//            WHERE oi.organization.id = :organizationId
//            """)
//    Page<OrganizationInvitation> findOrganizationInvitationByOrganizationId(Long organizationId, Pageable pageable);

    @Query(value = """
            SELECT new io.heapdog.core.feature.organization.OrganizationInvitationResponseDto(
                oi.id,
                u.username,
                u.email,
                oi.code,
                oi.accepted,
                oi.revoked,
                oi.createdAt
            )
            FROM OrganizationInvitation oi
            JOIN oi.user u
            WHERE oi.organization.id = :organizationId
            """, countQuery = """
            SELECT COUNT(oi)
            FROM OrganizationInvitation oi
            WHERE oi.organization.id = :organizationId
            """)
    Page<OrganizationInvitationResponseDto> findOrganizationInvitationsByOrganizationId(long organizationId, Pageable pageable);


    @Query("""
            SELECT oi
            FROM OrganizationInvitation oi
            LEFT JOIN FETCH oi.organization
            WHERE oi.id = :id
            """)
    Optional<OrganizationInvitation> findOrganizationInvitationById(Long id);

    boolean existsByOrganizationIdAndUserId(Long organizationId, Long userId);

    Optional<OrganizationInvitation> findByOrganizationSlugAndCode(String slug, String code);
}
