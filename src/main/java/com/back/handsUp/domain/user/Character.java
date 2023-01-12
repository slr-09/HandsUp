package com.back.handsUp.domain.user;

import com.back.handsUp.baseResponse.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "characters")
@NoArgsConstructor
@DynamicInsert
public class Character extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long characterIdx;

    @Column(nullable = false, length = 20)
    private String eye;

    @Column(nullable = false, length = 20)
    private String eyeBrow;

    @Column(nullable = true, length = 20)
    private String glasses;

    @Column(nullable = false, length = 20)
    private String nose;

    @Column(nullable = false, length = 20)
    private String mouth;

    @Column(nullable = false, length = 20)
    private String hair;

    @Column(nullable = false, length = 20)
    private String hairColor;

    @Column(nullable = false, length = 20)
    private String skinColor;

    @Column(nullable = false, length = 20)
    private String backGroundColor;

    @Column(columnDefinition = "varchar(20) default 'ACTIVE'")
    private String status;

    @Builder
    public Character(String eye, String eyeBrow, String glasses, String nose, String mouth, String hair, String hairColor, String skinColor, String backGroundColor, String status) {
        this.eye = eye;
        this.eyeBrow = eyeBrow;
        this.glasses = glasses;
        this.nose = nose;
        this.mouth = mouth;
        this.hair = hair;
        this.hairColor = hairColor;
        this.skinColor = skinColor;
        this.backGroundColor = backGroundColor;
        this.status = status;
    }

    public void changeStatus (String newStatus) {
        this.status = newStatus;
    }

}

