package io.heapdog.core.feature.sse;


import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sse-token")
@RequiredArgsConstructor
public class SseController {

    private final SseTokenRegistry sseTokenRegistry;

    @GetMapping("/obtain")
    SseToken obtainToken(Authentication authentication) {
        HeapDogUser user = ((SecurityUser) authentication.getPrincipal()).getUser();
        String token = sseTokenRegistry.createToken(user.getId());
        return SseToken.builder()
                .token(token)
                .build();
    }

}
