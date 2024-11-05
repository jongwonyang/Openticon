package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.MemberRepository
import io.ssafy.openticon.domain.model.ResponseWithStatus
import retrofit2.HttpException
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(): Result<ResponseWithStatus<String>> {
        return try {
            val response = memberRepository.editProfile()
            if (response.isSuccessful) {
                // 요청 성공 시 ResponseWithStatus로 성공 결과 반환
                Result.success(ResponseWithStatus(response.body() ?: "Success", response.code()))
            } else {
                // 요청 실패 시 실패 결과 반환
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            // 예외 발생 시 실패 결과 반환
            Result.failure(e)
        }
    }
}
