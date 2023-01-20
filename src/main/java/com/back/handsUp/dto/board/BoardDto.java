package com.back.handsUp.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.back.handsUp.domain.user.Character;
import java.util.List;

@NoArgsConstructor
public class BoardDto {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class GetBoardInfo {
        private String indicateLocation;
        private String location;
        private String content;
        private String tag;
        private int messageDuration;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetBoardMap {
        private Long boardIdx;
        private Character character;
        private String location;
    }
}
