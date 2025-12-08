package io.heapdog.core.feature.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query("""
                            SELECT o FROM Organization o
                            WHERE o.slug = :slug
            """)
    Optional<Organization> findBySlug(String slug);

    @Query("""
                SELECT o FROM Organization o
                LEFT JOIN FETCH o.memberships m
                LEFT JOIN FETCH m.user
                WHERE o.slug = :slug
            """)
    Optional<Organization> findWithMembersBySlug(String slug);
    boolean existsBySlug(String slug);
}
