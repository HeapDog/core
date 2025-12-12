package io.heapdog.core.feature.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InternalUserResponseDto {

    private Long id;
    private String username;
    private String role;
    private Boolean enabled;

}
