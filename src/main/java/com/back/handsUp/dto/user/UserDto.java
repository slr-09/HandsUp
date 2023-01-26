package com.back.handsUp.dto.user;

import lombok.*;

import javax.validation.constraints.*;

@NoArgsConstructor
public class UserDto {
    @Getter
    @AllArgsConstructor
    @Builder
    @Setter
    public static class ReqSignUp {
        @Email
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        private String email;

        @Pattern(regexp = "^[a-zA-z0-9]{8,}$", message = "비밀번호는 특수문자를 제외한 8자리 이상이어야 합니다." )
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        private String password;

        @Pattern(regexp = "^[ㄱ-ㅎ가-힣0-9]{2,6}$", message = "닉네임은 특수문자를 제외한 2~5자리여야 합니다." )
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;

        private Long characterIdx;
        private String schoolName;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReqLogIn {
        @Email
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        private String email;

        @Pattern(regexp = "^[a-zA-z0-9]{8,}$", message = "비밀번호는 특수문자를 제외한 8자리 이상이어야 합니다." )
        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        private String password;
    }

 
    @AllArgsConstructor
    @Builder
    @Getter
    public static class ReqPwd {
        private String currentPwd;
        private String newPwd;
    }

    @Builder
    public static class ReqWithdraw {

        private Long userIdx;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class ResEmail {
        private String email;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ReqNickname {
        private String nickname;
    }
}
