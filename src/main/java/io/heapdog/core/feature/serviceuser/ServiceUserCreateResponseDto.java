package io.heapdog.core.feature.serviceuser;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceUserCreateResponseDto {

    private Long id;
    private String name;
    private String apiKey;
    private Boolean enabled;
}
