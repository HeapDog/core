package io.heapdog.core.feature.user;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class HeapdogUserController {

    private final HeapDogUserService service;

    @PostMapping("/signup")
    ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto dto) {
        SignupResponseDto res = service.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/verify-email")
    ResponseEntity<EmailVerificationResponseDto> verifyEmail(@Valid @RequestBody EmailVerificationRequestDto dto) {
        EmailVerificationResponseDto res = service.verifyEmailOtp(dto);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
