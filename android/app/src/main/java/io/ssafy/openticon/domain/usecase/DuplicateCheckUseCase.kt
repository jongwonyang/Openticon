package io.ssafy.openticon.domain.usecase

import android.util.Log
import io.ssafy.openticon.data.repository.MemberRepository
import javax.inject.Inject

class DuplicateCheckUseCase @Inject constructor(
    private val repository: MemberRepository
){
    suspend operator fun invoke(nickname: String): Result<Boolean> {
        return try {
            val response = repository.duplicateCheck(nickname)
            Log.d("DuplicateCheckUseCase", "Response: $response")
            when {
                response.isSuccessful -> Result.success(false)
                response.code() == 409 -> Result.success(true)
                else -> Result.failure(Exception("Unexpected response code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}