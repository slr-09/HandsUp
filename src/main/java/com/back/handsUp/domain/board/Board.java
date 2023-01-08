package com.back.handsUp.domain.board;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
public class Board {

    @Id
    public long boardIdx;

    public String content;

    public String location;

    public String indicateLocation;

    public int messageDuration;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    public String status;
}
