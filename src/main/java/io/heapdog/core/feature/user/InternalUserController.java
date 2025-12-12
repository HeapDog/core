package io.heapdog.core.feature.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final HeapDogUserService userService;

    @GetMapping("/{userId}")
    InternalUserResponseDto getUser(@PathVariable Long userId) {
        InternalUserResponseDto user = userService.getUserById(userId);
        return user;
    }

}
