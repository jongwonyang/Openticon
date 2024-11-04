package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.model.EmoticonPack
import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.data.repository.PointsRepository
import javax.inject.Inject

class PurchaseEmoticonUseCase @Inject constructor(
    private val pointsRepository: PointsRepository,
    private val emoticonPacksRepository: EmoticonPacksRepository
) {
    suspend operator fun invoke(packId: Int): Result<String> {
        val result = pointsRepository.purchaseEmoticonPack(packId)
        return if (result.isSuccessful) {
            emoticonPacksRepository.savedEmoticonPack(EmoticonPack(packId, false))
            Result.success(result.body() ?: "구매에 성공했습니다.")
        } else {
            Result.failure(Exception(result.errorBody()?.string() ?: "Unknown error"))
        }
    }
}