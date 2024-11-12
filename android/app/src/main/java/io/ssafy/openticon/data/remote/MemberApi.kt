package io.ssafy.openticon.data.remote
import io.ssafy.openticon.data.model.EditDeviceTokenRequestDto
import retrofit2.Response
import retrofit2.http.GET
import io.ssafy.openticon.data.model.MemberEntity;
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface MemberApi {
    @GET("/api/v1/member")
    suspend fun getMemberInfo(): Response<MemberEntity?>


    @POST("/api/v1/member/deviceToken")
    suspend fun editDeviceToken(
        @Body requestDto: EditDeviceTokenRequestDto
    ): Response<String>

    @Multipart
    @PUT("/api/v1/member")
    suspend fun editProfile(
        @Part("nickname") nickname: RequestBody,
        @Part("bio") bio: RequestBody,
        @Part profile_img: MultipartBody.Part? = null
    ): Response<String>


    @DELETE("/api/v1/member")
    suspend fun deleteMember(): Response<Unit>

    @GET("/api/v1/member/duplicate-check")
    suspend fun duplicateCheck(
        @Query("nickname") nickname: String
    ) : Response<Boolean>

    @GET("api/v1/member/writer")
    suspend fun getWriterInfo(@Query("nickname") nickname: String): Response<MemberEntity?>
}