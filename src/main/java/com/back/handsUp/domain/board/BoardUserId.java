package com.back.handsUp.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class BoardUserId implements Serializable {
    private Long boardIdx;
    private Long userIdx;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardUserId boardUserId = (BoardUserId) o;
        return Objects.equals(getBoardIdx(), boardUserId.getBoardIdx()) && Objects.equals(getUserIdx(), boardUserId.getUserIdx());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoardIdx(), getUserIdx());
    }
}

