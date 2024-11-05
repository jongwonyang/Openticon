package io.ssafy.openticon.data.remote

import io.ssafy.openticon.data.model.PurchaseEmoticonResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PurchaseApi {
    @GET("/api/v1/purchased")
    suspend fun fetchPurchasedPacks(
        @Query("all") all: Boolean = true,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): List<PurchaseEmoticonResponseDto>
}