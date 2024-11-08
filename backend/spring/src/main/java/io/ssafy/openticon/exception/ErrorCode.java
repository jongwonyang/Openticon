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
    DUPLICATE_TAG_NAME(HttpStatus.BAD_REQUEST, "중복된 태그 이름"),
    SAFE_SEARCH_ERROR(HttpStatus.BAD_REQUEST, "세이프 서치 에러"),
    PRIVATE_PACK(HttpStatus.FORBIDDEN, "비공개 이모티콘 팩입니다."),
    BLACKLIST_PACK(HttpStatus.FORBIDDEN, "차단된 이모티콘 팩입니다."),
    DUPLICATE_REPORT(HttpStatus.FORBIDDEN, "이미 신고한 이모티콘 팩입니다."),
    PACK_DATABASE_SAVE_ERROR(HttpStatus.BAD_REQUEST, "이모티콘 팩 데이터베이스 에러"),
    TAG_DATABASE_SAVE_ERROR(HttpStatus.BAD_REQUEST, "태그 데이터베이스 에러"),
    TAG_LIST_DATABASE_SAVE_ERROR(HttpStatus.BAD_REQUEST, "태그 리스트 데이터베이스 에러"),
    EMOTICON_DATABASE_SAVE_ERROR(HttpStatus.BAD_REQUEST, "이모티콘 데이터베이스 에러"),
    EMOTICON_PACK_EMPTY(HttpStatus.BAD_REQUEST, "이모티콘 팩 없음"),
    IAM_PORT_PAYMENT_ERROR(HttpStatus.BAD_REQUEST, "결제 정보 오류"),
    DUPLICATE_EMOTICON_PACK_PURCHASE(HttpStatus.BAD_REQUEST, "이미 구매한 이모티콘 팩입니다."),
    INSUFFICIENT_BALANCE_ERROR(HttpStatus.PAYMENT_REQUIRED, "소지 금액이 부족합니다."),
    NON_POSITIVE_INTEGER(HttpStatus.PAYMENT_REQUIRED, "금액이 잘못 입력되었습니다."),
    POINT_LIST_NO_CONTENT(HttpStatus.PAYMENT_REQUIRED, "포인트 기록이 없습니다."),
    HARMFUL_IMAGES(HttpStatus.BAD_REQUEST,"유해한 이미지"),
    DUPLICATE_PACK_TITLE(HttpStatus.BAD_REQUEST,"중복된 타이틀"),
    TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "요청 타임아웃"),
    NOT_FOUND_OBJECTION(HttpStatus.BAD_REQUEST, "이모티콘 팩을 찾을 수 없습니다."),
    NOT_FOUND_ANSWER(HttpStatus.BAD_REQUEST, "이의 신청에 대한 답변을 찾을 수 없습니다."),
    DUPLICATE_OBJECTION(HttpStatus.FORBIDDEN, "이미 이모티콘 팩에 대한 이의 신청을 진행하였습니다."),
    ACCESS_DENIED_OBJECTION(HttpStatus.FORBIDDEN, "현재 이모티콘 팩의 이의 신청 요청자가 아닙니다."),
    OBJECTION_REPORT_STATE_TYPE_ERROR(HttpStatus.BAD_REQUEST, "이의제기 상태가 잘못 입력 되었습니다.");



    private final HttpStatus httpStatus;
    private final String message;
}
