package com.back.handsUp.dto.user;

import lombok.*;

@NoArgsConstructor
public class CharacterDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class GetCharacterInfo{
        private String eye;
        private String eyeBrow;
        private String glasses;
        private String nose;
        private String mouth;
        private String hair;
        private String hairColor;
        private String skinColor;
        private String backGroundColor;

    }
}
