package com.back.handsUp.domain.fcmToken;

import com.back.handsUp.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "Fcm_Token")
@Entity
public class FcmToken {

    @org.springframework.data.annotation.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fcmTokenIdx;
    private String fcmToken;

    @UpdateTimestamp
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userIdx")
    private User user;

    public void updateToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Builder
    public FcmToken(String fcmToken, User user) {
        this.fcmToken = fcmToken;
        this.user = user;
    }
}
