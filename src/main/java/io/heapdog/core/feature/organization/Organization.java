package io.heapdog.core.feature.organization;

import io.heapdog.core.shared.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "organization",
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_organization_slug", columnNames = "slug"),
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization extends BaseEntity {
    @Column(name = "org_name", nullable = false)
    @NotBlank(message = "Name is mandatory")
    private String orgName;

    @Column(name = "slug", nullable = false, unique = true)
    @NotBlank(message = "Slug is mandatory")
    private String slug;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "email", length = 500)
    private String email;

    @Column(name = "website", length = 500)
    private String website;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Membership> memberships = new HashSet<>();
}
