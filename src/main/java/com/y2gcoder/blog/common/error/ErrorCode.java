package com.y2gcoder.blog.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 포스트
    NOT_FOUND_POST(HttpStatus.BAD_REQUEST, "P-001", "해당 포스트를 찾을 수 없습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }
}
