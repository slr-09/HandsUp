package com.back.handsUp.baseResponse;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 2000번대 성공
     */
    SUCCESS(true, 2000, "요청에 성공하였습니다.");

    /**
     * 4000번대 클라이언트 에러
     */

    /**
     * 5000번대 서버 에러
     */
    private final boolean isSuccess;
    private final int statusCode;
    private final String message;
    BaseResponseStatus(boolean isSuccess, int statusCode, String message) {
        this.isSuccess = isSuccess;
        this.statusCode = statusCode;
        this.message = message;
    }
}
