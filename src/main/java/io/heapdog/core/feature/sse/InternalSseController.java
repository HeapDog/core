package io.heapdog.core.feature.sse;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/sse-token")
@RequiredArgsConstructor
public class InternalSseController {

    private final SseTokenRegistry sseTokenRegistry;

    @GetMapping("/validate")
    InternalSseValidateTokenResponseDto validateSseToken(@RequestParam String token) {

        Long userId = sseTokenRegistry.getUserIdByToken(token);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid SSE token");
        }
        return InternalSseValidateTokenResponseDto.builder()
                .userId(userId)
                .build();
    }

    @DeleteMapping("/invalidate")
    void invalidateSseToken(@RequestParam String token) {
        sseTokenRegistry.invalidateToken(token);
    }
}
