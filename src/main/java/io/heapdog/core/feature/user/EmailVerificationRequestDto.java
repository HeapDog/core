package io.heapdog.core.feature.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailVerificationRequestDto {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "OTP is required")
    private String otp;
}
