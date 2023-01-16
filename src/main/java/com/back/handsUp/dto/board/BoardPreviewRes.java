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
    private String location;
    private LocalDateTime createdAt;

    @Builder
    public BoardPreviewRes(Long boardIdx, String status, String content, String location, LocalDateTime createdAt) {
        this.boardIdx = boardIdx;
        this.status = status;
        this.content = content;
        this.location = location;
        this.createdAt = createdAt;
    }
}
