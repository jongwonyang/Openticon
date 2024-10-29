package io.ssafy.openticon.ui.data.remote
import io.ssafy.openticon.ui.data.model.EditDeviceTokenRequestDto
import retrofit2.Response
import retrofit2.http.GET
import io.ssafy.openticon.ui.data.model.MemberEntity;
import retrofit2.http.Body
import retrofit2.http.POST

interface MemberApi {
    @GET("member")
    suspend fun getMemberInfo(): Response<MemberEntity>

    @POST("member/deviceToken")
    suspend fun editDeviceToken(
        @Body requestDto: EditDeviceTokenRequestDto
    ): Response<String>
}