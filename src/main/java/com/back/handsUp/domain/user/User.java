package com.back.handsUp.domain.user;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private Long userIdx;

    private String email;

    private String password;

    private String nickname;

    private LocalDateTime nicknameModifiedAt;

    private String location;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
