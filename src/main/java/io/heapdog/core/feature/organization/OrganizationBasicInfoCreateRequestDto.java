package io.heapdog.core.feature.organization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrganizationBasicInfoCreateRequestDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Slug is mandatory")
    private String slug;

    @Size(max = 100, message = "Description can be at most 100 characters")
    private String description;

    @Size(max = 500, message = "Email can be at most 500 characters")
    private String email;

    @Size(max = 500, message = "Website can be at most 500 characters")
    private String website;

    @Size(max = 1000, message = "Address can be at most 1000 characters")
    private String address;

    @Size(max = 20, message = "Phone can be at most 20 characters")
    private String phone;
}
