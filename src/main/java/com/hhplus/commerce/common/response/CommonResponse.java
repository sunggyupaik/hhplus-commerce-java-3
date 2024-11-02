package com.hhplus.commerce.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {
    private Result result;
    private T data;
    private String message;
    private String code;

    public static <T> CommonResponse<T> success(T data) {
        return (CommonResponse<T>) CommonResponse.builder()
                .result(Result.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> fail(String message, String errorCode) {
        return (CommonResponse<T>) CommonResponse.builder()
                .result(Result.FAIL)
                .message(message)
                .code(errorCode)
                .build();
    }

    public static <T> CommonResponse<T> fail(ErrorCode errorCode) {
        return (CommonResponse<T>) CommonResponse.builder()
                .result(Result.FAIL)
                .message(errorCode.getErrorMsg())
                .code(errorCode.name())
                .build();
    }

    public enum Result {
        SUCCESS, FAIL
    }
}
