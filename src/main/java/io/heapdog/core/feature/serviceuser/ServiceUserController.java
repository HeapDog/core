package io.heapdog.core.feature.serviceuser;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service-users")
@RequiredArgsConstructor
public class ServiceUserController {

    private final ServiceUserService serviceUserService;


    @GetMapping("/{id}")
    ResponseEntity<ServiceUserReadResponseDto> getServiceUser(@PathVariable Long id) {
        var resp = serviceUserService.getServiceUser(id);
        return ResponseEntity.ok(resp);
    }

    @PostMapping
    ResponseEntity<ServiceUserCreateResponseDto>
    createServiceUser(@Valid @RequestBody ServiceUserCreateRequestDto request) {
        var resp = serviceUserService.createServiceUser(request);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/permissions")
    ResponseEntity<ServiceUserPermissionResponseDto> getAvailablePermissions() {
        var resp = serviceUserService.getServiceUserPermissions();
        return ResponseEntity.ok(resp);
    }

}
