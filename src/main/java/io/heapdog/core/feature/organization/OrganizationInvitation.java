package io.heapdog.core.feature.organization;

import io.heapdog.core.feature.user.HeapDogUser;
import io.heapdog.core.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organization_invitation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrganizationInvitation extends BaseEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private HeapDogUser user;

    private String code;

    @Column(name = "is_accepted", nullable = false)
    @Builder.Default
    private boolean accepted = false;

    @Column(name = "is_revoked", nullable = false)
    @Builder.Default
    private Boolean revoked = false;
}
