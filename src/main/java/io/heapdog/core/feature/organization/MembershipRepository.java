package io.heapdog.core.feature.organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    @Query("""
                SELECT m FROM Membership m
                WHERE m.organization.slug = :slug AND m.user.id = :userId
            """)
    Optional<Membership> findByOrganizationSlugAndUserId(String slug, Long userId);



    Page<Membership> findMembershipsByOrganizationId(Long organizationId, Pageable pageable);


    @Query(value = """
                SELECT new io.heapdog.core.feature.organization.OrganizationMemberResponseDto(
                    m.user.id,
                    m.user.username,
                    m.user.email,
                    m.role
                )
                FROM Membership m
                WHERE m.organization.id = :organizationId
            """, countQuery = """
                SELECT COUNT(m) FROM Membership m
                WHERE m.organization.id = :organizationId
            """)
    Page<OrganizationMemberResponseDto> findByOrganizationId(Long organizationId, Pageable pageable);
}
