package io.heapdog.core.feature.serviceuser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceUserRepository extends JpaRepository<ServiceUser, Long> {

    Optional<ServiceUser> findByApiKey(String apiKey);

}
