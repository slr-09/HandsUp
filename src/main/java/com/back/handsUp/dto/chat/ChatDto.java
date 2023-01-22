package com.back.handsUp.dto.chat;

import com.back.handsUp.domain.board.Board;
import com.back.handsUp.domain.user.Character;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public class ChatDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResChat {
        private Board board;
        private Character character;
        private String nickname;
    }

}
