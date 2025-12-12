package io.heapdog.core.feature.serviceuser;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ServiceUserPermissionResponseDto {

    private List<Permission> permissions;

    @Builder
    @Data
    static class Permission {
        private String name;
        private String label;
        private String description;
    }
}
