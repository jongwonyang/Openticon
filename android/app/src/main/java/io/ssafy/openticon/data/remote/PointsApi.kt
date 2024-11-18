package io.ssafy.openticon.data.remote

import io.ssafy.openticon.data.model.PointResponseDto
import io.ssafy.openticon.data.model.PurchaseEmoticonPackRequestDto
import io.ssafy.openticon.data.model.PurchasePointRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PointsApi {
    @POST("/api/v1/points/purchase-pack")
    suspend fun purchaseEmoticonPack(
        @Body request: PurchaseEmoticonPackRequestDto
    ): Response<PointResponseDto>

    @POST("/api/v1/points/purchase-point")
    suspend fun purchasePoint(
        @Body request: PurchasePointRequestDto
    ): Response<PointResponseDto>
}