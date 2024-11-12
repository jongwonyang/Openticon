package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.model.MemberEntity
import io.ssafy.openticon.data.repository.MemberRepository
import io.ssafy.openticon.domain.model.ResponseWithStatus
import retrofit2.Response
import javax.inject.Inject

class WriterInfoUsecase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend operator fun invoke(nickname: String): Result<ResponseWithStatus<MemberEntity?>> {
        return try {
            val memberData: Response<MemberEntity?> = repository.getWriterInfo(nickname)
            if (memberData.isSuccessful) {
                val response = ResponseWithStatus(
                    data = memberData.body(),
                    status = memberData.code()
                )
                Result.success(response)
            } else {
                // 실패한 경우 적절한 메시지를 반환
                Result.failure(Exception("Error: ${memberData.code()} - ${memberData.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
