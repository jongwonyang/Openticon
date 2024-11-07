package io.ssafy.openticon.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.HttpParser;
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
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러"),
    DUPLICATE_THUMBNAIL(HttpStatus.BAD_REQUEST, "중복된 썸네일 이미지"),
    DUPLICATE_LIST_IMG(HttpStatus.BAD_REQUEST, "중복된 리스트 이미지"),
    PRIVATE_PACK(HttpStatus.FORBIDDEN, "비공개 이모티콘 팩입니다."),
    BLACKLIST_PACK(HttpStatus.FORBIDDEN, "차단된 이모티콘 팩입니다."),
    DUPLICATE_REPORT(HttpStatus.FORBIDDEN, "이미 신고한 이모티콘 팩입니다."),
    PACK_DATABASE_SAVE_ERROR(HttpStatus.BAD_REQUEST, "이모티콘 팩 데이터베이스 에러"),
    TAG_DATABASE_SAVE_ERROR(HttpStatus.BAD_REQUEST, "태그 데이터베이스 에러"),
    TAG_LIST_DATABASE_SAVE_ERROR(HttpStatus.BAD_REQUEST, "태그 리스트 데이터베이스 에러"),
    EMOTICON_DATABASE_SAVE_ERROR(HttpStatus.BAD_REQUEST, "이모티콘 데이터베이스 에러"),
    HARMFUL_IMAGES(HttpStatus.BAD_REQUEST,"유해한 이미지"),
    DUPLICATE_PACK_TITLE(HttpStatus.BAD_REQUEST,"중복된 타이틀");



    private final HttpStatus httpStatus;
    private final String message;
}
