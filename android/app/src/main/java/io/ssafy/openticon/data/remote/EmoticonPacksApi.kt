package io.ssafy.openticon.data.remote

import io.ssafy.openticon.data.model.PackInfoResponseDto
import io.ssafy.openticon.data.model.PageEmoticonPackResponseDto
import okhttp3.ResponseBody
import retrofit2.http.GET
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

    @GET("/api/v1/emoticonpacks/info")
    suspend fun getPublicPackInfo(
        @Query("emoticonPackId") emoticonPackId: Int
    ): PackInfoResponseDto

    @Streaming
    @GET
    suspend fun downloadEmoticon(@Url url: String): ResponseBody
}