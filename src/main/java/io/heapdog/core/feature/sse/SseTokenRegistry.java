package io.heapdog.core.feature.sse;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SseTokenRegistry {

    private final Map<String, Long> tokenToUserId = new ConcurrentHashMap<>();


    public String createToken(Long userId) {
        String token = UUID.randomUUID().toString();
        tokenToUserId.put(token, userId);
        return token;
    }

    @PreAuthorize("hasAuthority('read:sse_token')")
    public Long getUserIdByToken(String token) {
        return tokenToUserId.get(token);
    }

    public void invalidateToken(String token) {
        log.debug("Invalidating SSE token: {}", token);
        tokenToUserId.remove(token);
    }

}
