package com.back.handsUp.domain.board;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
public class Board {

    @Id
    private long boardIdx;

    private String content;

    private String location;

    private String indicateLocation;

    private int messageDuration;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String status;
}
