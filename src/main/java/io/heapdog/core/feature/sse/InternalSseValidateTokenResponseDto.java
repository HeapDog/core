package io.heapdog.core.feature.sse;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InternalSseValidateTokenResponseDto {
    private Long userId;
}
