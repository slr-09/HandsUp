package com.back.handsUp.domain.user;

import com.back.handsUp.baseResponse.BaseEntity;
import com.back.handsUp.utils.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "user")
@NoArgsConstructor
@DynamicInsert
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(nullable = false, length = 15)
    private String nickname;

    @Column(columnDefinition="date default (current_date)")
    private Date nicknameUpdatedAt;

    @OneToOne
    @JoinColumn(name = "characterIdx")
    private Character characterIdx;

    @OneToOne
    @JoinColumn(name = "schoolIdx")
    private School schoolIdx;

    @Column(columnDefinition = "varchar(20) default 'ACTIVE'")
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String email, String password, String nickname, Date nicknameUpdatedAt, Character characterIdx, School schoolIdx, String status, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.nicknameUpdatedAt = nicknameUpdatedAt;
        this.characterIdx = characterIdx;
        this.schoolIdx = schoolIdx;
        this.status = status;
        this.role = role;
    }

    public void changeStatus (String newStatus) {
        this.status = newStatus;
    }

    public String getRoleName(){
        return this.role.name();
    }
}
