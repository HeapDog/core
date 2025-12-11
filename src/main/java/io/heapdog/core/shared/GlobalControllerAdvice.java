package io.heapdog.core.shared;


import io.heapdog.core.feature.auth.InvalidOtpException;
import io.heapdog.core.feature.user.UserAlreadyVerifiedException;
import io.heapdog.core.feature.user.UserNotFoundException;
import io.heapdog.core.security.jwt.JwtValidationFailedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @SkipApiWrap
    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ApiError.FieldError> details = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> ApiError
                        .FieldError
                        .builder()
                        .field(err.getField())
                        .message(err.getDefaultMessage()).build())
                .toList();

        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("VALIDATION_ERROR")
                .message("Validation failed for request")
                .details(details)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @SkipApiWrap
    @ExceptionHandler(exception = BadCredentialsException.class)
    ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .code("BAD_CREDENTIALS")
                .message("Invalid username or password")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @SkipApiWrap
    @ExceptionHandler(exception = JwtValidationFailedException.class)
    ResponseEntity<ApiError> handleJWTVerificationFailedException(JwtValidationFailedException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .code("INVALID_TOKEN")
                .message("Invalid token")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @SkipApiWrap
    @ExceptionHandler(exception = AuthorizationDeniedException.class)
    ResponseEntity<ApiError> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .code("ACCESS_DENIED")
                .message("You do not have permission to access this resource")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @SkipApiWrap
    @ExceptionHandler(exception = InvalidOtpException.class)
    ResponseEntity<ApiError> handleInvalidOtpException(InvalidOtpException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("INVALID_OTP")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @SkipApiWrap
    @ExceptionHandler(exception = UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .code("USER_NOT_FOUND")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @SkipApiWrap
    @ExceptionHandler(exception = DisabledException.class)
    ResponseEntity<ApiError> handleDisabledException(DisabledException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .code("ACCOUNT_DISABLED")
                .message("Your account is not enabled yet.")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


    @SkipApiWrap
    @ExceptionHandler(exception = UserAlreadyVerifiedException.class)
    ResponseEntity<ApiError> handleUserAlreadyVerifiedException(UserAlreadyVerifiedException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("USER_ALREADY_VERIFIED")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @SkipApiWrap
    @ExceptionHandler(exception = DuplicateResourceException.class)
    ResponseEntity<ApiError> handleDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .code("DUPLICATE_RESOURCE")
                .message("Resource already exists")
                .details(List.of(ApiError.FieldError.builder().field(ex.getResourceName()).message(ex.getMessage()).build()))
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @SkipApiWrap
    @ExceptionHandler(exception = ResourceNotFoundException.class)
    ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .code("RESOURCE_NOT_FOUND")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @SkipApiWrap
    @ExceptionHandler(exception = HandlerMethodValidationException.class)
    ResponseEntity<ApiError> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpServletRequest request
    ) {
        // Return 400 Bad Request
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("VALIDATION_ERROR")
                .message("Validation failed for request")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    // swallow all
    @SkipApiWrap
    @ExceptionHandler(exception = Exception.class)
    ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred: ", ex);
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @SkipApiWrap
    @ExceptionHandler(exception = IllegalArgumentException.class)
    ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("ILLEGAL_ARGUMENT")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @SkipApiWrap
    @ExceptionHandler(exception = HttpMessageNotReadableException.class)
    ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.warn(ex.toString());
        ApiError errorResponse = ApiError
                .builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("MALFORMED_REQUEST")
                .message("Malformed request body")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
