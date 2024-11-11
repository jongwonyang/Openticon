package io.ssafy.openticon.data.repository

import com.google.gson.Gson
import io.ssafy.openticon.data.model.ErrorResponse
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
            Result.success(response.body()?.message ?: "구매 성공")
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody?.let {
                try {
                    Gson().fromJson(it, ErrorResponse::class.java).message
                } catch (e: Exception) {
                    e.printStackTrace()
                    "알 수 없는 오류가 발생했습니다."
                }
            }
            Result.failure(Exception(errorMessage))
        }
    }
}