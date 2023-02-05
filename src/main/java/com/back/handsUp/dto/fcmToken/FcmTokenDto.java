package com.back.handsUp.dto.fcmToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FcmTokenDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class updateToken {
        private String fcmToken;
    }
}
