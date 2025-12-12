package io.heapdog.core.security;

import io.heapdog.core.feature.serviceuser.ServiceUser;
import io.heapdog.core.feature.serviceuser.ServiceUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final ServiceUserRepository repository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiKeyAuthenticationToken token = (ApiKeyAuthenticationToken) authentication;
        String apiKey = token.getCredentials().toString();
        Optional<ServiceUser> user = repository.findByApiKey(apiKey);
        if (user.isPresent()) {
            ServiceUser serviceUser = user.get();

            // Update last accessed time
            serviceUser.setLastAccessedAt(java.time.Instant.now());
            repository.save(serviceUser);

            if (!serviceUser.isEnabled()) {
                throw new BadCredentialsException("API Key is disabled");
            }
            return ApiKeyAuthenticationToken.authenticated(
                    serviceUser,
                    serviceUser.getPermissions().stream()
                            .map(permission -> (GrantedAuthority) permission::name)
                            .toList()
            );
        } else {
            throw new BadCredentialsException("Invalid API Key");
        }
//        return repository.findByApiKey(apiKey)
//                .map(serviceUser -> ApiKeyAuthenticationToken.authenticated(
//                        serviceUser,
//                        serviceUser.getPermissions().stream()
//                                .map(permission -> (GrantedAuthority) permission::name)
//                                .toList()
//                ))
//                .orElseThrow(() -> new BadCredentialsException("Invalid API Key"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return  ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
