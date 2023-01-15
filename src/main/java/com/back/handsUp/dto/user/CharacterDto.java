package com.back.handsUp.dto.user;

import lombok.*;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class CharacterDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class GetCharacterInfo{

        @NotBlank(message = "eye는 필수 입력 값입니다.")
        private String eye;

        @NotBlank(message = "eyeBrow는 필수 입력 값입니다.")
        private String eyeBrow;

        private String glasses;

        @NotBlank(message = "nose는 필수 입력 값입니다.")
        private String nose;

        @NotBlank(message = "mouth는 필수 입력 값입니다.")
        private String mouth;

        @NotBlank(message = "hair는 필수 입력 값입니다.")
        private String hair;

        @NotBlank(message = "hairColor는 필수 입력 값입니다.")
        private String hairColor;

        @NotBlank(message = "skinColor는 필수 입력 값입니다.")
        private String skinColor;

        @NotBlank(message = "backGroundColor는 필수 입력 값입니다.")
        private String backGroundColor;

    }
}
