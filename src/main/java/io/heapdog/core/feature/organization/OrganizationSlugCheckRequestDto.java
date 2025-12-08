package io.heapdog.core.feature.organization;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationSlugCheckRequestDto {
    @NotBlank(message = "Slug is mandatory")
    private String slug;
}
