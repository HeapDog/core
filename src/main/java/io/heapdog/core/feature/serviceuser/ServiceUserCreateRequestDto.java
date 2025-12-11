package io.heapdog.core.feature.serviceuser;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceUserCreateRequestDto {

    @NotEmpty
    private String name;

}
