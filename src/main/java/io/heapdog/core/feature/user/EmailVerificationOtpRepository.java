package io.heapdog.core.feature.user;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailVerificationOtpRepository extends JpaRepository<EmailVerificationOtp, Long> {
    @Modifying
    @Query("DELETE FROM EmailVerificationOtp o WHERE o.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM EmailVerificationOtp o JOIN FETCH o.user WHERE o.user.id = :userId")
    Optional<EmailVerificationOtp> findByUserId(@Param("userId") @NotNull Long userId);
}
