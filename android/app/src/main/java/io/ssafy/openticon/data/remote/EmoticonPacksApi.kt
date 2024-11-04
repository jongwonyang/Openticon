package io.ssafy.openticon.data.remote

import io.ssafy.openticon.data.model.PackInfoResponseDto
import io.ssafy.openticon.data.model.PageEmoticonPackResponseDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface EmoticonPacksApi {
    @GET("/api/v1/emoticonpacks/search")
    suspend fun searchEmoticonPacks(
        @Query("query") query: String,
        @Query("type") type: String,
        @Query("sort") sort: String,
        @Query("size") size: Int,
        @Query("page") page: Int
    ): PageEmoticonPackResponseDto

    @Multipart
    @POST("/api/v1/emoticonpacks/imageSearch")
    suspend fun imageSearchEmoticonPacks(
        @Part image: MultipartBody.Part
    ): PageEmoticonPackResponseDto  // 서버에서 응답으로 받을 데이터 형식에 맞게 변경

    @GET("/api/v1/emoticonpacks/info")
    suspend fun getPublicPackInfo(
        @Query("emoticonPackId") emoticonPackId: Int
    ): PackInfoResponseDto

    @Streaming
    @GET
    suspend fun downloadEmoticon(@Url url: String): ResponseBody
}