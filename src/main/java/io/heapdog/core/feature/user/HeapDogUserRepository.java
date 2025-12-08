package io.heapdog.core.feature.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HeapDogUserRepository extends JpaRepository<HeapDogUser, Long> {
    @Query("""
            SELECT u FROM HeapDogUser u
            LEFT JOIN FETCH u.currentMembership cm
            LEFT JOIN FETCH u.memberships m
            LEFT JOIN FETCH m.organization o
            WHERE u.username = ?1
            """)
    Optional<HeapDogUser> findByUsername(String username);

    Optional<HeapDogUser> findByEmail(String email);
}
