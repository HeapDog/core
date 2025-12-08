package io.heapdog.core.feature.user;

import io.heapdog.core.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_verification_otp")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailVerificationOtp extends BaseEntity {

    @Column(nullable = false)
    private String otp;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
//    @Fetch(FetchMode.JOIN)
    private HeapDogUser user;

}
