package com.back.handsUp.dto.user;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.JoinColumn;
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
        private Long schoolIdx;
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

    @Getter
    public static class ReqWithdraw {

        @Id
        private Long userIdx;
    }
}
