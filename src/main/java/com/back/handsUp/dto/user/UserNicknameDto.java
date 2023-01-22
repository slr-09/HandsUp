package com.back.handsUp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserNicknameDto {

		private Long userIdx;
		private String nickname;

}
