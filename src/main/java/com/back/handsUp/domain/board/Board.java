package com.back.handsUp.domain.board;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
public class Board {
    @Id
    public long id;

    @Column("boardContents")
    public String contents;

    @Column("messageDuration")
    public int duration;
}
