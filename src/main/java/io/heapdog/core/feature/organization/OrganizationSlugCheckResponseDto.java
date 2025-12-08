package io.heapdog.core.feature.organization;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationSlugCheckResponseDto {
    private  String slug;
    private boolean isAvailable;
}
