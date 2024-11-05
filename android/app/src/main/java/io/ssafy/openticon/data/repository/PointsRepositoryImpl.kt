package io.ssafy.openticon.data.repository

import io.ssafy.openticon.data.model.PurchaseEmoticonPackRequestDto
import io.ssafy.openticon.data.remote.PointsApi
import io.ssafy.openticon.domain.repository.PointsRepository
import javax.inject.Inject

class PointsRepositoryImpl @Inject constructor(
    private val pointsApi: PointsApi
) : PointsRepository {
    override suspend fun purchasePack(packId: Int): Result<String> {
        val response = pointsApi.purchaseEmoticonPack(
            PurchaseEmoticonPackRequestDto(emoticonPackId = packId)
        )

        return if (response.isSuccessful) {
            Result.success(response.body() ?: "구매 성공")
        } else {
            Result.failure(Exception(response.body()))
        }
    }
}