package io.ssafy.openticon.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AccessToken 만료"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "RefreshToken 만료"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없음"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "엔티티 없음"),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "적절하지 않은 인자"),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 없음"),
    INVALID_REQUEST_PARAMS(HttpStatus.BAD_REQUEST, "잘못된 요청 파라미터"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러"),;




    private final HttpStatus httpStatus;
    private final String message;
}
