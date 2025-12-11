package io.heapdog.core.feature.serviceuser;

import io.heapdog.core.shared.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceUserService {

    private final ServiceUserRepository serviceUserRepository;

    @PreAuthorize("hasRole('ADMIN')")
    ServiceUserCreateResponseDto createServiceUser(ServiceUserCreateRequestDto request) {
        ServiceUser serviceUser = ServiceUser.builder()
                .name(request.getName())
                .apiKey(String.format("svc-%s", OtpGenerator.generateOtp(32)))
                .enabled(true)
                .build();
        var saved = serviceUserRepository.save(serviceUser);
        return ServiceUserCreateResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .apiKey(saved.getApiKey())
                .enabled(saved.isEnabled())
                .build();
    }
}
