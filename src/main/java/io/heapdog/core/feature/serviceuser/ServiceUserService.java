package io.heapdog.core.feature.serviceuser;

import io.heapdog.core.shared.ResourceNotFoundException;
import io.heapdog.core.shared.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
                .permissions(request.getPermissions())
                .build();
        var saved = serviceUserRepository.save(serviceUser);
        return ServiceUserCreateResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .apiKey(saved.getApiKey())
                .enabled(saved.isEnabled())
                .permissions(saved.getPermissions())
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    ServiceUserReadResponseDto getServiceUser(Long id) {
        var serviceUser = serviceUserRepository.findByIdWithPermissionsAndCreator(id)
                .orElseThrow(() -> new ResourceNotFoundException("ServiceUser", "id", id.toString()));
        return ServiceUserReadResponseDto.builder()
                .id(serviceUser.getId())
                .name(serviceUser.getName())
                .apiKey(serviceUser.getApiKey())
                .enabled(serviceUser.isEnabled())
                .createdAt(serviceUser.getCreatedAt())
                .updatedAt(serviceUser.getUpdatedAt())
                .createdBy(ServiceUserReadResponseDto.User.builder()
                        .id(serviceUser.getCreatedBy().getId())
                        .username(serviceUser.getCreatedBy().getUsername())
                        .build())
                .permissions(serviceUser.getPermissions())
                .lastAccessedAt(serviceUser.getLastAccessedAt())
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    ServiceUserPermissionResponseDto getServiceUserPermissions() {
        var permissions = Arrays.stream(ServiceUserPermission.values())
                .map(permission -> ServiceUserPermissionResponseDto.Permission.builder()
                        .name(permission.name())
                        .label(permission.getLabel())
                        .description(permission.getDescription())
                        .build())
                .toList();
        return ServiceUserPermissionResponseDto.builder()
                .permissions(permissions)
                .build();
    }
}
