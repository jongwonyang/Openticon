package io.ssafy.openticon.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OpenticonException extends RuntimeException{

    private final ErrorCode errorCode;

}
