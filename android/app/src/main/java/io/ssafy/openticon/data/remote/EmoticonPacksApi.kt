package io.ssafy.openticon.data.remote

import io.ssafy.openticon.data.model.PageEmoticonPackResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface EmoticonPacksApi {
    @GET("emoticonpacks/search")
    suspend fun searchEmoticonPacks(
        @Query("query") query: String,
        @Query("type") type: String,
        @Query("sort") sort: String,
        @Query("size") size: Int,
        @Query("page") page: Int
    ): PageEmoticonPackResponseDto
}