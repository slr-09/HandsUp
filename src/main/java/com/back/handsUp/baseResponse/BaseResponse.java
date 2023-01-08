package com.back.handsUp.baseResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "statusCode", "message", "result"})
public class BaseResponse<T> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private String message;
    private final int statusCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    /**
     * 성공 시
     * @param result
     */
    public BaseResponse(T result) {
        this.isSuccess = BaseResponseStatus.SUCCESS.isSuccess();
        this.message = BaseResponseStatus.SUCCESS.getMessage();
        this.statusCode = BaseResponseStatus.SUCCESS.getStatusCode();
        this.result = result;
    }

    /**
     * 실패 시
     * @param status
     */
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.statusCode = getStatusCode();
    }
}
