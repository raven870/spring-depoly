package np.com.gautam101.com.testHibernate.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_token",indexes = {
        @Index(name = "refresh_jti_idx",columnList = "jti",unique = true),
        @Index(name = "refresh_tokens_user_id_idx",columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jti",unique = true,nullable = false,updatable = false)
    private String jti;


    @ManyToOne(optional=false,fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "user_id",updatable = false)
    private User user;

    @Column(updatable = false,nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private  Instant expireAt;

    @Column(nullable = false)
    private Boolean revoked;

    private String replacedByToken;


}
