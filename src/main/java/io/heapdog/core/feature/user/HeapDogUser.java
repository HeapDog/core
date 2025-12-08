package io.heapdog.core.feature.user;

import io.heapdog.core.feature.organization.Membership;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "heapdog_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class HeapDogUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = false;

    public enum Role {
        ROLE_USER,
        ROLE_ADMIN
    };

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Membership> memberships = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_membership_id", referencedColumnName = "id")
    private Membership currentMembership;
}
