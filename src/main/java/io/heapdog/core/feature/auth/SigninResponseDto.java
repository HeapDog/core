package io.heapdog.core.feature.auth;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninResponseDto {

    private String token;
}
