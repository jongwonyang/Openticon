package io.ssafy.openticon.data.remote
import io.ssafy.openticon.data.model.EditDeviceTokenRequestDto
import retrofit2.Response
import retrofit2.http.GET
import io.ssafy.openticon.data.model.MemberEntity;
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface MemberApi {
    @GET("/api/v1/member")
    suspend fun getMemberInfo(): Response<MemberEntity?>


    @POST("/api/v1/member/deviceToken")
    suspend fun editDeviceToken(
        @Body requestDto: EditDeviceTokenRequestDto
    ): Response<String>
}