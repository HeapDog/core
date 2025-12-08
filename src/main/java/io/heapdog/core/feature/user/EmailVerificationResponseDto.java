package io.heapdog.core.feature.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVerificationResponseDto {

    private String message;

}
