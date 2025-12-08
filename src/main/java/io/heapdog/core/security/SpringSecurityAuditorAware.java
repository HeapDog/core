package io.heapdog.core.security;

import io.heapdog.core.feature.user.HeapDogUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class SpringSecurityAuditorAware implements AuditorAware<HeapDogUser> {
    @Override
    public Optional<HeapDogUser> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        if (authentication.getPrincipal() instanceof SecurityUser user) {
            return Optional.ofNullable(user.getUser());
        } else {
            log.warn("Authentication principal is not of type SecurityUser: {}", authentication.getPrincipal().getClass().getName());
            return Optional.empty();
        }
    }
}
