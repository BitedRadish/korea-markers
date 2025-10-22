package com.korea_markers_backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND("U001", "해당 사용자를 찾을 수 없습니다. ID: %s", HttpStatus.NOT_FOUND),
    USER_EMAIL_DUPLICATE("U002", "중복된 이메일입니다.", HttpStatus.CONFLICT),
    USER_INVALID_TOKEN("U003", "유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST),
    USER_INVALID_EMAIL_VERIFICATION("U004", "인증되지 않은 이메일입니다.", HttpStatus.UNAUTHORIZED),
    USER_CONFLICT_PASSWORD("U005","비밀번호와 비밀번호 확인이 일치하지 않습니다.",HttpStatus.CONFLICT),
    USER_INVALID_REFRESH_TOKEN("U006","유효하지 않은 리프레시 토큰입니다.",HttpStatus.UNAUTHORIZED)
    ;


    private final String code;        // A001, A002 등
    private final String message;     // 사용자에게 보여줄 메시지
    private final HttpStatus status;  //http status 코드

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
