package io.ssafy.openticon.data.remote

import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.data.model.MemberResponseDto
import retrofit2.Response
import javax.inject.Inject

class MemberApiService @Inject constructor(
    private val memberApi: MemberApi
) {
    // Member 정보 가져오는 함수 (MemberResponseDTO로 성공/오류 상태 반환)
    suspend fun getMemberInfo(): MemberResponseDto {
        return try {
            val response: Response<MemberEntity?> = memberApi.getMemberInfo()
            if (response.isSuccessful) {
                val memberEntity = response.body()
                if (memberEntity != null) {
                    // 성공적인 경우, stateCode = 200
                    MemberResponseDto(
                        stateCode = 200,
                        message = "Success",
                        memberEntity = memberEntity
                    )
                } else {
                    // body가 null인 경우, stateCode = 204 (No Content)
                    MemberResponseDto(
                        stateCode = 204,
                        message = "No content available",
                        memberEntity = MemberEntity() // 기본값을 사용하거나 null 허용시 수정 가능
                    )
                }
            } else {
                // 실패한 경우, 상태 코드와 오류 메시지 설정
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val errorMessage = when (response.code()) {
                    401 -> "Unauthorized - 로그인 필요"
                    404 -> "Not Found - 사용자 정보 없음"
                    500 -> "Server Error - 서버 문제 발생"
                    else -> "Failed with status code: ${response.code()}"
                }
                MemberResponseDto(
                    stateCode = response.code(),
                    message = "$errorMessage: $errorBody",
                    memberEntity = MemberEntity() // 기본값을 사용하거나 null 허용시 수정 가능
                )
            }
        } catch (e: Exception) {
            // 예외 발생 시, 상태 코드 500으로 설정
            MemberResponseDto(
                stateCode = 500,
                message = "Exception occurred: ${e.message}",
                memberEntity = MemberEntity() // 기본값을 사용하거나 null 허용시 수정 가능
            )
        }
    }
}

