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
    public static class ResChatMessageList {
        private Board board;
        private Character character;
        private String nickname;
        private List<BriefChatMessage> chatMessageList;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BriefChatMessage {
        private Long chatMessageIdx;
        private Boolean isMe; //메세지를 보낸 유저가 '나'이면 TRUE
        private String chatContents;
        private LocalDateTime createdAt;
    }
}
