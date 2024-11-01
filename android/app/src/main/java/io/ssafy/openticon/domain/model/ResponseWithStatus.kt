package io.ssafy.openticon.domain.model

data class ResponseWithStatus<T>(
    var data: T,    // 원하는 데이터 타입으로 `data`를 정의할 수 있음
    val status: Int // 상태 코드
)