package com.back.handsUp.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class BoardTagId implements Serializable {
    private Long boardIdx;
    private Long tagIdx;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardTagId boardTagId = (BoardTagId) o;
        return Objects.equals(getBoardIdx(), boardTagId.getBoardIdx()) && Objects.equals(getTagIdx(), boardTagId.getTagIdx());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoardIdx(), getTagIdx());
    }
}

