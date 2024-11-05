package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.MemberRepository
import javax.inject.Inject

class DeleteMemberUseCase @Inject constructor(
    private val repository: MemberRepository
){
    suspend operator fun invoke(): Result<Unit> {
        try {
            val response = repository.deleteMember()
            if (response.isSuccessful) {
                return Result.success(Unit)
            } else {
                return Result.failure(Exception("회원 삭제 실패"))
            }
        } catch (e: Exception) {
        return Result.failure(e)
        }
    }
}