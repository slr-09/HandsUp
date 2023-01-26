package com.back.handsUp.dto.board;

import com.back.handsUp.dto.user.CharacterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.back.handsUp.domain.user.Character;
import org.jetbrains.annotations.NotNull;

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
        private CharacterDto.GetCharacterInfo character;
        private String location;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SingleBoardRes {
        private String nickname;
        private String location;
        private String content;
        private String tag;
        private String didLike;
        private int messageDuration;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BriefBoard {
        private Long boardIdx;
        private String location;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReceivedLikeRes implements Comparable<ReceivedLikeRes> {
        private Long chatRoomIdx;
        private LocalDateTime LikeCreatedAt;
        private String text;
        private String boardContent;
        private CharacterDto.GetCharacterInfo character;

        @Override
        public int compareTo(@NotNull ReceivedLikeRes res) {
            if (res.LikeCreatedAt.isBefore(LikeCreatedAt)) {
                return 1;
            } else if (res.LikeCreatedAt.isAfter(LikeCreatedAt)) {
                return -1;
            }
            return 0;
        }
    }
}
