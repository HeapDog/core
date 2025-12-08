package io.heapdog.core.feature.organization;

import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "membership")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Membership extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private HeapDogUser user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private OrganizationRole role;

}
