package io.heapdog.core.feature.user;

import io.heapdog.core.feature.auth.*;
import io.heapdog.core.shared.ConstraintToFieldMapper;
import io.heapdog.core.shared.DuplicateResourceException;
import io.heapdog.core.shared.util.OtpGenerator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class HeapDogUserService {
    private final HeapDogUserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final EmailVerificationOtpRepository emailVerificationOtpRepository;
    private final OtpService otpService;
    private final ConstraintToFieldMapper constraintToFieldMapper;

    public SignupResponseDto createUser(@Valid SignupRequestDto dto) {
        dto.setPassword(encoder.encode(dto.getPassword()));
        HeapDogUser user = mapper.toEntity(dto);
        try {
            HeapDogUser saved = repository.save(user);
            EmailVerificationOtp otp = EmailVerificationOtp.builder()
                    .otp(OtpGenerator.generateOtp())
                    .user(user)
                    .build();
            emailVerificationOtpRepository.save(otp);
            log.info("MOCK EMAIL: Verification OTP for user {} is: {}", user.getEmail(),
                    otp.getOtp());
            return mapper.toDto(saved);
        } catch (DataIntegrityViolationException exception) {
//            throw new DuplicateUsernameException("Username already exists: " + dto.getUsername());
            Throwable cause = exception.getCause();
            if (cause instanceof ConstraintViolationException cve) {
                String constraint = cve.getConstraintName();
                String field = constraintToFieldMapper.map(constraint);
                throw new DuplicateResourceException(field, "User with the same " + field + " already exists");
            } else {
                throw exception;
            }
        }
    }

    public PasswordResetResponseDto generatePasswordResetOtp(PasswordResetRequestDto dto) {
        // Find the user by email.
        HeapDogUser user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account associated with the provided email."));

        String otpString = otpService.generateOtp(user);

        log.info("MOCK EMAIL: OTP for user {} is: {}", user.getEmail(), otpString);

        return PasswordResetResponseDto.builder()
                .message("OTP sent successfully (mock).")
                .build();
    }

    public PasswordResetResponseDto verifyOtpAndResetPassword(PasswordResetVerifyRequestDto dto) {
        HeapDogUser user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("No account associated with the provided email."));

        PasswordResetOtp otp = passwordResetOtpRepository.findByUser(user)
                .orElseThrow(() -> new InvalidOtpException("No OTP found for this user. Please request a new one."));

        if (!otp.getOtp().equals(dto.getOtp()) || LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new InvalidOtpException("The OTP provided is incorrect or has expired.");
        }

        user.setPassword(encoder.encode(dto.getNewPassword()));
        repository.save(user);

        passwordResetOtpRepository.delete(otp);

        return PasswordResetResponseDto.builder()
                .message("Password reset successful.")
                .build();
    }

    @Transactional
    public EmailVerificationResponseDto verifyEmailOtp(EmailVerificationRequestDto dto) {
        EmailVerificationOtp otp = emailVerificationOtpRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found for the provided ID."));

        if (otp.getUser().getEnabled()) {
            throw new UserAlreadyVerifiedException("User is already verified.");
        }

        if (otp.getOtp() == null || !otp.getOtp().equals(dto.getOtp())) {
            throw new InvalidOtpException("The OTP provided is incorrect.");
        }

        HeapDogUser user = otp.getUser();
        user.setEnabled(true);
        repository.save(user);

        emailVerificationOtpRepository.deleteByUserId(dto.getUserId());

        return EmailVerificationResponseDto.builder()
                .message("Email verified successfully.")
                .build();
    }

    InternalUserResponseDto getUserById(Long userId) {
        HeapDogUser user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return InternalUserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .enabled(user.getEnabled())
                .build();
    }


}
