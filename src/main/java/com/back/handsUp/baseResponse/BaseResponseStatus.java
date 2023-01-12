package com.back.handsUp.baseResponse;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    /**
     * 2000번대 성공
     */
    SUCCESS(true, 2000, "요청에 성공하였습니다."),

    /**
     * 4000번대 클라이언트 에러
     */

    NON_EXIST_BOARDIDX(false, 4010, "게시물 인덱스가 존재하지 않습니다."),
    NON_EXIST_USERIDX(false, 4011, "유저 인덱스가 존재하지 않습니다."),


    LOCATION_ERROR(false, 4021, "위치정보에 오류가 발생했습니다."),
    MESSAGEDURATION_ERROR(false, 4022, "메세지 지속시간에 오류가 발생했습니다."),


    /**
     * 5000번대 서버 에러
     */
    DATABASE_INSERT_ERROR(false, 5000, "데이터베이스 저장 오류가 발생했습니다.");


    private final boolean isSuccess;
    private final int statusCode;
    private final String message;
    BaseResponseStatus(boolean isSuccess, int statusCode, String message) {
        this.isSuccess = isSuccess;
        this.statusCode = statusCode;
        this.message = message;
    }
}
