package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.MemberRepository
import io.ssafy.openticon.domain.model.ResponseWithStatus
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.http.Part
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(
        nickname: String,
        profileImage: MultipartBody.Part?
    ): Result<ResponseWithStatus<String>> {

        // 로그 추가
        if (profileImage != null) {
            println("Profile Image Part: ${profileImage.body.contentLength()} bytes, " +
                    "File Name: ${profileImage.headers?.get("Content-Disposition")}")
        } else {
            println("Profile Image Part is null")
        }

        return try {
            val response = memberRepository.editProfile(nickname, profileImage)
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