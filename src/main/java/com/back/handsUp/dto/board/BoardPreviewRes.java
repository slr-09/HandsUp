package com.back.handsUp.dto.board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardPreviewRes {
    private Long boardIdx;
    private String status;
    private String content;
    private double latitude;
    private double longitude;
    private String location;
    private LocalDateTime createdAt;

    @Builder
    public BoardPreviewRes(Long boardIdx, String status, String content, double latitude, double longitude, String location, LocalDateTime createdAt) {
        this.boardIdx = boardIdx;
        this.status = status;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.createdAt = createdAt;
    }
}
