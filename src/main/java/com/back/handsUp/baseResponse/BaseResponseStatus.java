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
    NON_EXIST_EMAIL(false, 4000, "존재하지 않는 이메일입니다."),
    INVALID_PASSWORD(false, 4001, "비밀번호가 틀렸습니다."),
    EXIST_USER(false, 4002, "이미 존재하는 유저입니다."),
    INVALID_REQUEST(false, 4003, "입력 양식이 잘못되었습니다."),
    SAME_PASSWORD(false, 4004, "현재 비밀번호와 변경할 비밀번호가 같습니다."),




    NON_EXIST_BOARDIDX(false, 4010, "게시물 인덱스가 존재하지 않습니다."),
    NON_EXIST_USERIDX(false, 4011, "유저 인덱스가 존재하지 않습니다."),
    EMPTY_USER(false, 4012, "공백없이 입력해주세요."),
    NON_EXIST_CHARACTERIDX(false, 4013, "캐릭터 인덱스가 존재하지 않습니다."),
    NON_EXIST_SCHOOLIDX(false, 4014, "학교 인덱스가 존재하지 않습니다."),
    NON_EXIST_BOARDUSERIDX(false, 4015, "유저가 작성한 게시물이 아닙니다."),
    NON_EXIST_BOARD_LIST(false, 4016, "게시물이 존재하지 않습니다."),
    NON_EXIST_CHATROOMIDX(false, 4017, "채팅방이 존재하지 않습니다."),




    LOCATION_ERROR(false, 4021, "위치정보에 오류가 발생했습니다."),
    MESSAGEDURATION_ERROR(false, 4022, "메세지 지속시간에 오류가 발생했습니다."),

    NON_EXIST_CHARACTER_VALUE(false,4031,"캐릭터 생성에 필요한 값이 모두 입력되지 않았습니다."),



    /**
     * 5000번대 서버 에러
     */
    DATABASE_INSERT_ERROR(false, 5000, "데이터베이스 저장 오류가 발생했습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, 5001, "비밀번호 암호화 오류가 발생했습니다.");


    private final boolean isSuccess;
    private final int statusCode;
    private final String message;
    BaseResponseStatus(boolean isSuccess, int statusCode, String message) {
        this.isSuccess = isSuccess;
        this.statusCode = statusCode;
        this.message = message;
    }
}
