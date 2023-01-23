package com.back.handsUp.dto.chat;

import com.back.handsUp.dto.board.BoardDto;
import com.back.handsUp.dto.user.CharacterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResChatRoom {
        private String nickname;
        private BoardDto.BriefBoard board;
        private CharacterDto.GetCharacterInfo character;
    }

}
