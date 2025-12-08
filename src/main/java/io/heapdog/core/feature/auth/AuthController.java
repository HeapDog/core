package io.heapdog.core.feature.auth;

import com.nimbusds.jose.JOSEException;
import io.heapdog.core.feature.user.HeapDogUserService;
import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.security.SecurityUser;
import io.heapdog.core.shared.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final HeapDogUserService service;
    private final JwtAuthenticationService jwtService;

    @PostMapping("/signin")
    ResponseEntity<ApiResponse<SigninResponseDto>> signin(@Valid @RequestBody SigninRequestDto dto) throws JOSEException {
        SigninResponseDto res = jwtService.authenticate(dto);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<SigninResponseDto>builder()
                        .timestamp(Instant.now())
                        .data(res)
                        .path("/auth/signin")
                        .build()
        );
    }

    @PostMapping("/reset")
    public ResponseEntity<PasswordResetResponseDto> requestPasswordReset(
            @Valid @RequestBody PasswordResetRequestDto dto) {
        PasswordResetResponseDto res = service.generatePasswordResetOtp(dto);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PatchMapping("/reset/verify")
    public ResponseEntity<PasswordResetResponseDto> verifyOtpAndResetPassword(
            @Valid @RequestBody PasswordResetVerifyRequestDto dto) {
        PasswordResetResponseDto res = service.verifyOtpAndResetPassword(dto);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/whoami")
    public ResponseEntity<ApiResponse<WhoamiResponseDto>> whoami(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        HeapDogUser user = securityUser.getUser();
        WhoamiResponseDto res = WhoamiResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .currentOrganizationId(
                        user.getCurrentMembership() != null ?
                                user.getCurrentMembership().getOrganization().getId() : null
                )
                .organizations(user.getMemberships().stream().map(membership ->
                        WhoamiResponseDto.Organization.builder()
                                .id(membership.getOrganization().getId())
                                .orgName(membership.getOrganization().getOrgName())
                                .slug(membership.getOrganization().getSlug())
                                .role(membership.getRole())
                                .membershipId(membership.getId())
                                .build()
                ).toList())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<WhoamiResponseDto>builder()
                        .timestamp(Instant.now())
                        .data(res)
                        .path("/auth/whoami")
                        .build()
        );
    }
}
