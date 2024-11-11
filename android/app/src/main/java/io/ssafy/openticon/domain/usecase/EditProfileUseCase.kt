package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.MemberRepository
import io.ssafy.openticon.domain.model.ResponseWithStatus
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(
        nickname: String,
        bio: String,
        profileImage: MultipartBody.Part?
    ): Result<ResponseWithStatus<String>> {
        return try {
            val response = memberRepository.editProfile(nickname, bio, profileImage)

            if (response.isSuccessful) {
                Result.success(ResponseWithStatus(response.body() ?: "Success", response.code()))
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
