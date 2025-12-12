package io.heapdog.core.feature.serviceuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ServiceUserRepository extends JpaRepository<ServiceUser, Long> {

    Optional<ServiceUser> findByApiKey(String apiKey);

    @Query("""
              SELECT DISTINCT s
              FROM ServiceUser s
              LEFT JOIN FETCH s.permissions
              LEFT JOIN FETCH s.createdBy
              WHERE s.id = :id
            """)
    Optional<ServiceUser> findByIdWithPermissionsAndCreator(Long id);

}
