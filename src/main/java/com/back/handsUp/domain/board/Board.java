package com.back.handsUp.domain.board;

import com.back.handsUp.domain.user.User;
import com.back.handsUp.dto.board.BoardPreviewRes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Setter
@Getter
@Table(name = "board")
@NoArgsConstructor
@DynamicInsert
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Board {

    @Id
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long boardIdx;


    @javax.persistence.Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    //위도
    @javax.persistence.Column(nullable = true)
    private double latitude;

    //경도
    @javax.persistence.Column(nullable = true)
    private double longitude;


    @javax.persistence.Column(nullable = false, length= 10)
    private String indicateLocation;

    @javax.persistence.Column(nullable = false)
    private int messageDuration;

    @CreationTimestamp
    @javax.persistence.Column(updatable = false)
    public LocalDateTime createdAt;

    @UpdateTimestamp
    public LocalDateTime updatedAt;

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private String status;

    @Builder
    public Board(String content, double latitude, double longitude, String indicateLocation, int messageDuration, String status) {
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.indicateLocation = indicateLocation;
        this.messageDuration = messageDuration;
        this.status = status;
    }

    public BoardPreviewRes toPreviewRes() {
        return BoardPreviewRes.builder()
                .boardIdx(this.boardIdx)
                .status(this.status)
                .content(this.content)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .createdAt(this.createdAt)
                .build();
    }
    public void changeBoard(String content, double latitude, double longitude, String indicateLocation, int messageDuration) {
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.indicateLocation = indicateLocation;
        this.messageDuration = messageDuration;
    }

    public void changeStatus (String newStatus) {
        this.status = newStatus;
    }
}
