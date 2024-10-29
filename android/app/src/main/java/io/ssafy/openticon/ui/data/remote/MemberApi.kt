package io.ssafy.openticon.ui.data.remote
import retrofit2.Response
import retrofit2.http.GET
import io.ssafy.openticon.ui.data.model.MemberEntity;

interface MemberApi {
    @GET("member")
    suspend fun getMemberInfo(): Response<MemberEntity>
}