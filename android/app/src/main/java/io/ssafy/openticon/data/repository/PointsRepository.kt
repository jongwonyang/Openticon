package io.ssafy.openticon.data.repository

import io.ssafy.openticon.data.model.PointResponseDto
import io.ssafy.openticon.data.model.PurchaseEmoticonPackRequestDto
import io.ssafy.openticon.data.model.PurchasePointRequestDto
import io.ssafy.openticon.data.remote.PointsApi
import retrofit2.Response
import javax.inject.Inject

class PointsRepository @Inject constructor(
    private val pointsApi: PointsApi
) {
    suspend fun purchaseEmoticonPack(packId: Int): Response<PointResponseDto> {
        val request = PurchaseEmoticonPackRequestDto(packId)
        return pointsApi.purchaseEmoticonPack(request)
    }

    suspend fun purchasePoint(amount: Int, impUid: String): Response<PointResponseDto> {
        val request = PurchasePointRequestDto(point = amount, imp_uid = impUid)
        return pointsApi.purchasePoint(request)
    }

}