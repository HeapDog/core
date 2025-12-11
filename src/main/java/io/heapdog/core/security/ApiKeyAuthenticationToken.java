package io.heapdog.core.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final String apiKey;
    private final Object principal;

    public ApiKeyAuthenticationToken(Object principal, String apiKey, boolean authenticated, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.apiKey = apiKey;
        setAuthenticated(authenticated);
    }

    public static ApiKeyAuthenticationToken authenticated(Object principal, Collection<? extends GrantedAuthority> authorities) {
        return new ApiKeyAuthenticationToken(principal, null, true, authorities);
    }

    public static ApiKeyAuthenticationToken unauthenticated(String apiKey) {
        return new ApiKeyAuthenticationToken(null, apiKey, false, null);
    }

    @Override
    public Object getCredentials() {
        return this.apiKey;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
