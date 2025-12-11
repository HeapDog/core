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


    @PostMapping
    ResponseEntity<ServiceUserCreateResponseDto>
    createServiceUser(@Valid @RequestBody ServiceUserCreateRequestDto request) {
        var resp = serviceUserService.createServiceUser(request);
        return ResponseEntity.ok(resp);
    }

}
